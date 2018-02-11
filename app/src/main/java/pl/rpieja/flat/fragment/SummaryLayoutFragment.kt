package pl.rpieja.flat.fragment

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.dto.User
import pl.rpieja.flat.viewmodels.Loadable
import kotlin.math.absoluteValue

class SummaryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val amountTextView: TextView = itemView.findViewById(R.id.summaryAmount)
    val userTextView: TextView = itemView.findViewById(R.id.summaryUser)
}

abstract class SummaryLayoutFragment<VM, DTO>:
        ChargeLikeFragment<Summary, SummaryViewHolder, VM, DTO>()
        where VM: Loadable<DTO>, VM: ViewModel{
    override val itemLayoutId: Int = R.layout.summary_item

    override fun getUsers(item: Summary): List<User> = item.fromUsers

    override fun updateItemView(viewHolder: SummaryViewHolder, item: Summary) {
        formatAmount(viewHolder.amountTextView, item.amount)
        viewHolder.userTextView.text = item.chargeName
    }

    override fun createViewHolder(view: View): SummaryViewHolder = SummaryViewHolder(view)

    // getColor() requires context, available after onAttach
    private var colorNegative: Int? = null
    private var colorPositive: Int? = null
    private var colorNeutral: Int? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        colorNegative = context!!.getColor(R.color.amountNegative)
        colorPositive = context.getColor(R.color.amountPositive)
        colorNeutral = context.getColor(R.color.amountNeutral)
    }

    override fun formatAmount(amountTextView: TextView, amount: Double) {
        val roundAmount = if (amount.absoluteValue < 0.01) 0.0 else amount
        val color = when {
            amount >= 0.01 -> colorNegative
            amount <= -0.01 -> colorPositive
            else -> colorNeutral
        }

        amountTextView.text = currencyFormat.format(roundAmount).toString()
        amountTextView.setTextColor(color!!)
    }
}