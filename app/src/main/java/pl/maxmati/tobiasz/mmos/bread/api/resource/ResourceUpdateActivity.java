package pl.maxmati.tobiasz.mmos.bread.api.resource;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

import org.springframework.web.client.HttpClientErrorException;

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.mmos.bread.api.APIAuthActivity;
import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionExpiredException;
import pl.maxmati.tobiasz.mmos.bread.widget.BreadWidgetUpdater;

public class ResourceUpdateActivity extends Activity {
    public static final String RESOURCE_FIELD_NAME = "resourceName";

    private static final String TAG = "ResourceUpdateActivity";

    private String resourceName;

    protected View mUpdateFormView;
    protected View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating activity");

        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        resourceName = getIntent().getStringExtra(RESOURCE_FIELD_NAME);
        // FIXME: it should be checked before activity creation
        if(resourceName == null || resourceName.isEmpty()) {
            Log.d(TAG, "No resource name supplied, canceling activity");
            finish();
            return;
        }

        setContentView(R.layout.activity_resource_update);

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.count_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateResourceCount().execute();
            }
        });

        mUpdateFormView = findViewById(R.id.update_form);
        mProgressView = findViewById(R.id.update_progress);
    }

    private AsyncTask<Void, Void, Integer> updateResourceCount() {
        return new AsyncTask<Void, Void, Integer>() {
            private int changeCount;
            private boolean sessionExpired;

            @Override
            protected void onPreExecute() {
                changeCount = ((NumberPicker) findViewById(R.id.count_picker)).getValue();
                if(((Switch) findViewById(R.id.decrease_switch)).isChecked())
                    changeCount = -changeCount;
                sessionExpired = false;
                showProgress(true);
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    APIConnector apiConnector = new APIConnector(SessionManager.restoreSession
                            (ResourceUpdateActivity.this));
                    int updatedCount = ResourceManager.update(apiConnector, resourceName, new
                            ResourceUpdate(changeCount));
                    Intent updaterIntent = new Intent(ResourceUpdateActivity.this,
                            BreadWidgetUpdater.class);
                    updaterIntent.putExtra(BreadWidgetUpdater.EXTRA_RESOURCE_COUNT, updatedCount);
                    startService(updaterIntent);

                    setResult(RESULT_OK);
                    finish();
                } catch (SessionExpiredException e) {
                    sessionExpired = true;
                    Log.d(TAG, "Session expired, bringing authentication activity");
                } catch (SessionException |
                        HttpClientErrorException e) {
                    Log.e(TAG, "Resource count update failed: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if(!sessionExpired)
                    return;

                startActivityForResult(new Intent(ResourceUpdateActivity.this, APIAuthActivity.class), 0);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Log.d(TAG, "Authenticated, retrying update...");
            updateResourceCount().execute();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUpdateFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
