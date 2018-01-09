package pl.rpieja.flat

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.viewmodels.ChargesViewModel
import java.text.NumberFormat
import java.util.*

class SummaryTab : Fragment() {
    private class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val amountTextView: TextView = itemView.findViewById(R.id.summaryAmount)
        val userTextView: TextView = itemView.findViewById(R.id.summaryUser)
    }

    private class ItemAdapter(context: Context, private val summaries: List<Summary>):
            RecyclerView.Adapter<ItemViewHolder>() {
        private val colorNegative = context.getColor(R.color.amountNegative)
        private val colorPositive = context.getColor(R.color.amountPositive)
        private val colorNeutral = context.getColor(R.color.amountNeutral)
        private val currencyFormat = NumberFormat.getCurrencyInstance()

        init {
            currencyFormat.currency = Currency.getInstance("USD") // TODO fetch
        }

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            val summary = summaries[position]
            val amountColor = when {
                summary.amount > 0 -> colorNegative
                summary.amount < 0 -> colorPositive
                else -> colorNeutral
            }

            holder!!.userTextView.text = summary.name

            holder.amountTextView.text = currencyFormat.format(summary.amount)
            holder.amountTextView.setTextColor(amountColor)
        }

        override fun getItemCount(): Int = summaries.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
            val inflater = LayoutInflater.from(parent!!.context)
            return ItemViewHolder(inflater.inflate(R.layout.summary_item, parent, false))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.summary_tab, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.summary_list)
        val chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)

        recyclerView.adapter = ItemAdapter(context!!, chargesViewModel.charges.value!!.summary)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        return rootView
    }
}