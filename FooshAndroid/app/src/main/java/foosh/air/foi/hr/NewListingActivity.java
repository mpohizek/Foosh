package foosh.air.foi.hr;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;

public class NewListingActivity extends NavigationDrawerBaseActivity{

    private ConstraintLayout contentLayout;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=2;
    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    public static String getMenuTitle(){
        return "Dodaj oglas";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.fragment_listing_new, contentLayout);
    }
}
