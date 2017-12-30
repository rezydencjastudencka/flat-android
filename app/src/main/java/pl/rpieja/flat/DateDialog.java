package pl.rpieja.flat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by radix on 29.11.17.
 */

public class DateDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    EditText txtDate;

    public void setEditText(View view){
        txtDate=(EditText) view;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState){
        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        String date = year+"-"+(month+1)+"-"+day;
        txtDate.setText(date);
    }
}
