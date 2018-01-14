package pl.rpieja.flat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (supportFragmentManager.findFragmentByTag(ChargesFragment.tag) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, ChargesFragment(), ChargesFragment.tag)
                    .commit()
        }
    }
}