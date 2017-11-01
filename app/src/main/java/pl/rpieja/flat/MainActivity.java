package pl.rpieja.flat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import pl.rpieja.flat.containers.APILoginContainer;
import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.tasks.AsyncLogin;

public class MainActivity extends AppCompatActivity {

    private Button signInButton;
    private EditText passwordTextEdit, usernameTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = (Button) findViewById(R.id.signInButton);
        usernameTextEdit = (EditText) findViewById(R.id.usernameTextEdit);
        passwordTextEdit = (EditText) findViewById(R.id.passwordTextEdit);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Username or password empty.", Toast.LENGTH_SHORT).show();
                } else {
                    ClearableCookieJar cookieJar =
                            new PersistentCookieJar(new SetCookieCache(),
                                    new SharedPrefsCookiePersistor(MainActivity.this.getApplicationContext()));
                    FlatAPI flatAPI = new FlatAPI(cookieJar);
                    APILoginContainer apiLoginContainer = new APILoginContainer(flatAPI,
                            username,
                            password);

                    new AsyncLogin().execute(new AsyncLogin.Params(apiLoginContainer, new AsyncLogin.Callable<Boolean>() {
                        @Override
                        public void onCall(Boolean aBoolean) {
                            if (aBoolean) {
                                Intent intent = new Intent(MainActivity.this, ChargesActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong username or password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));
                }
            }
        });
    }
}
