package pl.rpieja.flat;

import android.app.FragmentTransaction;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import pl.rpieja.flat.dto.CreateChargeDTO;
import pl.rpieja.flat.dto.User;
import pl.rpieja.flat.tasks.AsyncCreateCharge;
import pl.rpieja.flat.util.IsoTimeFormatter;
import pl.rpieja.flat.viewmodels.NewChargeViewModel;

public class NewChargeActivity extends AppCompatActivity {

    private static final String SET_DATE_TAG = "pl.rpieja.flat.newCharge.setDate";
    private Toolbar toolbar;
    private NewChargeViewModel newChargeViewModel;

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

        bindEditTextWithLiveData(findViewById(R.id.new_charge_name), newChargeViewModel.chargeName);

        bindEditTextWithLiveData(findViewById(R.id.newChargeAmount), newChargeViewModel.chargeAmount);

        RecyclerView users = findViewById(R.id.newChargeUsersList);
        users.setLayoutManager(new LinearLayoutManager(this));
        users.setAdapter(new UsersListAdapter(newChargeViewModel.getUsers(),
                newChargeViewModel.getSelectedUsers(), this));

        FloatingActionButton accept = findViewById(R.id.accept_button);
        accept.setEnabled(newChargeViewModel.isValid.getValue());
        newChargeViewModel.isValid.observe(this, isValid -> {
            accept.setEnabled(isValid);
            if (isValid){
                accept.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(R.color.colorAccent, getTheme())));
            } else {
                accept.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(R.color.iconColorGreyDark, getTheme())));
            }
        });
        accept.setOnClickListener(view -> newChargeViewModel.createCharge(
                NewChargeActivity.this, NewChargeActivity.this::finish));
    }

    private void bindEditTextWithLiveData(EditText field, MutableLiveData<String> liveData) {
        final String value = liveData.getValue();
        if (value != null) {
            field.setText(value);
        }
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                liveData.setValue(editable.toString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        private final MutableLiveData<Set<User>> selectedUsers;
        private List<User> users;


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public CheckBox mCheckBox;

            public ViewHolder(CheckBox v) {
                super(v);
                mCheckBox = v;
            }
        }

        public UsersListAdapter(LiveData<List<User>> users, MutableLiveData<Set<User>> selectedUsers,
                                LifecycleOwner lifecycleOwner) {
            users.observe(lifecycleOwner, this::setUsers);
            setUsers(users.getValue());
            this.selectedUsers = selectedUsers;
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
            holder.mCheckBox.setChecked(selectedUsers.getValue().contains(user));
            holder.mCheckBox.setOnClickListener(view -> {
                if (selectedUsers.getValue().contains(user)) {
                    selectedUsers.getValue().remove(user);
                } else {
                    selectedUsers.getValue().add(user);
                }
                selectedUsers.setValue(selectedUsers.getValue());
            });
        }

        @Override
        public int getItemCount() {
            return users != null ? users.size() : 0;
        }
    }
}
