package pl.rpieja.flat.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.Transfer
import pl.rpieja.flat.dto.TransfersDTO
import pl.rpieja.flat.viewmodels.TransfersViewModel

class TransfersOutgoingTab: ChargeLikeTab<Transfer, ChargeViewHolder>() {
    override val layoutId: Int = R.layout.charges_tab
    override val itemLayoutId: Int = R.layout.charges_item
    override val recyclerViewId: Int = R.id.chargesListView

    override fun updateItemView(viewHolder: ChargeViewHolder, item: Transfer) {
        viewHolder.chargeName.text = item.name
        viewHolder.chargeAmount.text = currencyFormat.format(item.amount).toString()
        viewHolder.chargeUsers.text = item.from!!.name
    }

    override fun createViewHolder(view: View): ChargeViewHolder = ChargeViewHolder(view)

    override fun observe() {
        ViewModelProviders.of(activity!!).get(TransfersViewModel::class.java).getChargesList()
                .observe(this, Observer { x: TransfersDTO? -> setData(x!!.outgoing!!) })
    }
}
