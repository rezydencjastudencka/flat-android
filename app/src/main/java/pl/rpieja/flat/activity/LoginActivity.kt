package pl.rpieja.flat.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import pl.rpieja.flat.R
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.tasks.AsyncLogin


class LoginActivity : AppCompatActivity() {

    private var signInButton: Button? = null
    private var passwordTextEdit: EditText? = null
    private var usernameTextEdit: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        signInButton = findViewById(R.id.signInButton)
        usernameTextEdit = findViewById(R.id.usernameTextEdit)
        passwordTextEdit = findViewById(R.id.passwordTextEdit)

        signInButton!!.setOnClickListener(View.OnClickListener { _ ->
            val username = usernameTextEdit!!.text.toString()
            val password = passwordTextEdit!!.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(applicationContext, "Username or password empty.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val cookieJar = FlatCookieJar(this@LoginActivity)
            val flatAPI = FlatAPI(this, cookieJar)

            val registrationToken = FirebaseInstanceId.getInstance().token
            AsyncLogin(flatAPI, username, password, registrationToken, {
                if (!it) {
                    runOnUiThread {
                        Toast.makeText(applicationContext,
                                "Wrong username or password.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    AccountService.addAccount(this@LoginActivity,
                            username, cookieJar.sessionId!!)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, {}).execute()

        })
    }
}
