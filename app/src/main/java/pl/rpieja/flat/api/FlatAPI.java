package pl.rpieja.flat.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.dto.CreateCharge;
import pl.rpieja.flat.dto.SessionCheckResponse;
import pl.rpieja.flat.dto.User;

/**
 * Created by radix on 10/29/17.
 */

public class FlatAPI {

    private OkHttpClient client;
    private Gson gson = new Gson();

    private static final String API_ADDRESS = "https://api.flat.memleak.pl/";
    private static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");
    private static final String SESSION_CHECK_URL = API_ADDRESS + "session/check";
    private static final String CREATE_SESSION_URL = API_ADDRESS + "session/create";
    private static final String GET_CHARGES_URL = API_ADDRESS + "charge/";
    private static final String CREATE_CHARGE_URL = API_ADDRESS + "charge/create";
    private static final String GET_USERS_URL = API_ADDRESS + "user/";

    private static final String TAG = FlatAPI.class.getSimpleName();

    public FlatAPI(CookieJar cookieJar) {
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    public Boolean login(String username, String password) throws IOException {
        //TODO: use Gson
        String json = "{\"name\":\"" + username + "\", \"password\": \"" + password + "\"}";

        Request request = new Request.Builder()
                .url(CREATE_SESSION_URL)
                .post(RequestBody.create(JSON_MEDIA_TYPE, json))
                .build();
        Response response = client.newCall(request).execute();
        return response.isSuccessful();
    }

    public Boolean validateSession() throws IOException {
        Request request = new Request.Builder()
                .url(SESSION_CHECK_URL)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) return false;
        Gson gson = new Gson();
        SessionCheckResponse checkResponse = gson.fromJson(response.body().string(), SessionCheckResponse.class);
        if (checkResponse == null) return false;
        return "ok".equals(checkResponse.error);
    }

    public ChargesDTO getCharges(int month, int year) throws IOException, NoInternetConnectionException {
        String requestUrl = GET_CHARGES_URL + Integer.toString(year) + "/" + Integer.toString(month);
        return fetch(requestUrl, ChargesDTO.class);
    }

    public List<User> getUsers() throws IOException, NoInternetConnectionException {
        return Arrays.asList(fetch(GET_USERS_URL, User[].class));
    }

    public void createCharge(CreateCharge charge) throws IOException, NoInternetConnectionException {
        put(CREATE_CHARGE_URL, charge);
    }

    private <T> void post(String url, T data) throws IOException, NoInternetConnectionException {
        method("POST", url, data);
    }
    private <T> void put(String url, T data) throws IOException, NoInternetConnectionException {
        method("PUT", url, data);
    }
    private <T> void method(String methodName, String url, T data) throws IOException, NoInternetConnectionException {
        String json = gson.toJson(data);
        Request request = new Request.Builder()
                .url(url)
                .method(methodName, RequestBody.create(JSON_MEDIA_TYPE, json))
                .build();

        Log.d(TAG, String.format("Sending %s %s with data %s", methodName, url, json));
        Response response = client.newCall(request).execute();
        if (response.code() == 403) throw new UnauthorizedException();
        if (!response.isSuccessful()) throw new NoInternetConnectionException();
    }

    private <T> T fetch(String requestUrl, Class<T> tClass) throws IOException, NoInternetConnectionException {
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 403) throw new UnauthorizedException();
        if (!response.isSuccessful()) throw new NoInternetConnectionException();

        T users = gson.fromJson(response.body().string(), tClass);
        if (users == null) throw new JsonIOException("JSON parsing exception");
        return users;
    }

}
