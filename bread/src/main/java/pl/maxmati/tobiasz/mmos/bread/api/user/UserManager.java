package pl.maxmati.tobiasz.mmos.bread.api.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.APIRequest;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;

/**
 * Created by mmos on 20.02.16.
 *
 * @author mmos
 */
public class UserManager {
    public static final String TAG = "UserManager";

    private static final String USER_PATH = "user/";

    public static final String STORE_NAME = "UsersStore";
    public static final String STORE_FIELD_NAME_USERS = "users";

    private UserManager() {
    }

    public static User[] get(APIConnector apiConnector) throws SessionException {
        ResponseEntity<UserRecord[]> response = apiConnector.sendRequest(new APIRequest
                (HttpMethod.GET, USER_PATH, null, null), UserRecord[].class);
        UserRecord[] userRecords = response.getBody();
        User[] users = new User[userRecords.length];
        for(int userCount = 0; userCount < userRecords.length; ++userCount)
            users[userCount] = new User(userRecords[userCount].getId(), userRecords[userCount]
                    .getName());
        return users;
    }

    private static SharedPreferences getStore(Context context) {
        return context.getApplicationContext().getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
    }

    public static void clearStore(Context context) {
        getStore(context).edit().clear().apply();
        Log.d(TAG, "Users store cleared");
    }

    public static void storeUsers(Context context, User[] users) {
        SharedPreferences.Editor preferencesEditor = getStore(context).edit();
        preferencesEditor.putString(STORE_FIELD_NAME_USERS, new Gson().toJson(users));
        preferencesEditor.apply();

        Log.d(TAG, "Stored users: " + Arrays.toString(users));
    }

    public static User[] restoreUsers(Context context) {
        User[] users;

        if(getStore(context).contains(STORE_FIELD_NAME_USERS)) {
            users = new Gson().fromJson(getStore(context).getString(STORE_FIELD_NAME_USERS, null),
                    User[].class);
        } else {
            try {
                users = get(new APIConnector(context, SessionManager.restoreSession(context)));
                storeUsers(context, users);
                return users;
            } catch (SessionException e) {
                Log.d(TAG, "Failed to fetch user list: " + e.getMessage());
                return null;
            }
        }
        Log.d(TAG, "Restored users: " + Arrays.toString(users));
        return users;
    }
}
