package pl.rpieja.flat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import pl.rpieja.flat.dto.ChargesDTO;
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

        chargesViewModel.getChargesList().observe(this, new Observer<ChargesDTO>() {
            @Override
            public void onChanged(@Nullable ChargesDTO chargesDTO) {
                if (chargesDTO.getCharges() == null)
                    return;
                updateListWithCharges(chargesDTO.getCharges());
            }
        });

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

    public void updateListWithCharges(final List<Charges> charges) {
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return charges.size();
            }

            @Override
            public Object getItem(int i) {
                return charges.get(i);
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

                for (int j = 0; j < charges.get(i).to.size(); j++)
                    userList.add(CapitalizeFirstLetter(charges.get(i).to.get(j).name));

                String test = android.text.TextUtils.join(", ", userList);

                chargeName.setText(CapitalizeFirstLetter(charges.get(i).name));
                chargeAmount.setText(round(charges.get(i).amount, 2).toString());
                chargeUsers.setText(test);
                return view;
            }
        });
    }
}
