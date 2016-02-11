package pl.maxmati.tobiasz.mmos.bread.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class BreadManager {
    public static int get(APIConnector apiConnector) {
        ResponseEntity<BreadStatus> response = apiConnector.sendRequest(new APIRequest(HttpMethod.GET, "bread", null, null), BreadStatus.class);
        return response.getBody().getBreadCount();
    }
}
