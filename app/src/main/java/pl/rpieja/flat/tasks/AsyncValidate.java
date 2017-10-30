package pl.rpieja.flat.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import pl.rpieja.flat.api.FlatAPI;

/**
 * Created by radix on 29.10.17.
 */

public class AsyncValidate extends AsyncTask<AsyncValidate.Params, Void, Boolean>{
    AsyncValidate.Params params;
    @Override
    protected Boolean doInBackground(AsyncValidate.Params... flatAPIS) {
        params = flatAPIS[0];
        try{
            return params.api.validateSession();
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
        public FlatAPI api;
        public Callable<Boolean> callback;

        public Params(FlatAPI api, Callable<Boolean> callback) {
            this.api = api;
            this.callback = callback;
        }
    }

    public interface Callable<Param>{
        void onCall(Param param);
    }
}
