package pl.rpieja.flat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
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
        ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java).charges
                .observe(this, Observer { x: ChargesDTO? -> setData(x!!.charges) })
    }
}