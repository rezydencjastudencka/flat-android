package pl.rpieja.flat.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by maxmati on 11/18/17.
 */

public class AccountService extends Service {
    private Authenticator authenticator;
    public static final String ACCOUNT_TYPE = "pl.memleak.flat";

    public static void removeCurrentAccount(Context context) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length == 0) return;
        am.removeAccountExplicitly(accounts[0]);
    }
    public static void addAccount(Context context, String username, String token) {
        AccountManager am = AccountManager.get(context);
        Account account = AccountService.GetAccount(username);
        am.addAccountExplicitly(account, token, null);
    }

    public static String getAuthToken(Context context) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        if(accounts.length == 0) return null;
        return am.getPassword(accounts[0]);
    }

    private static Account GetAccount(String accountName) {
        return new Account(accountName, ACCOUNT_TYPE);
    }

    @Override
    public void onCreate() {
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

    private static class Authenticator extends AbstractAccountAuthenticator {

        Authenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String s, String s1, String[] strings, Bundle bundle) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }
    }
}
