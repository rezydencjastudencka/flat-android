package pl.rpieja.flat.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.rpieja.flat.dto.Charge;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.dto.CreateChargeDTO;
import pl.rpieja.flat.dto.CreateDTO;
import pl.rpieja.flat.dto.SessionCheckResponse;
import pl.rpieja.flat.dto.TransfersDTO;
import pl.rpieja.flat.dto.User;

public class FlatAPI {

    private OkHttpClient client;
    private Gson gson = new Gson();

    private static final String API_ADDRESS = "https://api.flat.memleak.pl/";
    private static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");
    private static final String SESSION_CHECK_URL = API_ADDRESS + "session/check";
    private static final String CREATE_SESSION_URL = API_ADDRESS + "session/create";
    private static final String GET_CHARGES_URL = API_ADDRESS + "charge/";
    private static final String GET_TRANSFERS_URL = API_ADDRESS + "transfer/";
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
        Gson gson = new GsonBuilder().setDateFormat("YYYY-MM-DD").create();
        SessionCheckResponse checkResponse = gson.fromJson(response.body().string(), SessionCheckResponse.class);
        if (checkResponse == null) return false;
        return "ok".equals(checkResponse.error);
    }

    public ChargesDTO fetchCharges(int month, int year) throws IOException, NoInternetConnectionException {
        String requestUrl = GET_CHARGES_URL + Integer.toString(year) + "/" + Integer.toString(month);
        return fetch(requestUrl, ChargesDTO.class);
    }

    public TransfersDTO fetchTransfers(int month, int year) throws IOException, NoInternetConnectionException {
        String requestUrl = GET_TRANSFERS_URL + Integer.toString(year) + "/" + Integer.toString(month);
        return fetch(requestUrl, TransfersDTO.class);
    }

    public List<User> fetchUsers() throws IOException, NoInternetConnectionException {
        return Arrays.asList(fetch(GET_USERS_URL, User[].class));
    }

    private <T> T createEntity(CreateDTO<T> entity, String entityUrl) throws IOException, NoInternetConnectionException {
        Response response = post(entityUrl, entity);
        return gson.fromJson(response.body().charStream(), entity.getEntityClass());
    }

    public Charge createCharge(CreateChargeDTO charge) throws IOException, NoInternetConnectionException {
        return createEntity(charge, CREATE_CHARGE_URL);
    }

    private <T> Response post(String url, T data) throws IOException, NoInternetConnectionException {
        return method("POST", url, data);
    }

    private <T> void put(String url, T data) throws IOException, NoInternetConnectionException {
        method("PUT", url, data);
    }

    private <T> Response method(String methodName, String url, T data) throws IOException, NoInternetConnectionException {
        String json = gson.toJson(data);
        Request request = new Request.Builder()
                .url(url)
                .method(methodName, RequestBody.create(JSON_MEDIA_TYPE, json))
                .build();

        Log.d(TAG, String.format("Sending %s %s with data %s", methodName, url, json));
        Response response = client.newCall(request).execute();
        if (response.code() == 403) throw new UnauthorizedException();
        if (!response.isSuccessful()) throw new NoInternetConnectionException();
        return response;
    }

    private <T> T fetch(String requestUrl, Class<T> tClass) throws IOException, NoInternetConnectionException {
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 403) throw new UnauthorizedException();
        if (!response.isSuccessful()) throw new NoInternetConnectionException();

        T entity = gson.fromJson(response.body().string(), tClass);
        if (entity == null) throw new JsonIOException("JSON parsing exception");
        return entity;
    }

}
