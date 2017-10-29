package pl.rpieja.flat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by radix on 10/26/17.
 */

public class APILogIn extends AsyncTask<String, String, String> {
    private ClearableCookieJar cookieJar;
    private OkHttpClient client;

    public APILogIn(Context context) {
        cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(context));
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected String doInBackground(String... params) {

        try {
            String json = "{\"name\":\"" + params[1] + "\", \"password\": \"" + params[2] + "\"}";

            Request request = new Request.Builder()
                    .url(params[0])
                    .post(RequestBody.create(JSON, json))
                    .build();
            Response response = client.newCall(request).execute();
            response.isSuccessful();
            Log.d("RESPONSE", response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
