package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.authentication.AccountService;
import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.dto.Charge;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.dto.Expense;
import pl.rpieja.flat.dto.Summary;
import pl.rpieja.flat.tasks.AsyncGetCharges;

/**
 * Created by radix on 14.11.17.
 */

public class ChargesViewModel extends ViewModel {
    private MutableLiveData<ChargesDTO> charges = new MutableLiveData<>();
    private Integer month, year;

    public MutableLiveData<ChargesDTO> getCharges() {
        return charges;
    }

    public MutableLiveData<ChargesDTO> getChargesList() {
        return charges;
    }

    public List<Expense> getIncomesList() {
        if (charges.getValue() == null) return new ArrayList<>();
        return charges.getValue().getIncomes();
    }

    public void sortCharges(Comparator<Charge> comparator) {
        if(charges.getValue() == null || charges.getValue().getCharges() == null) return;

        Collections.sort(charges.getValue().getCharges(), comparator);
        charges.setValue(charges.getValue());
    }

    public void sortIncomes(Comparator<Expense> comparator) {
        if(charges.getValue() == null || charges.getValue().getIncomes() == null) return;

        Collections.sort(charges.getValue().getIncomes(), comparator);
        charges.setValue(charges.getValue());
    }

    public void sortSummary(Comparator<Summary> comparator) {
        if(charges.getValue() == null || charges.getValue().getSummary() == null) return;

        Collections.sort(charges.getValue().getSummary(), comparator);
        charges.setValue(charges.getValue());
    }

    public void loadCharges(Context context) {
        Calendar calendar = Calendar.getInstance();
        loadCharges(context, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public void loadCharges(Context context, int month, int year) {
        FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(context));

        // Do not refetch data if month/year are the same
        if (this.month != null && this.year != null && month == this.month && year == this.year)
            return;

        this.month = month;
        this.year = year;

        new AsyncGetCharges(flatAPI, month, year,
                chargesDTO -> charges.setValue(chargesDTO),
                () -> AccountService.removeCurrentAccount(context)).execute();
    }

    public int getMonth() {
        return month;
    }
}
