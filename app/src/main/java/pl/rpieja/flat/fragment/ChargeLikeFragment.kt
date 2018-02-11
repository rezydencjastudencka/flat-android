package pl.rpieja.flat.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.ChargeLike
import pl.rpieja.flat.dto.User
import pl.rpieja.flat.viewmodels.Loadable
import java.text.NumberFormat
import java.util.*

abstract class ChargeLikeFragment<T: ChargeLike, VH: RecyclerView.ViewHolder, VM , DTO>:
        Fragment() where VM: Loadable<DTO>, VM: ViewModel {
    open val listBottomPaddingDp = 0f
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
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
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
            swipeRefreshLayout?.isRefreshing = false
        })
    }

    private fun reload() {
        val viewModel: VM = ViewModelProviders.of(activity!!).get(modelClass)
        viewModel.load(context!!, true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        swipeRefreshLayout = rootView as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener { reload() }
        recyclerView = rootView.findViewById(recyclerViewId)

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = ItemAdapter(elements ?: emptyList())
        val paddingPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, listBottomPaddingDp,
                resources.displayMetrics).toInt()
        recyclerView?.setPadding(0, 0, 0, paddingPx)

        return rootView
    }
}

class ChargeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val chargeName: TextView = itemView.findViewById(R.id.chargeName)
    val chargeAmount: TextView = itemView.findViewById(R.id.chargeAmount)
    val chargeUsers: TextView = itemView.findViewById(R.id.chargeUsers)
}

abstract class ChargeLayoutFragment<T: ChargeLike, VM, DTO>:
        ChargeLikeFragment<T, ChargeViewHolder, VM, DTO>() where VM: Loadable<DTO>, VM: ViewModel {
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