package foosh.air.foi.hr.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import foosh.air.foi.hr.R;
import foosh.air.foi.hr.adapters.PagerAdapter;

/**
 * Aktivnost za prikaz prijavljenih i objavljenih oglasa prijavljenog korisnika.
 */
public class MyListingsActivity extends NavigationDrawerBaseActivity {

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=1;
    //what happens with layout when selected tab changes

    /**
     * Dohvaća naziv aktivnosti.
     * @return
     */
    public static String getMenuTitle(){
        return "Moji oglasi";
    }

    /**
     * Izbjegavanje efekata prijelaza iz jedne aktivnosti u drugu.
     */
    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    /**
     * Dohvaćanje potrebnih widgeta.
     * Učitavanje AdMob oglasa.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_my_listing_base, contentLayout);

        AppBarLayout appBarLayoutMain = findViewById(R.id.id_appbar_main);
        appBarLayoutMain.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionbar.setTitle("Moji oglasi");

        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = contentLayout.findViewById(R.id.id_viewpager);

        TabLayout mTabLayout = contentLayout.findViewById(R.id.id_tabs);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Listener koji se poziva klkom na opciju Moji oglasi su glavnom bočnom meniju.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Aktivira se klikom na hamburger.
     * Otvara bočni meni.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return false;
        }
    }
}
