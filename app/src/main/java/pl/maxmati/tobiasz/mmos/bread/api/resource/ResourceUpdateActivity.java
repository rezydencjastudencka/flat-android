package pl.maxmati.tobiasz.mmos.bread.api.resource;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

import org.springframework.web.client.HttpClientErrorException;

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;
import pl.maxmati.tobiasz.mmos.bread.widget.BreadWidgetUpdater;

public class ResourceUpdateActivity extends Activity {
    public static final String RESOURCE_FIELD_NAME = "resourceName";

    private static final String TAG = "ResourceUpdateActivity";

    private String resourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.countPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateResourceCount().execute();
            }
        });
    }

    private AsyncTask<Void, Void, Void> updateResourceCount() {
        return new AsyncTask<Void, Void, Void>() {
            private int changeCount;

            @Override
            protected void onPreExecute() {
                changeCount = ((NumberPicker) findViewById(R.id.countPicker)).getValue();
                if(((Switch) findViewById(R.id.decreaseSwitch)).isChecked())
                    changeCount = -changeCount;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    APIConnector apiConnector = new APIConnector(SessionManager.restoreSession
                            (ResourceUpdateActivity.this));
                    int updatedCount = ResourceManager.update(apiConnector, resourceName, new
                            ResourceUpdate(changeCount)); // FIXME: add extra passing updated count
                    startService(new Intent(ResourceUpdateActivity.this, BreadWidgetUpdater.class));

                    setResult(RESULT_OK);
                    finish();
                } catch (SessionException | HttpClientErrorException e) {
                    Log.e(TAG, "Resource count update failed: " + e.getMessage());
                }
                return null;
            }
        };
    }
}
