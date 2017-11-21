package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.api.NoInternetConnectionException;
import pl.rpieja.flat.api.UnauthorizedException;
import pl.rpieja.flat.dto.ChargesDTO;

/**
 * Created by radix on 2017-11-01.
 */

public class AsyncGetCharges extends AsyncTask<Void, Void, ChargesDTO> {
    private final FlatAPI flatAPI;
    private final int month, year;
    private final Callable<ChargesDTO> onSuccess;
    private final Runnable onCancel;

    public AsyncGetCharges(FlatAPI flatAPI, int month, int year, Callable<ChargesDTO> onSuccess, Runnable onUnauthorized) {
        this.flatAPI = flatAPI;
        this.month = month;
        this.year = year;
        this.onSuccess = onSuccess;
        this.onCancel = onUnauthorized;
    }

    @Override
    protected ChargesDTO doInBackground(Void... voids) {
        try {
            return flatAPI.getCharges(month, year);
        } catch (NoInternetConnectionException | IOException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e){
            onCancel.run();
        }
        return new ChargesDTO();
    }

    @Override
    protected void onPostExecute(ChargesDTO charges) {
        super.onPostExecute(charges);
        onSuccess.onCall(charges);
    }

    public interface Callable<Param> {
        void onCall(Param param);
    }
}
