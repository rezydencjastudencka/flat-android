package pl.rpieja.flat.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

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

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public void loadUsers(Context context) {
        FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(context));

        if (users.getValue() != null) return;

        new AsyncFetchUsers(flatAPI, usersList -> users.setValue(usersList),
                () -> AccountService.removeCurrentAccount(context)).execute();
    }
}
