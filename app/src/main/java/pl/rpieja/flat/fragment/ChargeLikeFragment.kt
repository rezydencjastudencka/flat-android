package pl.rpieja.flat.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.ChargeLike
import pl.rpieja.flat.dto.User
import java.text.NumberFormat
import java.util.*

abstract class ChargeLikeFragment<T: ChargeLike, VH: RecyclerView.ViewHolder, VM: ViewModel, DTO>:
        Fragment() {
    open val layoutId: Int = R.layout.charges_tab
    open val itemLayoutId: Int = R.layout.charges_item
    open val recyclerViewId: Int = R.id.chargesListView
    abstract val modelClass: Class<VM>

    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()

    init {
        currencyFormat.currency = Currency.getInstance("USD") // TODO fetch
    }

    abstract fun getUsers(item: T): List<User>
    abstract fun extractLiveData(vm: VM): LiveData<DTO>
    abstract fun extractEntityFromDTO(dto: DTO): List<T>
    abstract fun updateItemView(viewHolder: VH, item: T)
    abstract fun createViewHolder(view: View): VH
    abstract fun formatAmount(amountTextView: TextView, amount: Double)

    private var recyclerView: RecyclerView? = null
    private var elements: List<T>? = null

    private fun setData(data: List<T>) {
        recyclerView?.adapter = ItemAdapter(data)
        elements = data
    }

    inner class ItemAdapter(val list: List<T>): RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
            val view = LayoutInflater.from(context)
                    .inflate(this@ChargeLikeFragment.itemLayoutId, parent, false)
            return this@ChargeLikeFragment.createViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: VH?, position: Int) =
                this@ChargeLikeFragment.updateItemView(holder!!, list[position])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
    }

    private fun observe() {
        val viewModel: VM = ViewModelProviders.of(activity!!).get(modelClass)
        extractLiveData(viewModel).observe(this, Observer<DTO> { dto ->
            setData(extractEntityFromDTO(dto!!))
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        recyclerView = rootView.findViewById(recyclerViewId)

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = ItemAdapter(elements ?: emptyList())
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        return rootView
    }
}

class ChargeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val chargeName: TextView = itemView.findViewById(R.id.chargeName)
    val chargeAmount: TextView = itemView.findViewById(R.id.chargeAmount)
    val chargeUsers: TextView = itemView.findViewById(R.id.chargeUsers)
}

abstract class ChargeLayoutFragment<T: ChargeLike, VM: ViewModel, DTO>:
        ChargeLikeFragment<T, ChargeViewHolder, VM, DTO>() {
    override fun createViewHolder(view: View): ChargeViewHolder = ChargeViewHolder(view)

    override fun updateItemView(viewHolder: ChargeViewHolder, item: T) {
        viewHolder.chargeName.text = item.chargeName
        formatAmount(viewHolder.chargeAmount, item.chargeAmount)
        viewHolder.chargeUsers.text = android.text.TextUtils.join(", ",
                getUsers(item).map { user -> user.name })
    }

    override fun formatAmount(amountTextView: TextView, amount: Double) {
        amountTextView.text = currencyFormat.format(amount).toString()
    }
}