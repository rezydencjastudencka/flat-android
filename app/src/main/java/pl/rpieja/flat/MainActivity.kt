package pl.rpieja.flat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag("test") == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, ChargesFragment(), "test")
                    .commit()
        }

    }
}