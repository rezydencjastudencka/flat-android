package pl.maxmati.tobiasz.flat.api.resource;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import pl.maxmati.tobiasz.flat.api.APIConnector;
import pl.maxmati.tobiasz.flat.api.APIRequest;
import pl.maxmati.tobiasz.flat.api.session.SessionException;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class ResourceManager {
    private static final String RESOURCE_PATH = "counter/";
    private static final String RESOURCE_UPDATE_ACTION = "/modify";

    public static int get(APIConnector apiConnector, String resourceName) throws SessionException {
        ResponseEntity<ResourceStatus> response = apiConnector.sendRequest(new APIRequest
                (HttpMethod.GET, RESOURCE_PATH + resourceName, null, null), ResourceStatus.class);
        return response.getBody().getCount();
    }

    public static int update(APIConnector apiConnector, String resourceName, ResourceUpdate resourceUpdate)
            throws SessionException {
        ResponseEntity<ResourceStatus> response = apiConnector.sendRequest(new APIRequest
                (HttpMethod.POST, RESOURCE_PATH + resourceName + RESOURCE_UPDATE_ACTION, resourceUpdate , null), ResourceStatus.class);
        return response.getBody().getCount();
    }
}
