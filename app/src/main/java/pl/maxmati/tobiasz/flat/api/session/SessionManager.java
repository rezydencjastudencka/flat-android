package pl.maxmati.tobiasz.flat.api.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.maxmati.tobiasz.flat.api.APIConnector;
import pl.maxmati.tobiasz.flat.api.RestException;
import pl.maxmati.tobiasz.flat.api.user.UserRecord;

/**
 * Created by mmos on 10.02.16.
 * @author mmos
 */
public class SessionManager {
    private static final String TAG = "SessionManager";

    private static final String SESSION_PATH = "session";

    private static final String SESSION_CREATE_ACTION = SESSION_PATH + "/create";
    private static final String SESSION_CHECK_ACTION = SESSION_PATH + "/check";

    private static final String STORE_NAME = "SessionStore";
    private static final String STORE_FIELD_NAME_SESSION_COOKIE = "sessionCookie";

    private SessionManager() {
    }

    private static JSONObject buildCreateSessionJson(String username, String password) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("name", username);
        body.put("password", password);
        return body;
    }

    private static String getCookie(HttpHeaders headers) {
        return headers.get("Set-cookie").get(0).split(";")[0];
    }

    public static Session create(String apiUri, String username, String password)
            throws SessionException, RestException {
        try {
            HttpEntity<Object> entity = APIConnector.buildHttpEntity(buildCreateSessionJson(username,
                    password).toString(), null);
            RestTemplate restTemplate = new RestTemplate();
            Session session;

            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            Log.d(TAG, "Requesting new session");
            ResponseEntity<UserRecord> response = restTemplate.exchange(apiUri +
                    SESSION_CREATE_ACTION,
                    HttpMethod.POST, entity, UserRecord.class);

            session = new Session(getCookie(response.getHeaders()));

            return session;
        } catch (JSONException e) {
            throw new AuthenticationException("Failed do create JSON request", e);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                Log.d(TAG, "User not found");
                throw new AuthenticationException(new UserNotFoundException(e));
            } if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                Log.d(TAG, "Invalid password");
                throw new AuthenticationException(new InvalidPasswordException(e));
            }
            throw new AuthenticationException("Got unexpected response during session creation", e);
        } catch (RestClientException e) {
            throw new RestException(e);
        }
    }

    public static boolean check(String apiUri, Session session) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity;

        addSessionCookieToHeader(httpHeaders, session);
        httpEntity = new HttpEntity<>(httpHeaders);

        try {
            restTemplate.exchange(apiUri + SESSION_CHECK_ACTION, HttpMethod.GET,
                    httpEntity, String.class, "");
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                return false;

            Log.w(TAG, "Unhandled response from server during session check");
            throw e;
        }
        return true;
    }

    public static void addSessionCookieToHeader(HttpHeaders headers, Session session) {
        headers.add("Cookie", session.getSessionCookie());
    }

    private static SharedPreferences getStore(Context context) {
        return context.getApplicationContext().getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
    }

    public static void storeSession(Context context, Session session) {
        SharedPreferences.Editor mPreferencesEditor = getStore(context).edit();
        mPreferencesEditor.putString(STORE_FIELD_NAME_SESSION_COOKIE, session.getSessionCookie());
        mPreferencesEditor.apply();
    }

    public static Session restoreSession(Context context) throws SessionException {
        return new Session(getStore(context).getString(STORE_FIELD_NAME_SESSION_COOKIE, null));
    }

    public static void clearStore(Context context) {
        getStore(context).edit().clear().apply();
        Log.d(TAG, "Session store cleared");
    }

    public static boolean hasSessionInStore(Context context) {
        SharedPreferences store = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        return store.contains(STORE_FIELD_NAME_SESSION_COOKIE);
    }
}