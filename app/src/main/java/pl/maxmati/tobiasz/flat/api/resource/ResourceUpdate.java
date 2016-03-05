package pl.maxmati.tobiasz.flat.api.resource;

/**
 * Created by mmos on 13.02.16.
 *
 * @author mmos
 */
public class ResourceUpdate {
    private final int amount;

    public ResourceUpdate(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
