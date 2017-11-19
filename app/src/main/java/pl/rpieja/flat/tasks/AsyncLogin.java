package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import pl.rpieja.flat.api.FlatAPI;

/**
 * Created by radix on 29.10.17.
 */

public class AsyncLogin extends AsyncTask<AsyncLogin.Params, Void, Boolean> {
    private AsyncLogin.Params params;

    public static void run(FlatAPI api, String username, String password, Callable<Boolean> callable) {
        new AsyncLogin().execute(new AsyncLogin.Params(api, username, password, callable));
    }

    @Override
    protected Boolean doInBackground(AsyncLogin.Params... flatAPIS) {
        params = flatAPIS[0];
        try {
            return params.flatAPI.login(params.username, params.password);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        params.callback.onCall(result);
    }

    static class Params {
        FlatAPI flatAPI;
        String username, password;
        Callable<Boolean> callback;

        Params(FlatAPI flatAPI, String username, String password, Callable<Boolean> callback) {
            this.flatAPI = flatAPI;
            this.username = username;
            this.password = password;
            this.callback = callback;
        }
    }

    public interface Callable<Param> {
        void onCall(Param param);
    }
}
