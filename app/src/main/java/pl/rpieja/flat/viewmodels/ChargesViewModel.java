package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import pl.rpieja.flat.ChargesActivity;
import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.containers.APIChargesContainer;
import pl.rpieja.flat.dto.Charges;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.dto.Incomes;
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

    public Incomes[] getIncomesList() {
        if (charges.getValue() == null) return new Incomes[0];
        return charges.getValue().getIncomes();
    }

    public void loadCharges(Context context, int month, int year) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(context));
        FlatAPI flatAPI = new FlatAPI(cookieJar);

        // Do not refetch data if month/year are the same
        if (this.month != null && this.year != null && month == this.month && year == this.year)
            return;

        this.month = month;
        this.year = year;

        AsyncGetCharges.run(flatAPI, month, year, new AsyncGetCharges.Callable<ChargesDTO>() {
            @Override
            public void onCall(ChargesDTO chargesDTO) {
                charges.setValue(chargesDTO);
            }
        });

    }
}
