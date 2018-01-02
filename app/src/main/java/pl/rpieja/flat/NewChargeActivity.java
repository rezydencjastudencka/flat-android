package pl.rpieja.flat;

import android.app.FragmentTransaction;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

        RecyclerView users = findViewById(R.id.newChargeUsersList);
        users.setLayoutManager(new LinearLayoutManager(this));
        users.setAdapter(new UsersListAdapter(newChargeViewModel.getUsers(),
                newChargeViewModel.getSelectedUsers(), this));

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


    public static class UsersListAdapter extends RecyclerView.Adapter<NewChargeActivity.UsersListAdapter.ViewHolder> {
        private final Set<User> selectedUsers;
        private List<User> users;


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public CheckBox mCheckBox;

            public ViewHolder(CheckBox v) {
                super(v);
                mCheckBox = v;
            }
        }

        public UsersListAdapter(LiveData<List<User>> users, LiveData<Set<User>> selectedUsers,
                                LifecycleOwner lifecycleOwner) {
            users.observe(lifecycleOwner, this::setUsers);
            setUsers(users.getValue());
            this.selectedUsers = selectedUsers.getValue();
        }

        private void setUsers(List<User> users) {
            this.users = users;
            this.notifyDataSetChanged();
        }

        @Override
        public NewChargeActivity.UsersListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                int viewType) {
            CheckBox v = (CheckBox) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_item, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final User user = users.get(position);
            holder.mCheckBox.setText(user.name);
            holder.mCheckBox.setChecked(selectedUsers.contains(user));
            holder.mCheckBox.setOnClickListener(view -> {
                if (selectedUsers.contains(user)) {
                    selectedUsers.remove(user);
                } else {
                    selectedUsers.add(user);
                }
            });
        }

        @Override
        public int getItemCount() {
            return users != null ? users.size() : 0;
        }
    }
}
