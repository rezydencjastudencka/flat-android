package pl.rpieja.flat.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import pl.rpieja.flat.R
import pl.rpieja.flat.fragment.ChargesFragment
import pl.rpieja.flat.fragment.TransfersFragment

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<NavigationView>(R.id.navigation).setNavigationItemSelectedListener(this)

        // FIXME does not scale well with more perspectives
        if (supportFragmentManager.findFragmentByTag(ChargesFragment.tag) == null &&
                supportFragmentManager.findFragmentByTag(TransfersFragment.tag) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, ChargesFragment(), ChargesFragment.tag)
                    .commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.charges_nav -> {
                val fragment = supportFragmentManager.findFragmentByTag(ChargesFragment.tag) ?:
                        ChargesFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content_frame,
                        fragment, ChargesFragment.tag).commit()
            }
            R.id.transfers_nav -> {
                val fragment = supportFragmentManager.findFragmentByTag(TransfersFragment.tag) ?:
                        TransfersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content_frame,
                        fragment, TransfersFragment.tag).commit()
            }
        }

        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)

        return true
    }

}