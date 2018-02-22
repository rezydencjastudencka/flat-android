package pl.maxmati.tobiasz.mmos.bread.api.resource;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class ResourceStatus {
    private int count;

    public ResourceStatus(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
