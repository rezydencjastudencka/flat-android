package pl.maxmati.tobiasz.mmos.bread.api.resource;

import org.json.JSONObject;

/**
 * Created by mmos on 13.02.16.
 *
 * @author mmos
 */
public class ResourceUpdate extends JSONObject {
    private final int amount;

    public ResourceUpdate(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
