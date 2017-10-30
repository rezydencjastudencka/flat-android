package pl.rpieja.flat.tasks;

import android.os.AsyncTask;
import java.io.IOException;

import pl.rpieja.flat.api.APIContainer;
import pl.rpieja.flat.api.FlatAPI;

/**
 * Created by radix on 29.10.17.
 */

public class AsyncLogin extends AsyncTask<AsyncLogin.Params, Void, Boolean>{
    AsyncLogin.Params params;
    @Override
    protected Boolean doInBackground(AsyncLogin.Params... flatAPIS) {
        params = flatAPIS[0];
        try{
            return params.api.getFlatAPI().login(params.api.getUsername(), params.api.getPassword());
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

    public static class Params {
        public APIContainer api;
        public Callable<Boolean> callback;

        public Params(APIContainer api, Callable<Boolean> callback) {
            this.api = api;
            this.callback = callback;
        }
    }

    public interface Callable<Param>{
        void onCall(Param param);
    }
}
