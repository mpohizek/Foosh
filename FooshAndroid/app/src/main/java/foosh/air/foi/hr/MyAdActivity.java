package foosh.air.foi.hr;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import foosh.air.foi.hr.fragments.MyAdsFragment;
import foosh.air.foi.hr.model.Ads;
import foosh.air.foi.hr.model.AdsManager;

public class MyAdActivity extends NavigationDrawerBaseActivity implements onAdsDelivered, MyAdsFragment.OnFragmentInteractionListener {

    private ConstraintLayout contentLayout;

    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private AdsManager mAdsManager;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    //what happens with layout when selected tab changes
    private TabLayout mTabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_my_ad_base, contentLayout);

        toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionbar.setTitle("Moji oglasi");

        mAdsManager = new AdsManager(this);
        mAdsManager.setUpDummyData();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = contentLayout.findViewById(R.id.id_viewpager);

        mTabLayout = contentLayout.findViewById(R.id.id_tabs);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add(0, MenuItem_FilterAds, 0, "Filter");
        item1.setIcon(R.drawable.ic_filter_list_black_24dp);
        item1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem item2 = menu.add(0, MenuItem_ExpandOpt, 1, "More");
        item2.setIcon(R.drawable.ic_more_vert_black_24dp);
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

            default:
                return false;
        }
    }

    @Override
    public void fetchAds(Ads newData) {
        return;
    }

    @Override
    public AdsManager onFragmentInteraction(Fragment fragment) {
        return mAdsManager;
    }
}
