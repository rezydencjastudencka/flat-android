package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import pl.rpieja.flat.containers.APIChargesContainer;
import pl.rpieja.flat.dto.Charges;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.tasks.AsyncGetCharges;

/**
 * Created by radix on 14.11.17.
 */

public class ChargesViewModel extends ViewModel {
    private MutableLiveData<ChargesDTO> charges = new MutableLiveData<>();

    public MutableLiveData<ChargesDTO> getCharges() {
        return charges;
    }

    public void loadCharges(APIChargesContainer apiChargesContainer) {
        new AsyncGetCharges().execute(new AsyncGetCharges.Params(apiChargesContainer, new AsyncGetCharges.Callable<ChargesDTO>() {
            @Override
            public void onCall(ChargesDTO chargesDTO) {
                charges.setValue(chargesDTO);
            }
        }));
    }

    public Charges[] getChargesList() {
        if(charges.getValue() == null) return new Charges[0];
        return charges.getValue().getCharges();
    }
}
