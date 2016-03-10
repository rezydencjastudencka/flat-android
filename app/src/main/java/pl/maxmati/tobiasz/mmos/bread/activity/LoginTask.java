package pl.maxmati.tobiasz.mmos.bread.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.RestException;
import pl.maxmati.tobiasz.mmos.bread.api.session.AuthenticationException;
import pl.maxmati.tobiasz.mmos.bread.api.session.InvalidPasswordException;
import pl.maxmati.tobiasz.mmos.bread.api.session.Session;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;
import pl.maxmati.tobiasz.mmos.bread.api.session.UserNotFoundException;

/**
 * Created by mmos on 15.02.16.
 *
 * @author mmos
 *
 */

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "LoginTask";

    private final APIAuthActivity apiAuthActivity;
    private final String username;
    private final String password;

    protected Exception exception;
    protected Session session;

    public LoginTask(APIAuthActivity apiAuthActivity, String username, String password) {
        this.apiAuthActivity = apiAuthActivity;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            session = SessionManager.create(APIConnector.getAPIUri(apiAuthActivity),
                    username, password);
            SessionManager.storeSession(apiAuthActivity.getApplicationContext(), session);
        } catch (Exception e) {
            exception = e;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        onCancelled();

        if(success) {
            apiAuthActivity.setResult(Activity.RESULT_OK);
            apiAuthActivity.finish();

            if(apiAuthActivity.getIntent().hasExtra(APIAuthActivity.EXTRA_AUTH_NOTIFICATION_ID))
                removeAuthNotification(apiAuthActivity.getIntent().getIntExtra(APIAuthActivity
                        .EXTRA_AUTH_NOTIFICATION_ID, 0));
            if(apiAuthActivity.getIntent().hasExtra(APIAuthActivity.EXTRA_AUTH_SERVICE_INTENT))
                apiAuthActivity.startService(apiAuthActivity.getIntent().<Intent>getParcelableExtra(
                        APIAuthActivity.EXTRA_AUTH_SERVICE_INTENT));
        } else {
            if (exception instanceof AuthenticationException) {
                if (exception.getCause() instanceof UserNotFoundException) {
                    apiAuthActivity.mUsernameView.setError(apiAuthActivity.getString(R.string
                            .error_invalid_username));
                    apiAuthActivity.mUsernameView.requestFocus();
                } else if (exception.getCause() instanceof InvalidPasswordException) {
                    apiAuthActivity.mPasswordView.setError(apiAuthActivity.getString(R.string
                            .error_incorrect_password));
                    apiAuthActivity.mPasswordView.requestFocus();
                } else {
                    Log.e(TAG, "Session creation failed: " + exception
                            .getMessage(), exception);
                }
            } else if(exception instanceof RestException) {
                new AlertDialog.Builder(apiAuthActivity)
                        .setTitle("Network error")
                        .setMessage(exception.getLocalizedMessage())
                        .setNeutralButton("OK", null)
                        .create()
                        .show();
            }
        }
    }

    @Override
    protected void onCancelled() {
        apiAuthActivity.mAuthTask = null;
        apiAuthActivity.showProgress(false);
    }

    private void removeAuthNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) apiAuthActivity.getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
