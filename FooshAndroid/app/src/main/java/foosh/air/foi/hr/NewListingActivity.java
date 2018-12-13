package foosh.air.foi.hr;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Locale;

import foosh.air.foi.hr.model.Category;

public class NewListingActivity extends NavigationDrawerBaseActivity{

    private ConstraintLayout contentLayout;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=2;
    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private Button buttonAddNewListing;
    private boolean hiring;
    private String listingTitle;
    private Category listingCategory;
    private String listingDescription;
    private List<String> ImgPaths;
    private float price;
    private String location;

    public static String getMenuTitle(){
        return "Dodaj oglas";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.fragment_listing_new, contentLayout);

        buttonAddNewListing = contentLayout.findViewById(R.id.buttonAddListing);
        buttonAddNewListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
