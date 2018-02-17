package pl.maxmati.tobiasz.mmos.bread.api.charge;

import org.springframework.http.HttpMethod;

import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.APIRequest;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;

/**
 * Created by mmos on 21.02.16.
 *
 * @author mmos
 */
public class ChargeManager {
    private static final String CHARGE_PATH = "charge";
    private static final String CHARGE_CREATE_ACTION = "/create";

    private ChargeManager() {}

    public static void create(APIConnector apiConnector, Charge charge) throws SessionException {
        apiConnector.sendRequest(new APIRequest(HttpMethod.PUT, CHARGE_PATH +
                CHARGE_CREATE_ACTION, charge, null), Void.class);
    }
}
