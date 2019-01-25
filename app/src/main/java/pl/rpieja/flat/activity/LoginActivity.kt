package pl.rpieja.flat.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import pl.rpieja.flat.R
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService


class LoginActivity : AppCompatActivity() {

    private var signInButton: Button? = null
    private var passwordTextEdit: EditText? = null
    private var usernameTextEdit: EditText? = null

    private var loginDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        signInButton = findViewById(R.id.signInButton)
        usernameTextEdit = findViewById(R.id.usernameTextEdit)
        passwordTextEdit = findViewById(R.id.passwordTextEdit)

        signInButton!!.setOnClickListener(View.OnClickListener {
            val username = usernameTextEdit!!.text.toString()
            val password = passwordTextEdit!!.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(applicationContext, "Username or password empty.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val flatAPI = FlatAPI.getFlatApi(this)

            val registrationToken = FirebaseInstanceId.getInstance().token!!
            loginDisposable = flatAPI.login(username, password)
                    .flatMapMaybe {
                        if (it) {
                            flatAPI.registerFCM(registrationToken).toMaybe()
                        } else {
                            Maybe.just(false)
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (!it) {
                            Toast.makeText(applicationContext,
                                    "Wrong username or password.", Toast.LENGTH_SHORT).show()
                        } else {
                            AccountService.addAccount(this, username,
                                    FlatAPI.getCookieJar(this).sessionId!!)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }, {
                        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show()
                    }
                    )
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        loginDisposable?.dispose()
    }
}
