package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.api.NoInternetConnectionException;
import pl.rpieja.flat.dto.ChargesDTO;

/**
 * Created by radix on 2017-11-01.
 */

public class AsyncGetCharges extends AsyncTask<AsyncGetCharges.Params, Void, ChargesDTO> {
    private AsyncGetCharges.Params params;

    public static void run(FlatAPI api, int month, int year, Callable<ChargesDTO> callback){
        new AsyncGetCharges().execute(new AsyncGetCharges.Params(api, month, year, callback));
    }

    @Override
    protected ChargesDTO doInBackground(AsyncGetCharges.Params... chargesDTO) {
        params = chargesDTO[0];
        try {
            return params.flatAPI.getCharges(params.month, params.year);
        } catch (NoInternetConnectionException | IOException e) {
            e.printStackTrace();
        }
        return new ChargesDTO();
    }

    @Override
    protected void onPostExecute(ChargesDTO charges) {
        super.onPostExecute(charges);
        params.callback.onCall(charges);
    }

    static class Params {
        final FlatAPI flatAPI;
        final int month, year;
        final Callable<ChargesDTO> callback;

        Params(FlatAPI flatAPI, int month, int year, Callable<ChargesDTO> callback) {
            this.flatAPI = flatAPI;
            this.month = month;
            this.year = year;
            this.callback = callback;
        }
    }

    public interface Callable<Param> {
        void onCall(Param param);
    }
}
