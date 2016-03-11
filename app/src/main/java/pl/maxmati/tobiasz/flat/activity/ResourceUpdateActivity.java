package pl.maxmati.tobiasz.flat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Date;

import pl.maxmati.tobiasz.flat.R;
import pl.maxmati.tobiasz.flat.api.charge.Charge;
import pl.maxmati.tobiasz.flat.api.session.SessionExpiredException;
import pl.maxmati.tobiasz.flat.api.user.User;
import pl.maxmati.tobiasz.flat.fragment.BillingFragment;
import pl.maxmati.tobiasz.flat.fragment.QuantityFragment;

public class ResourceUpdateActivity extends ProgressBarActivity implements
        QuantityFragment.OnQuantityActionListener, BillingFragment.OnBillingAction {
    public static final String RESOURCE_FIELD_NAME = "resourceName";
    public static final String ENABLE_DECREASE_BUTTON_MAX_VALUE_FIELD_NAME =
            "enableDecreaseButtonMaxValue";

    private static final String TAG = "ResourceUpdateActivity";

    private String resourceName;

    private BillingFragment billingFragment;

    private View frameContainer;
    private View progressBar;

    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras;
        QuantityFragment quantityFragment;

        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_resource_update);

        resourceName = getIntent().getStringExtra(RESOURCE_FIELD_NAME);
        // FIXME: it should be checked before activity creation
        if(resourceName == null || resourceName.isEmpty()) {
            Log.d(TAG, "No resource name supplied, canceling activity");
            finish();
            return;
        }

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(BillingFragment.TAG))
                billingFragment = (BillingFragment) getFragmentManager().getFragment(savedInstanceState,
                        BillingFragment.TAG);
        }

        quantityFragment = new QuantityFragment();
        extras = getIntent().getExtras();
        if(extras.containsKey(ENABLE_DECREASE_BUTTON_MAX_VALUE_FIELD_NAME)) {
            Bundle argumentBundle = new Bundle(1);
            argumentBundle.putInt(QuantityFragment.ARGUMENT_ENABLE_DECREASE_BUTTON_MAX_VALUE,
                    extras.getInt(ENABLE_DECREASE_BUTTON_MAX_VALUE_FIELD_NAME));
            quantityFragment.setArguments(argumentBundle);
        }
        if(billingFragment == null)
            billingFragment = new BillingFragment();

        if(getFragmentManager().findFragmentByTag(QuantityFragment.TAG) == null)
            getFragmentManager().beginTransaction().add(R.id.resource_update_frame_container,
                    quantityFragment, QuantityFragment.TAG).commit();

        frameContainer = findViewById(R.id.resource_update_frame_container);
        progressBar = findViewById(R.id.resource_update_progressbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        billingFragment =
                (BillingFragment) getFragmentManager().findFragmentByTag(BillingFragment.TAG);
        if(billingFragment != null)
            getFragmentManager().putFragment(outState, BillingFragment.TAG, billingFragment);
    }

    @Override
    public void onIncrementPressed(int quantity) {
        this.quantity = quantity;

        if(billingFragment == null)
            billingFragment = new BillingFragment();

        getFragmentManager().beginTransaction().replace(R.id.resource_update_frame_container,
                billingFragment, BillingFragment.TAG).addToBackStack("quantity_to_billing").commit();
    }

    @Override
    public void onDecrementPressed(int quantity) {
        this.quantity = quantity;

        new UpdateCounterTask(this, resourceName, -quantity) {
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if(integer == null) {
                    handleException(getException());
                    return;
                }
                // TODO: do async update of counter listeners
                setResult(RESULT_OK);
                finish();
            }
        }.execute();
    }

    @Override
    public void onUpdatePressed(final double value, final User[] users) {
        new UpdateCounterTask(this, resourceName, quantity) {
            @Override
            protected void onPostExecute(Integer integer) {
                if(integer == null) {
                    showProgress(false);
                    handleException(getException());
                    return;
                }
                if(users.length != 0) {
                    new CreateChargeTask(ResourceUpdateActivity.this, new Charge(getString(R.string
                            .charge_name)
                            , new Date(), users, value)) {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if (getException() != null) {
                                showProgress(false);
                                handleException(getException());
                                return;
                            }

                            setResult(RESULT_OK);
                            finish();
                        }
                    }.execute();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }

                // TODO: do async update of counter listeners
            }
        }.execute();
    }

    private void handleException(Exception e) {
        if(e == null)
            return;

        if(e instanceof SessionExpiredException) {
            startActivity(new Intent(this, APIAuthActivity.class));
            // TODO: finalize activity automatically after auth success
        } else {
            new AlertDialog.Builder(ResourceUpdateActivity.this)
                    .setTitle(getString(R.string.error_update))
                    .setMessage(e.getLocalizedMessage())
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            frameContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            frameContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    frameContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            frameContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
