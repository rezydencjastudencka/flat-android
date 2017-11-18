package pl.rpieja.flat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import pl.rpieja.flat.api.FlatAPI;
import pl.rpieja.flat.containers.APIChargesContainer;
import pl.rpieja.flat.dto.ChargesDTO;
import pl.rpieja.flat.viewmodels.ChargesViewModel;

public class ChargesActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    ChargesViewModel chargesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);

        chargesViewModel = ViewModelProviders.of(this).get(ChargesViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(),
                        new SharedPrefsCookiePersistor(ChargesActivity.this.getApplicationContext()));
        FlatAPI flatAPI = new FlatAPI(cookieJar);
        APIChargesContainer apiChargesContainer = new APIChargesContainer(flatAPI,
                11,
                2017);

        chargesViewModel.setApiChargesContainer(apiChargesContainer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                case 0:
                    final ChargesTab chargesTab = new ChargesTab();
                    chargesViewModel.getCharges().observe(chargesTab, new Observer<ChargesDTO>() {
                        @Override
                        public void onChanged(@Nullable ChargesDTO chargesDTO) {
                            if(chargesDTO == null) return;
                            chargesTab.updateListWithCharges(chargesDTO.getCharges());
                        }
                    });
                    return chargesTab;
                case 1:
                    final ExpensesTab expensesTab = new ExpensesTab();
                    chargesViewModel.getCharges().observe(expensesTab, new Observer<ChargesDTO>() {
                        @Override
                        public void onChanged(@Nullable ChargesDTO chargesDTO) {
                            if(chargesDTO == null) return;
                            expensesTab.updateListWithCharges(chargesDTO.getIncomes());
                        }
                    });
                    return expensesTab;
                case 2:
                    SummaryTab summaryTab = new SummaryTab();
                    return summaryTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
