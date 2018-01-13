package pl.rpieja.flat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.rpieja.flat.dto.Expense
import pl.rpieja.flat.viewmodels.ChargesViewModel
import java.text.NumberFormat
import java.util.*

class ExpensesTab : Fragment() {
    private class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val chargeName: TextView = itemView.findViewById(R.id.chargeName)
        val chargeAmount: TextView = itemView.findViewById(R.id.chargeAmount)
        val chargeUsers: TextView = itemView.findViewById(R.id.chargeUsers)
    }

    private class ItemAdapter(private val expenses: List<Expense>):
            RecyclerView.Adapter<ItemViewHolder>() {
        private val currencyFormat = NumberFormat.getCurrencyInstance()

        init {
            currencyFormat.currency = Currency.getInstance("USD") // TODO fetch
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
            val inflater = LayoutInflater.from(parent!!.context)
            return ItemViewHolder(inflater.inflate(R.layout.charges_item, parent, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            val income = expenses[position]
            holder!!.chargeName.text = income.name
            holder.chargeAmount.text = currencyFormat.format(income.amount).toString()
            holder.chargeUsers.text = income.from.name
        }

        override fun getItemCount(): Int = expenses.size
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.expenses_tab, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.expensesListView)
        val chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        chargesViewModel.chargesList.observe(this, Observer { chargesDTO ->
            recyclerView.adapter = ItemAdapter(chargesDTO!!.incomes)
        })

        return rootView
    }
}
