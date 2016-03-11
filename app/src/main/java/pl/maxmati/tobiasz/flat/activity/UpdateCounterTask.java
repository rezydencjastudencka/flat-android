package pl.maxmati.tobiasz.flat.activity;

import android.util.Log;

import pl.maxmati.tobiasz.flat.api.APIConnector;
import pl.maxmati.tobiasz.flat.api.resource.ResourceManager;
import pl.maxmati.tobiasz.flat.api.resource.ResourceUpdate;
import pl.maxmati.tobiasz.flat.api.session.SessionException;
import pl.maxmati.tobiasz.flat.api.session.SessionManager;

/**
 * Created by mmos on 21.02.16.
 *
 * @author mmos
 */
public class UpdateCounterTask extends ProgressBarAsyncTask<Void, Void, Integer> {
    private static final String TAG = "UpdateCounterTask";

    private final String resourceName;
    private final int amount;

    private Exception exception;

    public UpdateCounterTask(ProgressBarActivity progressBarActivity, String resourceName, int amount) {
        super(progressBarActivity);
        this.resourceName = resourceName;
        this.amount = amount;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            Log.d(TAG, "Starting update of resource " + resourceName);
            ResourceUpdate resourceUpdate = new ResourceUpdate(amount);
            return ResourceManager.update(new APIConnector(progressBarActivity, SessionManager.restoreSession
                    (progressBarActivity)), resourceName, resourceUpdate);
        } catch (Exception e) {
            exception = e;
            Log.e(TAG, "Resource update failed: " + e.getMessage());
        }
        return null;
    }
}
