package pl.maxmati.tobiasz.mmos.bread.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.mmos.bread.api.APILoginActivity;
import pl.maxmati.tobiasz.mmos.bread.api.Session;
import pl.maxmati.tobiasz.mmos.bread.api.session.AuthenticationException;
import pl.maxmati.tobiasz.mmos.bread.api.session.InvalidPasswordException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;
import pl.maxmati.tobiasz.mmos.bread.api.session.UserNotFoundException;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class BreadWidgetConfigure extends APILoginActivity {
    private static final String TAG = "BreadWidgetConfigure";

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected AsyncTask<Void, Void, Boolean> getLoginTask(final String username, final String password) {
        /**
         * Represents an asynchronous login/registration task used to authenticate
         * the user.
         */
        return new AsyncTask<Void, Void, Boolean>() {
            SessionException sessionException;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Session session = SessionManager.create(username, password);
                    SessionManager.storeSession(getApplicationContext(), session);
                    startService(new Intent(BreadWidgetConfigure.this, BreadWidgetUpdater.class));
                } catch (SessionException e) {
                    sessionException = e;
                    return null;
                }
                return true;
            }

            @Override
            protected void onPostExecute(final Boolean result) {
                mAuthTask = null;
                showProgress(false);

                if (result != null) {
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

                    setResult(RESULT_OK, resultValue);
                    finish();
                } else {
                    if(sessionException instanceof AuthenticationException) {
                        if(sessionException.getCause() instanceof UserNotFoundException) {
                            mUsernameView.setError(getString(R.string.error_invalid_username));
                            mUsernameView.requestFocus();
                        } else if(sessionException.getCause() instanceof InvalidPasswordException) {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                        } else {
                            Log.e(TAG, "Session creation failed: " + sessionException.getMessage(),
                                    sessionException);
                        }
                    }
                }
            }

            @Override
            protected void onCancelled() {
                mAuthTask = null;
                showProgress(false);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras;

        extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        super.onCreate(savedInstanceState);

        if(SessionManager.hasSessionInStore(this)) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
