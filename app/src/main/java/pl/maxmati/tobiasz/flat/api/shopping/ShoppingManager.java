package pl.maxmati.tobiasz.flat.api.shopping;

import org.springframework.http.HttpMethod;

import pl.maxmati.tobiasz.flat.api.APIConnector;
import pl.maxmati.tobiasz.flat.api.APIRequest;
import pl.maxmati.tobiasz.flat.api.session.SessionException;

/**
 * Created by mmos on 05.03.16.
 *
 * @author mmos
 */
public class ShoppingManager {
    private static final String SHOPPING_PATH = "shopping";

    private ShoppingManager() {
    }

    public static PendingOrder[] get(APIConnector apiConnector) throws SessionException {
        return apiConnector.sendRequest(new APIRequest(HttpMethod.GET, SHOPPING_PATH, null, null),
                PendingOrder[].class).getBody();
    }
}
