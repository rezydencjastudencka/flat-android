package pl.maxmati.tobiasz.mmos.bread.api.session;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.Session;
import pl.maxmati.tobiasz.mmos.bread.api.User;

/**
 * Created by mmos on 10.02.16.
 * @author mmos
 */
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static final String CREATE_URI = "http://api.flat.maxmati.pl:8888/session/create";

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

    public static Session create(String username, String password) throws AuthenticationException {
        try {
            HttpEntity<String> entity = APIConnector.buildHttpEntity(buildCreateSessionJson(username, password).toString(), null);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            Log.d(TAG, "Requesting new session");
            ResponseEntity<User> response = restTemplate.exchange(CREATE_URI, HttpMethod.POST, entity, User.class);

            return new Session(response.getBody(), getCookie(response.getHeaders()));
        } catch (JSONException e) {
            throw new AuthenticationException("Failed do create JSON request", e);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                Log.d(TAG, "User not found");
                throw new UserNotFoundException(e);
            } if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                Log.d(TAG, "Invalid password");
                throw new InvalidPasswordException(e);
            }

            throw new AuthenticationException("Got unexpected response during session creation", e);
        }
    }

    public static Session check(Session session) {
        Log.w(TAG, "check() is stub method");
        return session;
    }
}