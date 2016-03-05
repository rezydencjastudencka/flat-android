package pl.maxmati.tobiasz.flat.api;

import android.content.Context;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.flat.api.session.Session;
import pl.maxmati.tobiasz.flat.api.session.SessionException;
import pl.maxmati.tobiasz.flat.api.session.SessionManager;
import pl.maxmati.tobiasz.flat.api.session.SessionExpiredException;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class APIConnector {
    private static final String TAG = "APIConnector";

    private final String apiUri;
    private final Session session;

    public APIConnector(Context context, Session session) throws SessionException {
        this.session = session;
        this.apiUri = getAPIUri(context);
    }

    public <T> ResponseEntity<T> sendRequest(APIRequest request, Class<T> responseType) throws
            SessionException {
        final String fullUri;
        final HttpHeaders requestHeaders;
        final RestTemplate requestRestTemplate;

        try {
            if(!SessionManager.check(apiUri, session))
                throw new SessionExpiredException("Session expired");
        } catch (RestClientException e) {
            throw new SessionException("Failed to check session expire", e);
        }

        fullUri = apiUri + request.getRequestPath();
        requestHeaders = new HttpHeaders();
        SessionManager.addSessionCookieToHeader(requestHeaders, session);

        requestRestTemplate = new RestTemplate();
        requestRestTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        if(request.getCustomResponseErrorHandler() != null)
            requestRestTemplate.setErrorHandler(request.getCustomResponseErrorHandler());

        Log.d(TAG, "REST exchange: " + fullUri);
        try {
            return requestRestTemplate.exchange(fullUri, request.getMethod(), buildHttpEntity(request.getData(), requestHeaders), responseType);
        } catch (RestClientException e) {
            throw new SessionException("REST exchange failed: " + e.getMessage());
        }
    }

    public static HttpEntity<Object> buildHttpEntity(Object data, HttpHeaders customHeaders) {
        final HttpHeaders requestHeaders;

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

    public Session getSession() {
        return session;
    }

    public static String getAPIUri(Context context) {
        return context.getString(R.string.api_uri);
    }
}
