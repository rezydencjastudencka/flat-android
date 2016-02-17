package pl.maxmati.tobiasz.mmos.bread.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import pl.maxmati.tobiasz.mmos.bread.api.APIAuthActivity;
import pl.maxmati.tobiasz.mmos.bread.api.LoginTask;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;

/**
 * Created by mmos on 11.02.16.
 *
 * @author mmos
 */
public class BreadWidgetConfigure extends APIAuthActivity {
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected AsyncTask<Void, Void, Boolean> getLoginTask(final String username, final String password) {
        /**
         * Represents an asynchronous login/registration task used to authenticate
         * the user.
         */
        return new LoginTask(BreadWidgetConfigure.this, username, password) {
            @Override
            protected Boolean doInBackground(Void... voids) {
                if(super.doInBackground(voids)) {
                    startService(new Intent(BreadWidgetConfigure.this, BreadWidgetUpdater.class));
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

                setResult(RESULT_OK, resultValue);
                finish();
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

        // FIXME: this is not a solution, activity blinks
        if(SessionManager.hasSessionInStore(this)) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
