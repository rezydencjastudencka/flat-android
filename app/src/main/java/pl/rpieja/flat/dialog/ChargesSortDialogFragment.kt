package pl.rpieja.flat.dialog

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import pl.rpieja.flat.R
import pl.rpieja.flat.viewmodels.ChargesViewModel

class ChargesSortDialogFragment: DialogFragment() {
    companion object {
        val tag = "pl.rpieja.flat.dialog.ChargesSortDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)
        val currentItem = activity!!.findViewById<ViewPager>(R.id.container).currentItem
        return ChargesSortDialog(context!!, chargesViewModel).create(currentItem)!!
    }
}