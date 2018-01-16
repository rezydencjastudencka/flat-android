package pl.rpieja.flat.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.ChargesDTO
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.viewmodels.ChargesViewModel

class SummaryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val amountTextView: TextView = itemView.findViewById(R.id.summaryAmount)
    val userTextView: TextView = itemView.findViewById(R.id.summaryUser)
}

class SummaryTab: ChargeLikeTab<Summary, SummaryViewHolder>() {

    override val layoutId: Int = R.layout.charges_tab
    override val itemLayoutId: Int = R.layout.summary_item
    override val recyclerViewId: Int = R.id.chargesListView

    override fun updateItemView(viewHolder: SummaryViewHolder, item: Summary) {
        val colorNegative = context!!.getColor(R.color.amountNegative)
        val colorPositive = context!!.getColor(R.color.amountPositive)
        val colorNeutral = context!!.getColor(R.color.amountNeutral)

        val amountColor = when {
            item.amount > 0 -> colorNegative
            item.amount < 0 -> colorPositive
            else -> colorNeutral
        }

        viewHolder.userTextView.text = item.name

        viewHolder.amountTextView.text = currencyFormat.format(item.amount)
        viewHolder.amountTextView.setTextColor(amountColor)
    }

    override fun createViewHolder(view: View): SummaryViewHolder = SummaryViewHolder(view)

     override fun observe() {
        ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java).charges
                .observe(this, Observer { x: ChargesDTO? -> setData(x!!.summary) })
    }
}
