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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.dto.CreateCharge;
import pl.rpieja.flat.dto.User;
import pl.rpieja.flat.tasks.AsyncCreateCharge;
import pl.rpieja.flat.viewmodels.NewChargeViewModel;

public class NewChargeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_charge);

        toolbar = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NewChargeViewModel newChargeViewModel =
                ViewModelProviders.of(this).get(NewChargeViewModel.class);
        newChargeViewModel.loadUsers(getApplicationContext());


        EditText newChargeDate = findViewById(R.id.newChargeDate);
        newChargeDate.setOnFocusChangeListener((view, b) -> {
            if (b) {
                DateDialog dialog = new DateDialog();
                dialog.setEditText(view);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "Set Date");
            }
        });

        EditText newChargeName = findViewById(R.id.new_charge_name);

        EditText newChargeAmount = findViewById(R.id.newChargeAmount);

        ListView users = findViewById(R.id.newChargeUsersList);
        users.setAdapter(new UsersListAdapter(getApplicationContext(),
                newChargeViewModel.getUsers(), newChargeViewModel.getSelectedUsers(), this));

        FloatingActionButton accept = findViewById(R.id.accept_button);
        accept.setOnClickListener(view -> {
            CreateCharge charge = new CreateCharge();
            charge.date = newChargeDate.getText().toString() + "T00:00:00.000Z";
            charge.name = newChargeName.getText().toString();
            charge.rawAmount = newChargeAmount.getText().toString();
            for(User user : newChargeViewModel.getSelectedUsers().getValue()){
                charge.to.add(user.id);
            }

            FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(NewChargeActivity.this));
            new AsyncCreateCharge(flatAPI, NewChargeActivity.this::finish, () -> {}).execute(charge);
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
