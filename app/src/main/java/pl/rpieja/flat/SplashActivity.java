package pl.rpieja.flat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.tasks.AsyncValidate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this.getApplicationContext()));
        FlatAPI flatAPI = new FlatAPI(cookieJar);
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
