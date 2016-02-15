package pl.maxmati.tobiasz.mmos.bread.api;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class APIConnector {
    private static final String TAG = "APIConnector";
    public static final String API_URI = "http://api.flat.maxmati.pl:8888/";

    private Session session;

    public APIConnector(Session session) throws SessionException {
        this.session = session;
    }

    public <T> ResponseEntity<T> sendRequest(APIRequest request, Class<T> responseType) {
        String fullUri = API_URI + request.getRequestPath();
        HttpHeaders requestHeaders = null;
        RestTemplate requestRestTemplate;

        if(session != null) {
            requestHeaders = new HttpHeaders();
            session = SessionManager.check(session);
            addSessionCookie(requestHeaders, session);
        }

        requestRestTemplate = new RestTemplate();
        requestRestTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        if(request.getCustomResponseErrorHandler() != null)
            requestRestTemplate.setErrorHandler(request.getCustomResponseErrorHandler());

        Log.d(TAG, "REST exchange: " + fullUri + "; " + request.getData());
        return requestRestTemplate.exchange(fullUri, request.getMethod(), buildHttpEntity(request.getData(), requestHeaders), responseType);
    }

    public static <T> HttpEntity<T> buildHttpEntity(T data, HttpHeaders customHeaders) {
        HttpHeaders requestHeaders;

        if(customHeaders != null) {
            requestHeaders = customHeaders;
        } else {
            requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        }

        if(data == null)
            return new HttpEntity<>(requestHeaders);
        else
            return new HttpEntity<>(data, requestHeaders);
    }

    private void addSessionCookie(HttpHeaders headers, Session session) {
        if(session == null)
            return;
        headers.add("Cookie", session.getSessionCookie());
    }

    public Session getSession() {
        return session;
    }
}
