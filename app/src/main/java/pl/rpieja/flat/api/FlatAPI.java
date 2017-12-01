package pl.rpieja.flat.api;

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
import pl.rpieja.flat.dto.SessionCheckResponse;
import pl.rpieja.flat.dto.User;

/**
 * Created by radix on 10/29/17.
 */

public class FlatAPI {

    private OkHttpClient client;

    private static final String apiAdress = "https://api.flat.memleak.pl/";
    private static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");
    private static final String sessionCheckUrl = apiAdress + "session/check";
    private static final String createSession = apiAdress + "session/create";
    private static final String getCharges = apiAdress + "charge/";
    private static final String getUsers = apiAdress + "user/";

    public FlatAPI(CookieJar cookieJar) {
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    public Boolean login(String username, String password) throws IOException {
        //TODO: use Gson
        String json = "{\"name\":\"" + username + "\", \"password\": \"" + password + "\"}";

        Request request = new Request.Builder()
                .url(createSession)
                .post(RequestBody.create(JSON_MEDIA_TYPE, json))
                .build();
        Response response = client.newCall(request).execute();
        return response.isSuccessful();
    }

    public Boolean validateSession() throws IOException {
        Request request = new Request.Builder()
                .url(sessionCheckUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) return false;
        Gson gson = new Gson();
        SessionCheckResponse checkResponse = gson.fromJson(response.body().string(), SessionCheckResponse.class);
        if (checkResponse == null) return false;
        return "ok".equals(checkResponse.error);
    }

    public ChargesDTO getCharges(int month, int year) throws IOException, NoInternetConnectionException {
        String requestUrl = getCharges + Integer.toString(year) + "/" + Integer.toString(month);
        return fetch(requestUrl, ChargesDTO.class);
    }

    public List<User> getUsers() throws IOException, NoInternetConnectionException {
        return Arrays.asList(fetch(getUsers, User[].class));
    }


    private <T> T fetch(String requestUrl, Class<T> tClass) throws IOException, NoInternetConnectionException {
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Response response = client.newCall(request).execute();

        if (response.code() == 403) throw new UnauthorizedException();
        if (!response.isSuccessful()) throw new NoInternetConnectionException();


        Gson gson = new Gson();
        T users = gson.fromJson(response.body().string(), tClass);
        if (users == null) throw new JsonIOException("JSON parsing exception");
        return users;
    }
}
