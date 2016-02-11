package pl.maxmati.tobiasz.mmos.bread.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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

    private static HttpEntity<String> buildHttpEntityFromJson(JSONObject data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data.toString());
    }

    private static String getCookie(HttpHeaders headers) {
        return headers.get("Set-cookie").get(0).split(";")[0];
    }

    public static Session create(String username, String password) throws SessionException {
        try {
            HttpEntity<String> entity = buildHttpEntityFromJson(buildCreateSessionJson(username, password));
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Log.d(TAG, "Requesting new session");
            ResponseEntity<User> response = restTemplate.exchange(CREATE_URI, HttpMethod.POST, entity, User.class);

            return new Session(response.getBody(), getCookie(response.getHeaders()));
        } catch (JSONException e) {
            throw new SessionException("Failed do create JSON request", e);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new UserNotFoundException(e);
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN))
                throw new InvalidPasswordException(e);

            throw new SessionException("Got unexpected response during session creation", e);
        }
    }
}