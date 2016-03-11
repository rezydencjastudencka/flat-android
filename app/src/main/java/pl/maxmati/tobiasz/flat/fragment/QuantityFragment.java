package pl.maxmati.tobiasz.flat.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import pl.maxmati.tobiasz.flat.R;

/**
 * Created by mmos on 20.02.16.
 *
 * @author mmos
 */
public class QuantityFragment extends Fragment {
    public static final String TAG = "QuantityFragment";

    public static final String ARGUMENT_ENABLE_DECREASE_BUTTON_MAX_VALUE =
            "enableDecreaseButtonMaxValue";

    private static final String STATE_QUANTITY_VALUE = "quantityValue";

    private OnQuantityActionListener mCallback;

    private NumberPicker quantityPicker;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnQuantityActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnQuantityActionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_quantity, container, false);
        quantityPicker = (NumberPicker) view.findViewById(R.id.quantity_picker);

        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(10);
        if(savedInstanceState != null) {
            Log.d(TAG, "Restored value: " + savedInstanceState.getInt(STATE_QUANTITY_VALUE));
            quantityPicker.setValue(savedInstanceState.getInt(STATE_QUANTITY_VALUE));
        } else {
            Log.d(TAG, "No value to restore");
        }
        quantityPicker.setSaveFromParentEnabled(false);
        quantityPicker.setSaveEnabled(true);

        view.findViewById(R.id.increment_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onIncrementPressed(quantityPicker.getValue());
            }
        });
        view.findViewById(R.id.decrement_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDecrementPressed(quantityPicker.getValue());
            }
        });
        setEnabledMaxValue((Button) view.findViewById(R.id.decrement_button));

        return view;
    }

    private void setEnabledMaxValue(final Button button) {
        Bundle arguments = getArguments();
        final int enableValue;

        if(quantityPicker == null || button == null || getArguments() == null || !arguments
                .containsKey(ARGUMENT_ENABLE_DECREASE_BUTTON_MAX_VALUE))
            return;

        enableValue = arguments.getInt(ARGUMENT_ENABLE_DECREASE_BUTTON_MAX_VALUE);
        button.setEnabled(quantityPicker.getValue() <= enableValue);

        quantityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int preVal, int postVal) {
                button.setEnabled(postVal <= enableValue);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Saving state"); // TODO: store quantity picker value during transitions
        if(quantityPicker != null) {
            outState.putInt(STATE_QUANTITY_VALUE, quantityPicker.getValue());
            Log.d(TAG, "Stored value " + quantityPicker.getValue());
        }
    }

    public interface OnQuantityActionListener {
        void onIncrementPressed(int quantity);
        void onDecrementPressed(int quantity);
    }
}
