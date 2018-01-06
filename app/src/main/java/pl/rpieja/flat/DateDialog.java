package pl.rpieja.flat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import pl.rpieja.flat.util.Callable;

/**
 * Created by radix on 29.11.17.
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Callable<Calendar> listener;

    public void setDateSetListener(Callable<Calendar> listener) {
        this.listener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        assert listener != null;
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        listener.onCall(calendar);
    }
}
