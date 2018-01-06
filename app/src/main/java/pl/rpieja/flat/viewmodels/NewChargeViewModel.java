package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.authentication.AccountService;
import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.dto.User;
import pl.rpieja.flat.tasks.AsyncFetchUsers;

/**
 * Created by maxmati on 11/28/17.
 */

public class NewChargeViewModel extends ViewModel {

    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    private MutableLiveData<Set<User>> selectedUsers = new MutableLiveData<>();
    public final MutableLiveData<Calendar> chargeDate = new MutableLiveData<>();
    public final MutableLiveData<String> chargeName = new MutableLiveData<>();
    public final MutableLiveData<String> chargeAmount = new MutableLiveData<>();
    public final MediatorLiveData<Boolean> isValid = new MediatorLiveData<>();


    public NewChargeViewModel() {
        selectedUsers.setValue(new HashSet<>());
        chargeDate.setValue(Calendar.getInstance());

        validate();
        isValid.addSource(selectedUsers, value -> this.validate());
        isValid.addSource(chargeName, value -> this.validate());
        isValid.addSource(chargeAmount, value -> this.validate());

    }

    private void validate() {
        if (selectedUsers.getValue() == null || selectedUsers.getValue().isEmpty()){
            isValid.setValue(false);
            return;
        }

        if (chargeName.getValue() == null || chargeName.getValue().isEmpty()){
            isValid.setValue(false);
            return;
        }

        if (chargeAmount.getValue() == null || chargeAmount.getValue().isEmpty()){
            isValid.setValue(false);
            return;
        }

        isValid.setValue(true);
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public MutableLiveData<Set<User>> getSelectedUsers() {
        return selectedUsers;
    }

    public void loadUsers(Context context) {
        FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(context));

        if (users.getValue() != null) return;

        new AsyncFetchUsers(flatAPI, usersList -> users.setValue(usersList),
                () -> AccountService.removeCurrentAccount(context)).execute();
    }
}
