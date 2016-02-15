package pl.maxmati.tobiasz.mmos.bread.api.session;

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
import org.springframework.web.client.RestTemplate;

import pl.maxmati.tobiasz.mmos.bread.User;
import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.Session;

/**
 * Created by mmos on 10.02.16.
 * @author mmos
 */
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static final String CREATE_URI = "session/create";

    private static final String STORE_NAME = "SessionStore";
    private static final String STORE_FIELD_NAME_USERNAME = "username";
    private static final String STORE_FIELD_NAME_PASSWORD = "password";
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

    public static Session create(String username, String password) throws SessionException {
        try {
            HttpEntity<String> entity = APIConnector.buildHttpEntity(buildCreateSessionJson(username, password).toString(), null);
            RestTemplate restTemplate = new RestTemplate();
            Session session;

            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            Log.d(TAG, "Requesting new session");
            ResponseEntity<UserRecord> response = restTemplate.exchange(APIConnector.API_URI + CREATE_URI, HttpMethod.POST, entity, UserRecord.class);

            session = new Session(new User(username, password), getCookie(response.getHeaders()));

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
        }
    }

    public static Session check(Session session) { // TODO: implement
        Log.w(TAG, "check() is stub method");
        //updateStoredCookie();
        return session;
    }

    public static void storeSession(Context context, Session session) {
        SharedPreferences.Editor mPreferencesEditor = context.getSharedPreferences(STORE_NAME,
                Context.MODE_PRIVATE).edit();
        mPreferencesEditor.putString(STORE_FIELD_NAME_USERNAME, session.getUser().getName());
        mPreferencesEditor.putString(STORE_FIELD_NAME_PASSWORD, session.getUser().getPassword());
        mPreferencesEditor.putString(STORE_FIELD_NAME_SESSION_COOKIE, session.getSessionCookie());
        mPreferencesEditor.apply();
    }

    public static Session restoreSession(Context context) throws SessionException {
        SharedPreferences store = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        try {
            return new Session(new User(store.getString(STORE_FIELD_NAME_USERNAME, null),
                    store.getString(STORE_FIELD_NAME_PASSWORD, null)), store.getString
                    (STORE_FIELD_NAME_SESSION_COOKIE, null));
        } catch (InvalidCredentialsException e) {
            throw new SessionException("No valid session in store", e);
        }
    }

    public static boolean hasSessionInStore(Context context) {
        SharedPreferences store = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        return store.contains(STORE_FIELD_NAME_USERNAME) && store.contains
                (STORE_FIELD_NAME_PASSWORD);
    }
}