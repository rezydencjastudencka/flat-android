package pl.rpieja.flat.authentication

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import pl.rpieja.flat.R
import pl.rpieja.flat.api.FlatAPI

class AccountService : Service() {
    private var authenticator: Authenticator? = null

    override fun onCreate() {
        authenticator = Authenticator(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return authenticator!!.iBinder
    }

    private class Authenticator internal constructor(context: Context) : AbstractAccountAuthenticator(context) {

        override fun editProperties(accountAuthenticatorResponse: AccountAuthenticatorResponse, s: String): Bundle {
            throw UnsupportedOperationException()
        }

        override fun addAccount(accountAuthenticatorResponse: AccountAuthenticatorResponse, s: String, s1: String, strings: Array<String>, bundle: Bundle): Bundle {
            throw UnsupportedOperationException()
        }

        override fun confirmCredentials(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, bundle: Bundle): Bundle {
            throw UnsupportedOperationException()
        }

        override fun getAuthToken(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, s: String, bundle: Bundle): Bundle {
            throw UnsupportedOperationException()
        }

        override fun getAuthTokenLabel(s: String): String {
            throw UnsupportedOperationException()
        }

        override fun updateCredentials(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, s: String, bundle: Bundle): Bundle {
            throw UnsupportedOperationException()
        }

        override fun hasFeatures(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, strings: Array<String>): Bundle {
            throw UnsupportedOperationException()
        }
    }

    companion object {
        private fun getAccountType(context: Context): String = context.getString(R.string.account_type)

        fun removeCurrentAccount(context: Context) {
            val am = AccountManager.get(context)
            val accounts = am.getAccountsByType(getAccountType(context))
            if (accounts.isEmpty()) return
            am.removeAccountExplicitly(accounts[0])
            FlatAPI.reset()
        }

        fun addAccount(context: Context, username: String, token: String) {
            val am = AccountManager.get(context)
            val account = AccountService.getAccount(context, username)
            am.addAccountExplicitly(account, token, null)
        }

        fun getAuthToken(context: Context): String? {
            val am = AccountManager.get(context)
            val accounts = am.getAccountsByType(getAccountType(context))
            return if (accounts.isEmpty()) null else am.getPassword(accounts[0])
        }

        private fun getAccount(context: Context, accountName: String): Account {
            return Account(accountName, getAccountType(context))
        }
    }
}
