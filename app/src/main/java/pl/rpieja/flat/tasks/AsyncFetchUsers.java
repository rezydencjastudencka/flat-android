package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.api.NoInternetConnectionException;
import pl.rpieja.flat.api.UnauthorizedException;
import pl.rpieja.flat.dto.User;

/**
 * Created by maxmati on 11/28/17.
 */

public class AsyncFetchUsers extends AsyncTask<Void, Void, List<User>> {
    private final FlatAPI flatAPI;
    private final Callable<List<User>> onSuccess;
    private final Runnable unauthorizedHandler;

    public AsyncFetchUsers(FlatAPI flatAPI, Callable<List<User>> onSuccess,
                           Runnable onUnauthorized) {
        this.flatAPI = flatAPI;
        this.onSuccess = onSuccess;
        this.unauthorizedHandler = onUnauthorized;
    }

    @Override
    protected List<User> doInBackground(Void... voids) {
        try {
            return flatAPI.getUsers();
        } catch (NoInternetConnectionException | IOException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e) {
            unauthorizedHandler.run();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<User> users) {
        super.onPostExecute(users);
        onSuccess.onCall(users);
    }

    public interface Callable<Param> {
        void onCall(Param param);
    }
}
