package pl.rpieja.flat.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog
import pl.rpieja.flat.R
import pl.rpieja.flat.viewmodels.ChargesViewModel
import pl.rpieja.flat.viewmodels.YearMonth
import java.util.*

class MonthPickerDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "pl.rpieja.flat.dialog.MonthPickerDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)

        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.MONTH, viewModel.date.value!!.month - 1)
        cal.set(Calendar.YEAR, viewModel.date.value!!.year)

        return YearMonthPickerDialog(context, YearMonthPickerDialog.OnDateSetListener { year, month ->
            viewModel.load(context!!, YearMonth(month + 1, year))
        }, R.style.monthPickerStyle, R.color.colorBackgroundLight, cal).dialog

    }
}