package pl.maxmati.tobiasz.flat.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.Arrays;
import java.util.HashSet;

import pl.maxmati.tobiasz.flat.R;
import pl.maxmati.tobiasz.flat.api.user.User;
import pl.maxmati.tobiasz.flat.api.user.UserManager;

/**
 * Created by mmos on 20.02.16.
 *
 * @author mmos
 */
public class UserFragment extends Fragment {
    public static final String TAG = "UserFragment";

    private static final String STATE_EVERYBODY_CHECKED = "everybodyChecked";
    private static final String STATE_SELECTED_CHECKED = "selectedChecked";
    public static final String STATE_CHECKED_USERS = "checkedUsers";

    private User[] allUsers;
    private HashSet<User> selectedUsers;

    private ListView personListView;
    private RadioButton everybodyRadioButton;
    private RadioButton selectedRadioButton;
    private UserAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreData(savedInstanceState);
        Log.d(TAG, "Fragment created");
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        allUsers = UserManager.restoreUsers(activity);
        // FIXME: if allUsers returns null, expect NullPointerException
        if(selectedUsers == null) {
            selectedUsers = new HashSet<>();
            Log.d(TAG, "First creation of selected users set");
        }
        userAdapter = new UserAdapter(activity, R.layout.user_checkbox, allUsers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Building view");
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        updateViewRefs(view);

        personListView.setAdapter(userAdapter);

        selectedRadioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (userAdapter != null) {
                            userAdapter.setUserSelectEnabled(b);
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });

        restoreViewData(savedInstanceState);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        view.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_EVERYBODY_CHECKED, everybodyRadioButton.isChecked());
        outState.putBoolean(STATE_SELECTED_CHECKED, selectedRadioButton.isChecked());
        outState.putSerializable(STATE_CHECKED_USERS, selectedUsers);
    }

    private void restoreData(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            return;

        selectedUsers = (HashSet<User>) savedInstanceState.getSerializable(STATE_CHECKED_USERS);
        if(selectedUsers == null) {
            Log.e(TAG, "Failed to restore selected users");
        } else {
            Log.d(TAG, "Restored " + selectedUsers.size() + " users");
        }
    }

    private void restoreViewData(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            everybodyRadioButton.setChecked(true);
            return;
        }
        everybodyRadioButton.setChecked(savedInstanceState.getBoolean(STATE_EVERYBODY_CHECKED));
        selectedRadioButton.setChecked(savedInstanceState.getBoolean(STATE_EVERYBODY_CHECKED));
    }

    private void updateViewRefs(final View view) {
        personListView = (ListView) view.findViewById(R.id.users_listview);
        everybodyRadioButton = (RadioButton) view.findViewById(R.id.users_everybody_radiobutton);
        selectedRadioButton = (RadioButton) view.findViewById(R.id.users_selected_radiobutton);
    }

    public User[] getSelectedUsers() {
        if(everybodyRadioButton.isChecked())
            return allUsers;
        else
            return selectedUsers.toArray(new User[selectedUsers.size()]);
    }

    private class UserAdapter extends ArrayAdapter<User> {
        private final Context context;
        private int resource;
        private final User[] objects;
        private boolean userSelectEnabled;

        public UserAdapter(Context context, int resource, User[] objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.objects = objects;
            this.userSelectEnabled = false;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            CheckBox checkBox;
            final User user = objects[position];

            if(convertView == null)
                view = ((LayoutInflater) context.getSystemService(Context
                        .LAYOUT_INFLATER_SERVICE)).inflate(resource, parent, false);
            else
                view = convertView;

            checkBox = (CheckBox) view.findViewById(R.id.user_checkbox);
            checkBox.setText(user.toString());
            checkBox.setEnabled(userSelectEnabled);
            if(selectedUsers.contains(user)) {
                checkBox.setChecked(true);
                Log.d(TAG, "Selecting user " + user + " as it was checked in previous instance");
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        selectedUsers.add(user);
                    else
                        selectedUsers.remove(user);
                    Log.d(TAG, "Selected users: " + Arrays.toString(getSelectedUsers()));
                }
            });
            return view;
        }

        public void setUserSelectEnabled(boolean userSelectEnabled) {
            this.userSelectEnabled = userSelectEnabled;
        }
    }
}
