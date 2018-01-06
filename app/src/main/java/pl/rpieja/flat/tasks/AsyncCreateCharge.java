package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.api.NoInternetConnectionException;
import pl.rpieja.flat.api.UnauthorizedException;
import pl.rpieja.flat.dto.CreateChargeDTO;

/**
 * Created by maxmati on 11/28/17.
 */

public class AsyncCreateCharge extends AsyncTask<CreateChargeDTO, Void, Void> {
    private final FlatAPI flatAPI;
    private final Runnable onSuccess;
    private final Runnable unauthorizedHandler;

    public AsyncCreateCharge(FlatAPI flatAPI, Runnable onSuccess,
                             Runnable onUnauthorized) {
        this.flatAPI = flatAPI;
        this.onSuccess = onSuccess;
        this.unauthorizedHandler = onUnauthorized;
    }

    @Override
    protected Void doInBackground(CreateChargeDTO... charges) {
        try {
            flatAPI.createCharge(charges[0]);
        } catch (NoInternetConnectionException | IOException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e) {
            unauthorizedHandler.run();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void _) {
        onSuccess.run();
    }
}
