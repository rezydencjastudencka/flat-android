package pl.rpieja.flat.dialog

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog
import pl.rpieja.flat.R
import pl.rpieja.flat.viewmodels.ChargesViewModel
import java.util.*

class MonthPickerDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "pl.rpieja.flat.dialog.MonthPickerDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)

        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.MONTH, viewModel.month - 1)
        cal.set(Calendar.YEAR, viewModel.year)

        return YearMonthPickerDialog(context, YearMonthPickerDialog.OnDateSetListener { year, month ->
            viewModel.load(context!!, month + 1, year)
        }, R.style.monthPickerStyle, R.color.colorBackgroundLight, cal).getmDialog()

    }
}