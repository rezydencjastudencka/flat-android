package pl.rpieja.flat;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import pl.rpieja.flat.viewmodels.ChargesViewModel;

/**
 * Created by radix on 2017-11-01.
 */

public class ChargesTab extends Fragment {

    private List<String> data = new ArrayList<>();
    private ChargesViewModel chargesViewModel;

    public ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.charges_tab, container, false);

        chargesViewModel = ViewModelProviders.of(getActivity()).get(ChargesViewModel.class);

        listView = rootView.findViewById(R.id.chargesListView);
        listView.setAdapter(new ChargesAdapter());


        // listView.setAdapter(new ArrayAdapter<>(this.getContext(), R.layout.charges_item, data));
        //listView.setAdapter(new ChargesAdapter(this.getContext(), R.layout.charges_item, data));

        return rootView;
    }


    public class ChargesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            //return 0xcafe;
            return chargesViewModel.getCharges() != null ?
                    chargesViewModel.getCharges().getCharges().length :
                    0;
        }

        @Override
        public Object getItem(int i) {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.charges_item, null);

            TextView chargeName = (TextView) view.findViewById(R.id.chargeName);
            TextView chargeAmount = (TextView) view.findViewById(R.id.chargeAmount);
            TextView chargeUsers = (TextView) view.findViewById(R.id.chargeUsers);

            List<String> userList = new ArrayList<>();

            for (int j = 0; chargesViewModel.getCharges().getCharges()[i].to.length < j; j++)
                userList.add(chargesViewModel.getCharges().getCharges()[i].to[j].name);

            String test = android.text.TextUtils.join(", ", userList);


            chargeName.setText(chargesViewModel.getCharges().getCharges()[i].name);
            chargeAmount.setText(chargesViewModel.getCharges().getCharges()[i].amount.toString());
            chargeUsers.setText(test);

            return view;
        }
    }
}
