package pl.maxmati.tobiasz.flat.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import pl.maxmati.tobiasz.flat.R;
import pl.maxmati.tobiasz.flat.api.user.User;

/**
 * Created by mmos on 20.02.16.
 *
 * @author mmos
 */
public class BillingFragment extends Fragment {
    public static String TAG = "BillingFragment";

    private OnBillingAction mCallback;

    private WeightedValueFragment valueFragment;
    private WeightedUserFragment userFragment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnBillingAction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnBillingAction");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView()");

        View view;

        view = inflater.inflate(R.layout.fragment_billing, container, false);
        setContainerOrientation(view);

        if(savedInstanceState != null) {
            valueFragment = (WeightedValueFragment) getChildFragmentManager().getFragment(savedInstanceState,
                    ValueFragment.TAG);
            userFragment = (WeightedUserFragment) getChildFragmentManager().getFragment(savedInstanceState,
                    UserFragment.TAG);
        } else {
            valueFragment = new WeightedValueFragment();
            getChildFragmentManager().beginTransaction().add(R.id.billing_fragment_container,
                    valueFragment, ValueFragment.TAG).commit();
            userFragment = new WeightedUserFragment();
            getChildFragmentManager().beginTransaction().add(R.id.billing_fragment_container,
                    userFragment, UserFragment.TAG).commit();
        }

        view.findViewById(R.id.billing_update_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.onUpdatePressed(valueFragment.getValue(), userFragment.getSelectedUsers());
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(valueFragment == null)
            Log.e(TAG, "Saving null value fragment");
        getChildFragmentManager().putFragment(outState, ValueFragment.TAG, valueFragment);
        getChildFragmentManager().putFragment(outState, UserFragment.TAG, userFragment);
    }

    public interface OnBillingAction {
        void onBackPressed();
        void onUpdatePressed(double value, User[] users);
    }

    private void setContainerOrientation(View view) {
        LinearLayout billingFrameContainer = (LinearLayout) view.findViewById(R.id
                .billing_fragment_container);
        int orientation = LinearLayout.HORIZONTAL;

        if(billingFrameContainer == null)
            return;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            orientation = LinearLayout.VERTICAL;

        billingFrameContainer.setOrientation(orientation);
    }

    public static void makeElementsSameWidth(LinearLayout linearLayout) {
        LinearLayout.LayoutParams layoutParams;
        if(linearLayout.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE) {
            layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        }
        layoutParams.weight = 1;
        linearLayout.setLayoutParams(layoutParams);
    }

    public static class WeightedValueFragment extends ValueFragment {
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            makeElementsSameWidth((LinearLayout) view);
        }
    }

    public static class WeightedUserFragment extends UserFragment {
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            makeElementsSameWidth((LinearLayout) view);
        }
    }
}
