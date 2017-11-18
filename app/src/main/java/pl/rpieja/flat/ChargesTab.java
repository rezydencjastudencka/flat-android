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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import pl.rpieja.flat.dto.Charges;
import pl.rpieja.flat.viewmodels.ChargesViewModel;

/**
 * Created by radix on 2017-11-01.
 */

public class ChargesTab extends Fragment {
    public ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.charges_tab, container, false);
        listView = rootView.findViewById(R.id.chargesListView);
        ChargesViewModel chargesViewModel = ViewModelProviders.of(getActivity()).get(ChargesViewModel.class);

        updateListWithCharges(chargesViewModel.getChargesList());

        return rootView;
    }

    private static String CapitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private static Double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateListWithCharges(final Charges[] charges) {
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return charges.length;
            }

            @Override
            public Object getItem(int i) {
                return charges[i];
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = getLayoutInflater().inflate(R.layout.charges_item, null);

                TextView chargeName = view.findViewById(R.id.chargeName);
                TextView chargeAmount = view.findViewById(R.id.chargeAmount);
                TextView chargeUsers = view.findViewById(R.id.chargeUsers);

                List<String> userList = new ArrayList<>();

                for (int j = 0; j < charges[i].to.length; j++)
                    userList.add(CapitalizeFirstLetter(charges[i].to[j].name));

                String test = android.text.TextUtils.join(", ", userList);

                chargeName.setText(CapitalizeFirstLetter(charges[i].name));
                chargeAmount.setText(round(charges[i].amount, 2).toString());
                chargeUsers.setText(test);
                return view;
            }
        });
    }
}
