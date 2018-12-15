package pl.rpieja.flat.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import pl.rpieja.flat.R
import pl.rpieja.flat.viewmodels.ChargesViewModel

class ChargesSortDialogFragment: DialogFragment() {
    companion object {
        const val tag = "pl.rpieja.flat.dialog.ChargesSortDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)
        val currentItem = activity!!.findViewById<ViewPager>(R.id.container).currentItem
        return ChargesSortDialog(context!!, chargesViewModel).create(currentItem)!!
    }
}