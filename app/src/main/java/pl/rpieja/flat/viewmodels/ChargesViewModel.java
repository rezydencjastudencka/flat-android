package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.ViewModel;

import pl.rpieja.flat.containers.APIChargesContainer;
import pl.rpieja.flat.dto.Charges;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.tasks.AsyncGetCharges;

/**
 * Created by radix on 14.11.17.
 */

public class ChargesViewModel extends ViewModel{

    private ChargesDTO charges;

    public void loadCharges(APIChargesContainer apiChargesContainer){
        new AsyncGetCharges().execute(new AsyncGetCharges.Params(apiChargesContainer, new AsyncGetCharges.Callable<ChargesDTO>() {
            @Override
            public void onCall(ChargesDTO chargesDTO) {
                charges=chargesDTO;
            }
        }));
    }
}
