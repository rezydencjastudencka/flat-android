package pl.rpieja.flat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.rpieja.flat.authentication.AccountService;
import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.tasks.AsyncValidate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(this));
        AsyncValidate.run(flatAPI, new AsyncValidate.Callable<Boolean>() {
            @Override
            public void onCall(Boolean result) {
                if (result) {
                    Intent intent = new Intent(SplashActivity.this, ChargesActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AccountService.removeCurrentAccount(getApplicationContext());
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
