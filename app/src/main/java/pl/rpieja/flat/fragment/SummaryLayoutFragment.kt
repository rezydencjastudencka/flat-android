package pl.rpieja.flat.fragment

import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.dto.User

class SummaryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val amountTextView: TextView = itemView.findViewById(R.id.summaryAmount)
    val userTextView: TextView = itemView.findViewById(R.id.summaryUser)
}

abstract class SummaryLayoutFragment<VM: ViewModel, DTO>:
        ChargeLikeFragment<Summary, SummaryViewHolder, VM, DTO>() {
    override val itemLayoutId: Int = R.layout.summary_item

    override fun getUsers(item: Summary): List<User> = item.fromUsers!!

    override fun updateItemView(viewHolder: SummaryViewHolder, item: Summary) {
        val colorNegative = context!!.getColor(R.color.amountNegative)
        val colorPositive = context!!.getColor(R.color.amountPositive)
        val colorNeutral = context!!.getColor(R.color.amountNeutral)

        val amountColor = when {
            item.chargeAmount!! > 0 -> colorNegative
            item.chargeAmount!! < 0 -> colorPositive
            else -> colorNeutral
        }

        viewHolder.amountTextView.setTextColor(amountColor)
        viewHolder.amountTextView.text = currencyFormat.format(item.amount)

        viewHolder.userTextView.text = item.name
    }

    override fun createViewHolder(view: View): SummaryViewHolder = SummaryViewHolder(view)
}