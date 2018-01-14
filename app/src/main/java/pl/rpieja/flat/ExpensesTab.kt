package pl.rpieja.flat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.rpieja.flat.dto.ChargesDTO
import pl.rpieja.flat.dto.Expense
import pl.rpieja.flat.viewmodels.ChargesViewModel

class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val chargeName: TextView = itemView.findViewById(R.id.chargeName)
    val chargeAmount: TextView = itemView.findViewById(R.id.chargeAmount)
    val chargeUsers: TextView = itemView.findViewById(R.id.chargeUsers)
}

class ExpensesTab: ChargeLikeTab<Expense, ExpenseViewHolder>() {
    override fun observe() {
        ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java).charges
                .observe(this, Observer { x: ChargesDTO? -> setData(x!!.incomes) })
    }

    override val layoutId: Int = R.layout.expenses_tab
    override val itemLayoutId: Int = R.layout.charges_item
    override val recyclerViewId: Int = R.id.expensesListView

    override fun updateItemView(viewHolder: ExpenseViewHolder, item: Expense) {
        viewHolder.chargeName.text = item.name
        viewHolder.chargeAmount.text = currencyFormat.format(item.amount).toString()
        viewHolder.chargeUsers.text = item.from.name
    }

    override fun createViewHolder(view: View): ExpenseViewHolder = ExpenseViewHolder(view)
}
