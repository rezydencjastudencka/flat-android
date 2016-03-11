package pl.maxmati.tobiasz.flat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import pl.maxmati.tobiasz.flat.R;

/**
 * Created by mmos on 20.02.16.
 *
 * @author mmos
 */
public class ValueFragment extends Fragment {
    public static final String TAG = "ValueFragment";

    public static final String STATE_TOTAL_VALUE = "totalValue";
    public static final String STATE_DECIMAL_VALUE = "decimalValue";

    private NumberPicker totalPicker;
    private NumberPicker decimalPicker;

    public ValueFragment() {
        Log.d(TAG, "New instance created");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_value, container, false);
        totalPicker = (NumberPicker) view.findViewById(R.id.total_picker);
        decimalPicker = (NumberPicker) view.findViewById(R.id.decimal_picker);

        totalPicker.setMaxValue(20);

        decimalPicker.setMaxValue(99);
        decimalPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        if(savedInstanceState != null) {
            totalPicker.setValue(savedInstanceState.getInt(STATE_TOTAL_VALUE));
            decimalPicker.setValue(savedInstanceState.getInt(STATE_DECIMAL_VALUE));
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        view.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_TOTAL_VALUE, totalPicker.getValue());
        outState.putInt(STATE_DECIMAL_VALUE, decimalPicker.getValue());
    }

    public double getValue() {
        return totalPicker.getValue() + (double) decimalPicker.getValue() / 100;
    }
}
