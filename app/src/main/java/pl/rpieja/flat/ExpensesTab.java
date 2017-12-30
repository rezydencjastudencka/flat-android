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
import java.util.List;

import pl.rpieja.flat.dto.Income;
import pl.rpieja.flat.viewmodels.ChargesViewModel;

/**
 * Created by radix on 2017-11-01.
 */

public class ExpensesTab extends Fragment {
    public ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.expenses_tab, container, false);
        listView = rootView.findViewById(R.id.expensesListView);
        ChargesViewModel chargesViewModel = ViewModelProviders.of(getActivity()).get(ChargesViewModel.class);

        updateListWithCharges(chargesViewModel.getIncomesList());

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

    public void updateListWithCharges(final List<Income> incomes) {
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return incomes.size();
            }

            @Override
            public Object getItem(int i) {
                return incomes.get(i);
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

                String test = incomes.get(i).from.name;

                chargeName.setText(CapitalizeFirstLetter(incomes.get(i).name));
                chargeAmount.setText(round(incomes.get(i).amount, 2).toString());
                chargeUsers.setText(test);

                return view;
            }
        });
    }
}
