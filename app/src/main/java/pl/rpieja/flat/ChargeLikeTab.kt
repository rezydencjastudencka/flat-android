package pl.rpieja.flat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.text.NumberFormat
import java.util.*

abstract class ChargeLikeTab<T, VH: RecyclerView.ViewHolder>: Fragment() {
    abstract val layoutId: Int
    abstract val itemLayoutId: Int
    abstract val recyclerViewId: Int

    private var recyclerView: RecyclerView? = null

    abstract fun updateItemView(viewHolder: VH, item: T)
    abstract fun createViewHolder(view: View): VH
    abstract fun observe()

    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()

    init {
        currencyFormat.currency = Currency.getInstance("USD") // TODO fetch
    }

    fun setData(data: List<T>) {
        recyclerView?.adapter = ItemAdapter(data)
    }

    inner class ItemAdapter(val list: List<T>): RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
            val view = LayoutInflater.from(parent!!.context)
                    .inflate(this@ChargeLikeTab.itemLayoutId, parent, false)
            return this@ChargeLikeTab.createViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: VH?, position: Int) =
                this@ChargeLikeTab.updateItemView(holder!!, list[position])
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        recyclerView = rootView.findViewById(recyclerViewId)

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = ItemAdapter(emptyList())
        observe()

        return rootView
    }
}
