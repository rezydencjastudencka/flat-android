package pl.rpieja.flat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radix on 2017-11-01.
 */

public class ChargesTab extends Fragment {
    private List<String> data = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.charges_tab, container, false);
        ListView listView = rootView.findViewById(R.id.chargesListView);
        listView.setAdapter(new ArrayAdapter<>(this.getContext(), R.layout.charges_item, data));

        return rootView;
    }
}
