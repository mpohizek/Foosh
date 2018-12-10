package foosh.air.foi.hr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import foosh.air.foi.hr.fragments.MyAdsFragment;
import foosh.air.foi.hr.model.Ads;

public class MyAdActivity extends NavigationDrawerBaseActivity implements onAdsDelivered, MyAdsFragment.onFragmentInteractionListener {

    private ConstraintLayout contentLayout;

    public static final int id=1;
    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    //what happens with layout when selected tab changes
    private TabLayout mTabLayout;
    private Toolbar toolbar;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static int getId() {
        return id;
    }
    public static String getMenuTitle(){
        return "Moji oglasi";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_my_ad_base, contentLayout);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add(0, MenuItem_FilterAds, 0, "Filter");
        item1.setIcon(R.drawable.ic_filter_list_white_24dp);
        item1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem item2 = menu.add(0, MenuItem_ExpandOpt, 1, "More");
        item2.setIcon(R.drawable.ic_more_vert_white_24dp);
        item2.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MenuItem_FilterAds:
                //
                return true;
            case MenuItem_ExpandOpt:
                //
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return false;
        }
    }

    @Override
    public ArrayList<Ads> fetchAds(Context context){
        return null;
    }

    @Override
    public void onFragmentInteraction(Fragment fragment) { }
}
