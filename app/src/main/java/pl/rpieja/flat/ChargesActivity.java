package pl.rpieja.flat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.rpieja.flat.authentication.AccountService;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.viewmodels.ChargesViewModel;

public class ChargesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int month, year;

    ChargesViewModel chargesViewModel;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);

        chargesViewModel = ViewModelProviders.of(this).get(ChargesViewModel.class);
        chargesViewModel.loadCharges(getApplicationContext(), month, year);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        if(savedInstanceState != null) {
            viewPager.setCurrentItem(savedInstanceState.getInt("currentItem"));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ChargesActivity.this, NewChargeActivity.class);
            startActivity(intent);
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentItem", viewPager.getCurrentItem());
    }

    public void showDatePickerDialog() {
        final RackMonthPicker picker = new RackMonthPicker(this);
        picker.setLocale(Locale.ENGLISH)
                .setColorTheme(R.color.colorPrimaryDark)
                .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                    ChargesActivity This = ChargesActivity.this;
                    This.month = month;
                    This.year = year;
                    This.chargesViewModel.loadCharges(This.getApplicationContext(), month, year);
                })
                .setNegativeButton(dialog -> picker.dismiss()).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by:
                new ChargesSortDialogFragment().show(getFragmentManager(), "chargesSortDialog");
                return true;

            case R.id.action_setmonth:
                showDatePickerDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.transfers_nav) {
            //Open Transfers Activity
        } else if (id == R.id.logout_nav) {
            AccountService.removeCurrentAccount(this);
            Intent intent = new Intent(ChargesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new ChargesTab();
                case 1: return new ExpensesTab();
                case 2: return new SummaryTab();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
