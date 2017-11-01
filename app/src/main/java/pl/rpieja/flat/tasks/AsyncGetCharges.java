package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import pl.rpieja.flat.containers.APIChargesContainer;
import pl.rpieja.flat.api.NoInternetConnectionException;
import pl.rpieja.flat.dto.ChargesDTO;

/**
 * Created by radix on 2017-11-01.
 */

public class AsyncGetCharges extends AsyncTask <AsyncGetCharges.Params, Void, ChargesDTO> {
    AsyncGetCharges.Params params;

    @Override
    protected ChargesDTO doInBackground(AsyncGetCharges.Params... chargesDTO) {
        params=chargesDTO[0];
        try{
            return params.api.getFlatAPI().getCharges(params.api.getMonth(), params.api.getYear());
        } catch (NoInternetConnectionException | IOException e) {
            e.printStackTrace();
        }
        return new ChargesDTO();
    }

    @Override
    protected void onPostExecute(ChargesDTO charges){
        super.onPostExecute(charges);
        params.callback.onCall(charges);
    }

    public static class Params{
        public APIChargesContainer api;
        public Callable<ChargesDTO> callback;

        public Params(APIChargesContainer api, Callable<ChargesDTO> callback){
            this.api=api;
            this.callback=callback;
        }
    }

    public interface Callable<Param>{
        void onCall(Param param);
    }
}
