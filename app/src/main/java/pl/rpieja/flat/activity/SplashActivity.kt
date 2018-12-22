package pl.rpieja.flat.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import pl.rpieja.flat.api.FlatAPI

class SplashActivity : AppCompatActivity() {

    private var validateSessionDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validateSessionDisposable = FlatAPI.getFlatApi(this).validateSession()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { validSession ->
                    val nextActivity =
                            if (validSession) MainActivity::class.java
                            else LoginActivity::class.java
                    val intent = Intent(this, nextActivity)
                    startActivity(intent)
                    finish()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        validateSessionDisposable?.dispose()
    }
}
