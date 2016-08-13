package pl.maxmati.tobiasz.mmos.bread.activity;

import android.util.Log;

import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.resource.ResourceManager;
import pl.maxmati.tobiasz.mmos.bread.api.resource.ResourceUpdate;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;

/**
 * Created by mmos on 21.02.16.
 *
 * @author mmos
 */
public class UpdateCounterTask extends ProgressBarAsyncTask<Void, Void, Integer> {
    private static final String TAG = "UpdateCounterTask";

    private final String resourceName;
    private final int amount;

    private SessionException sessionException;

    public UpdateCounterTask(ProgressBarActivity progressBarActivity, String resourceName, int amount) {
        super(progressBarActivity);
        this.resourceName = resourceName;
        this.amount = amount;
    }

    public SessionException getSessionException() {
        return sessionException;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            Log.d(TAG, "Starting update of resource " + resourceName);
            ResourceUpdate resourceUpdate = new ResourceUpdate(amount);
            return ResourceManager.update(new APIConnector(progressBarActivity, SessionManager.restoreSession
                    (progressBarActivity)), resourceName, resourceUpdate);
        } catch (SessionException e) {
            sessionException = e;
            Log.e(TAG, "Resource update failed: " + e.getMessage());
        }
        return null;
    }
}
