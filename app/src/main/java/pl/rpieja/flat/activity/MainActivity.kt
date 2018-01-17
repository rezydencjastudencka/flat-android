package pl.rpieja.flat.activity
import android.content.Intent
import android.drm.DrmStore
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import pl.rpieja.flat.R
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.fragment.ChargesFragment
import pl.rpieja.flat.fragment.TransfersFragment

class MainActivityNavigation(val activity: MainActivity):
        NavigationView.OnNavigationItemSelectedListener {

    private val drawer: DrawerLayout = activity.findViewById(R.id.drawer_layout)
    private var toggle: ActionBarDrawerToggle? = null

    init {
        activity.findViewById<NavigationView>(R.id.navigation)
                .setNavigationItemSelectedListener(this)
        setupHamburger()
    }

    private fun setupHamburger() {
        toggle = ActionBarDrawerToggle(activity, drawer, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle!!)
        toggle!!.syncState() // FIXME call syncState() from onPostCreate()

        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setHomeButtonEnabled(true)
    }

    fun toggleDrawer(): Boolean {
        return if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            true
        } else {
            false
        }
    }

    fun openDrawer(item: MenuItem?): Boolean {
        return toggle!!.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.charges_nav -> {
                val fragment = activity.supportFragmentManager
                        .findFragmentByTag(ChargesFragment.tag) ?: ChargesFragment()
                activity.supportFragmentManager.beginTransaction().replace(R.id.content_frame,
                        fragment, ChargesFragment.tag).commit()
            }
            R.id.transfers_nav -> {
                val fragment = activity.supportFragmentManager
                        .findFragmentByTag(TransfersFragment.tag) ?: TransfersFragment()
                activity.supportFragmentManager.beginTransaction().replace(R.id.content_frame,
                        fragment, TransfersFragment.tag).commit()
            }
            R.id.logout_nav -> {
                AccountService.removeCurrentAccount(activity)
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finish()
            }
        }

        activity.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)

        return true
    }
}

class MainActivity: AppCompatActivity() {
    private var navigation: MainActivityNavigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        navigation = MainActivityNavigation(this)

        // FIXME does not scale well with more perspectives
        if (supportFragmentManager.findFragmentByTag(ChargesFragment.tag) == null &&
                supportFragmentManager.findFragmentByTag(TransfersFragment.tag) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, ChargesFragment(), ChargesFragment.tag)
                    .commit()
        }
    }

    override fun onBackPressed() {
        if (!navigation!!.toggleDrawer()) super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (navigation!!.openDrawer(item))
            true
        else
            super.onOptionsItemSelected(item)
    }
}