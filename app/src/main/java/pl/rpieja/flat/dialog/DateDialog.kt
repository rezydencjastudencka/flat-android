package pl.rpieja.flat.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DateDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: ((Calendar) -> Unit)? = null

    fun setDateSetListener(listener: (Calendar) -> Unit) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        assert(listener != null)
        val calendar = GregorianCalendar(year, month, day)
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        listener!!(calendar)
    }
}
