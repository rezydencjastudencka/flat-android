package pl.rpieja.flat.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.rpieja.flat.fragment.ChargesFragment
import pl.rpieja.flat.R

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