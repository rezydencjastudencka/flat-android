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
import pl.rpieja.flat.dto.Charge
import pl.rpieja.flat.viewmodels.ChargesViewModel
import java.text.NumberFormat
import java.util.*

class ChargesTab : Fragment() {
    private class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val chargeName: TextView = itemView.findViewById(R.id.chargeName)
        val chargeAmount: TextView = itemView.findViewById(R.id.chargeAmount)
        val chargeUsers: TextView = itemView.findViewById(R.id.chargeUsers)
    }

    private class ItemAdapter(val charges: List<Charge>): RecyclerView.Adapter<ItemViewHolder>() {
        private val currencyFormat = NumberFormat.getCurrencyInstance()

        init {
            currencyFormat.currency = Currency.getInstance("USD") // TODO fetch
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.charges_item, parent, false))

        override fun getItemCount(): Int = charges.size

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            val charge = charges[position]

            holder!!.chargeName.text = charge.name
            holder.chargeAmount.text = currencyFormat.format(charge.amount)
            holder.chargeUsers.text = android.text.TextUtils.join(", ",
                    charge.to.map { user -> user.name })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.charges_tab, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.chargesListView)
        val chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        chargesViewModel.chargesList.observe(this, Observer { chargesDTO ->
            recyclerView.adapter = ItemAdapter(chargesDTO!!.charges)
        })

        return rootView
    }
}
