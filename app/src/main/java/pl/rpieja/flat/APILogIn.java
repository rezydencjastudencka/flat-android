package pl.rpieja.flat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
    URL url;
    InputStream stream=null;
    String result;
    HttpURLConnection session;
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
        }catch (Exception e){
            e.printStackTrace();
        }






//        Log.d("jhhk", "task");
//
//        System.setProperty("http.keepAlive", "false"); // must be set
//
//        try{
//            url=new URL(params[0]);
//            session=(HttpURLConnection) url.openConnection();
//            session.setDoOutput(true);
//
//            session.setRequestMethod("POST");
//            session.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            String json = "{\"name\":\""+params[1]+"\", \"password\": \""+params[2]+"\"}";
//
//            session.connect();
//
//            OutputStream os = session.getOutputStream();
//            os.write(json.getBytes("UTF-8"));
//            os.close();
//
//            //session.setRequestProperty("name", params[1]);
//            //session.setRequestProperty("password", params[2]);
//
//
//            int responseCode = session.getResponseCode();
//            if(responseCode!=HttpsURLConnection.HTTP_OK){
//                Log.d("HTTP ERROR CODE: ", Integer.toString(responseCode) );
//                throw new IOException("HTTP error code: " + responseCode);
//            }
//            stream=session.getInputStream();
//            result = getStringFromInputStream(stream);
//            Log.d("RESPONSE DATA: ", result);
//
//            session.disconnect();
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return null;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
