package foosh.air.foi.hr;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import foosh.air.foi.hr.fragments.MyListingsFragment;
import foosh.air.foi.hr.model.Listing;

public class MyListingsActivity extends NavigationDrawerBaseActivity implements onListingsDelivered, MyListingsFragment.onFragmentInteractionListener {

    private ConstraintLayout contentLayout;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=1;
    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    //what happens with layout when selected tab changes
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private AppBarLayout appBarLayoutMain;
    private AdView mAdView;

    public static String getMenuTitle(){
        return "Moji oglasi";
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_my_listing_base, contentLayout);

        appBarLayoutMain = findViewById(R.id.id_appbar_main);
        appBarLayoutMain.setVisibility(View.GONE);

        toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionbar.setTitle("Moji oglasi");

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = contentLayout.findViewById(R.id.id_viewpager);

        mTabLayout = contentLayout.findViewById(R.id.id_tabs);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public ArrayList<Listing> fetchListings(Context context){
        return null;
    }

    @Override
    public void onFragmentInteraction(Fragment fragment) { }
}
