package pl.rpieja.flat.fragment

import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.dto.User
import pl.rpieja.flat.viewmodels.Loadable
import java.util.*

class SummaryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val amountTextView: TextView = itemView.findViewById(R.id.summaryAmount)
    val userTextView: TextView = itemView.findViewById(R.id.summaryUser)
}

abstract class SummaryLayoutFragment<VM, DTO>:
        ChargeLikeFragment<Summary, SummaryViewHolder, VM, DTO>()
        where VM: Loadable<DTO>, VM: ViewModel {
    override val itemLayoutId: Int = R.layout.summary_item

    override fun getUsers(item: Summary): List<User> = item.fromUsers

    override fun updateItemView(viewHolder: SummaryViewHolder, item: Summary) {
        formatAmount(viewHolder.amountTextView, item.amount)
        viewHolder.userTextView.text = item.chargeName
    }

    override fun createViewHolder(view: View): SummaryViewHolder = SummaryViewHolder(view)

    override fun formatAmount(amountTextView: TextView, amount: Double) {
        val format = AmountFormatter(Locale.getDefault(),
                Currency.getInstance(Locale.getDefault())).format(amount)
        amountTextView.text = format.value
        amountTextView.setTextColor(context!!.getColor(format.color))
    }
}