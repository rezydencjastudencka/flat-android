package pl.rpieja.flat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.rpieja.flat.authentication.FlatCookieJar;
import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.tasks.AsyncValidate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlatAPI flatAPI = new FlatAPI(new FlatCookieJar(this));
        new AsyncValidate().execute(new AsyncValidate.Params(flatAPI, new AsyncValidate.Callable<Boolean>() {
            @Override
            public void onCall(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(SplashActivity.this, ChargesActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }));
    }
}
