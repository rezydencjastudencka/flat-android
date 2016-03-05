package pl.maxmati.tobiasz.flat.api;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class APIRequest {
    private final HttpMethod method;
    private final String requestPath;
    private final Object data;
    private final ResponseErrorHandler customResponseErrorHandler;

    public APIRequest(HttpMethod method, String requestPath, Object data, ResponseErrorHandler
            customResponseErrorHandler) {
        this.method = method;
        this.requestPath = requestPath;
        this.data = data;
        this.customResponseErrorHandler = customResponseErrorHandler;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Object getData() {
        return data;
    }

    public ResponseErrorHandler getCustomResponseErrorHandler() {
        return customResponseErrorHandler;
    }
}
