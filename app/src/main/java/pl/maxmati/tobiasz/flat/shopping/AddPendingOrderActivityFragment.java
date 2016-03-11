package pl.maxmati.tobiasz.flat.shopping;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pl.maxmati.tobiasz.flat.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddPendingOrderActivityFragment extends Fragment {

    public AddPendingOrderActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pending_order, container, false);

        Spinner prioritySpinner = (Spinner) view.findViewById(R.id
                .fragment_add_pending_order_product_priority_spinner);
        ArrayAdapter<Priority> priorityAdapter = new ArrayAdapter<Priority>(getContext(), R.layout
                .priority_item, R.id.priority_item_textview, Priority.getPredefined()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                View colorView = view.findViewById(R.id.priority_item_colorview);
                colorView.setBackgroundColor(getResources().getColor(getItem(position)
                        .getColorResId()));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getView(position, convertView, parent);
            }
        };
        prioritySpinner.setAdapter(priorityAdapter);
        return view;
    }
}
