package pl.rpieja.flat.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.tasks.AsyncValidate

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flatAPI = FlatAPI(this, FlatCookieJar(this))
        AsyncValidate(flatAPI) { result ->
            if (result) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                AccountService.removeCurrentAccount(applicationContext)
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.execute()
    }
}
