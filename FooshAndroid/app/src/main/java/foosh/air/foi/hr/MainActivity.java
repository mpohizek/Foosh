package foosh.air.foi.hr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import foosh.air.foi.hr.adapters.MainFeedPagerAdapter;
import foosh.air.foi.hr.fragments.MainFeedFragment;

public class MainActivity extends NavigationDrawerBaseActivity implements MainFeedFragment.onFragmentInteractionListener {

    private ConstraintLayout contentLayout;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=0;
    private static final int RC_MAIN = 1001;
    private FirebaseAuth mAuth;

    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private MainFeedPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    //what happens with layout when selected tab changes
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private AppBarLayout appBarLayoutMain;
    private SearchView searchView;

    public static String getMenuTitle(){
        return "Početna stranica";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        CoordinatorLayout coordinatorLayout = getLayoutInflater().inflate(R.layout.activity_main_feed, contentLayout).findViewById(R.id.main_content_main_feed);



        appBarLayoutMain = findViewById(R.id.id_appbar_main);
        appBarLayoutMain.setVisibility(View.GONE);

        toolbar = findViewById(R.id.id_toolbar_main_feed);
        setSupportActionBar(toolbar);

        searchView = new SearchView(this);
        toolbar.addView(searchView);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);


        mPagerAdapter = new MainFeedPagerAdapter(getSupportFragmentManager());
        mViewPager = contentLayout.findViewById(R.id.main_feed_viewpager);

        mTabLayout = contentLayout.findViewById(R.id.id_tabs_main_feed);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

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
                drawerLayout.openDrawer(GravityCompat.END);
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
    public void onFragmentInteraction(Fragment fragment) {

    }
}
