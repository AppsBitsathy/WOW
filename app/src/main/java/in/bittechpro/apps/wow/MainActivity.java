package in.bittechpro.apps.wow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences ;

    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.reward,
            R.drawable.map,
            R.drawable.cash,
            R.drawable.corporate

    };
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(SPrefManager.LOGGED)){
            if (sharedpreferences.getInt(SPrefManager.LOGGED, 1) == 0) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }else{
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        setTitle("");

        viewPager = findViewById(R.id.pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setSaveFromParentEnabled(true);


        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);
        if (sharedpreferences.getString(SPrefManager.ROLE, "1").equals("2")) {
            Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(tabIcons[3]);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RewardFragment(), "PROFILE");
        adapter.addFrag(new MapFragment(), "FIND DEVICE");
        adapter.addFrag(new ExchangeFragment(),"EXCHANGE");
        if (sharedpreferences.getString(SPrefManager.ROLE, "1").equals("2")) {
            adapter.addFrag(new CorporateFragment(), "CORPORATE");
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_main_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }
}
