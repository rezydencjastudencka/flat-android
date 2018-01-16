package pl.rpieja.flat.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.rpieja.flat.R
import pl.rpieja.flat.activity.NewChargeActivity
import pl.rpieja.flat.dto.Charge
import pl.rpieja.flat.dto.ChargesDTO
import pl.rpieja.flat.viewmodels.ChargesViewModel

class ChargeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val chargeName: TextView = itemView.findViewById(R.id.chargeName)
    val chargeAmount: TextView = itemView.findViewById(R.id.chargeAmount)
    val chargeUsers: TextView = itemView.findViewById(R.id.chargeUsers)
}

class ChargesTab: ChargeLikeTab<Charge, ChargeViewHolder>() {
    override val layoutId: Int = R.layout.charges_tab
    override val itemLayoutId: Int = R.layout.charges_item
    override val recyclerViewId: Int = R.id.chargesListView

    override fun updateItemView(viewHolder: ChargeViewHolder, item: Charge) {
        viewHolder.chargeName.text = item.name
        viewHolder.chargeAmount.text = currencyFormat.format(item.amount)
        viewHolder.chargeUsers.text = android.text.TextUtils.join(", ",
                item.to.map { user -> user.name })
    }

    override fun createViewHolder(view: View): ChargeViewHolder = ChargeViewHolder(view)

    override fun observe() {
        val chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)
        chargesViewModel.charges
                .observe(parentFragment!!, Observer { x: ChargesDTO? -> setData(x!!.charges) })
    }

    /*
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val fab = activity!!.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener({ _ ->
            startActivity(Intent(activity!!, NewChargeActivity::class.java))
        })

        return view
    }
    */
}