package pl.rpieja.flat;

import android.app.FragmentTransaction;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.dto.CreateCharge;
import pl.rpieja.flat.dto.User;
import pl.rpieja.flat.tasks.AsyncCreateCharge;
import pl.rpieja.flat.util.IsoTimeFormatter;
import pl.rpieja.flat.viewmodels.NewChargeViewModel;

public class NewChargeActivity extends AppCompatActivity {

    private static final String SET_DATE_TAG = "pl.rpieja.flat.newCharge.setDate";
    private Toolbar toolbar;
    private NewChargeViewModel newChargeViewModel;
    private EditText newChargeName;
    private EditText newChargeAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_charge);

        toolbar = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newChargeViewModel = ViewModelProviders.of(this).get(NewChargeViewModel.class);
        newChargeViewModel.loadUsers(getApplicationContext());

        prepareDateSelectionField(newChargeViewModel);

        newChargeName = findViewById(R.id.new_charge_name);
        final String chargeName = newChargeViewModel.chargeName.getValue();
        if (chargeName != null) {
            newChargeName.setText(chargeName);
        }

        newChargeAmount = findViewById(R.id.newChargeAmount);
        final String chargeAmount = newChargeViewModel.chargeAmount.getValue();
        if (chargeAmount != null) {
            newChargeAmount.setText(chargeAmount);
        }

        ListView users = findViewById(R.id.newChargeUsersList);
        users.setAdapter(new UsersListAdapter(getApplicationContext(),
                newChargeViewModel.getUsers(), newChargeViewModel.getSelectedUsers(), this));

        FloatingActionButton accept = findViewById(R.id.accept_button);
        accept.setOnClickListener(view -> {
            updateViewModel();
            CreateCharge charge = new CreateCharge();
            charge.date = IsoTimeFormatter.toIso8601(newChargeViewModel.chargeDate.getValue().getTime());
            charge.name = newChargeViewModel.chargeName.getValue();
            charge.rawAmount = newChargeViewModel.chargeAmount.getValue();
            for(User user : newChargeViewModel.getSelectedUsers().getValue()){
                charge.to.add(user.id);
            }

            FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(NewChargeActivity.this));
            new AsyncCreateCharge(flatAPI, NewChargeActivity.this::finish, () -> {}).execute(charge);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateViewModel();
    }

    private void updateViewModel() {
        newChargeViewModel.chargeName.setValue(newChargeName.getText().toString());
        newChargeViewModel.chargeAmount.setValue(newChargeAmount.getText().toString());
    }

    private void prepareDateSelectionField(NewChargeViewModel newChargeViewModel) {
        DateDialog currentSetDate =
                (DateDialog) getFragmentManager().findFragmentByTag(SET_DATE_TAG);
        if (currentSetDate != null) {
            currentSetDate.setDateSetListener(newChargeViewModel.chargeDate::setValue);
        }


        TextView newChargeDate = findViewById(R.id.newChargeDate);
        newChargeDate.setOnClickListener(view -> {
            DateDialog dialog = new DateDialog();
            dialog.setDateSetListener(newChargeViewModel.chargeDate::setValue);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, SET_DATE_TAG);
        });


        newChargeViewModel.chargeDate.observe(this, calendar -> {
            assert calendar != null;
            newChargeDate.setText(DateFormat.getLongDateFormat(this).format(calendar.getTime()));
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_charge, menu);
        return true;
    }

    private static class UsersListAdapter extends BaseAdapter {
        private final Context context;
        private List<User> users;
        private Set<User> selectedUsers;

        public UsersListAdapter(Context context, LiveData<List<User>> users,
                                MutableLiveData<Set<User>> selectedUsers,
                                LifecycleOwner lifecycleOwner) {
            this.context = context;
            setUsers(users.getValue());
            users.observe(lifecycleOwner, this::setUsers);
            setSelectedUsers(selectedUsers.getValue());
            selectedUsers.observe(lifecycleOwner, this::setSelectedUsers);
        }

        private void setUsers(List<User> users) {
            this.users = users;
            if (this.users == null) {
                this.users = new ArrayList<>();
            }
            this.notifyDataSetChanged();
        }

        private void setSelectedUsers(Set<User> selectedUsers) {
            this.selectedUsers = selectedUsers;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int i) {
            return users.get(i);
        }

        @Override
        public long getItemId(int i) {
            return users.get(i).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            final User user = users.get(position);

            if (convertView == null) {
                view = ((LayoutInflater) context.getSystemService(Context
                        .LAYOUT_INFLATER_SERVICE)).inflate(R.layout.user_list_item, parent, false);
            } else {
                view = convertView;
            }

            CheckBox checkBox = (CheckBox) view;
            checkBox.setText(user.name);
            checkBox.setChecked(selectedUsers.contains(user));
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    selectedUsers.add(user);
                } else {
                    selectedUsers.remove(user);
                }
            });

            return view;
        }
    }

}
