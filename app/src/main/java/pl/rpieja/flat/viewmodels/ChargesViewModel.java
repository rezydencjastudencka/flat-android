package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.authentication.AccountService;
import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.dto.Income;
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

    public List<Income> getIncomesList() {
        if (charges.getValue() == null) return new ArrayList<>();
        return charges.getValue().getIncomes();
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
}
