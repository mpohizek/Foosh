package foosh.air.foi.hr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private DatabaseReference mDatabaseCategorys;
    private DatabaseReference mDatabaseCities;

    private List<String> categories = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private List<String> sort = new ArrayList<>();

    public static String getMenuTitle(){
        return "Početna stranica";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        CoordinatorLayout coordinatorLayout = getLayoutInflater().inflate(R.layout.activity_main_feed, contentLayout).findViewById(R.id.main_content_main_feed);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END);

        appBarLayoutMain = findViewById(R.id.id_appbar_main);
        appBarLayoutMain.setVisibility(View.GONE);

        toolbar = findViewById(R.id.id_toolbar_main_feed);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.search_view);

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

        sort.add("Odabir sortiranja");
        sort.add("Cijena ASC");
        sort.add("Cijena DSC");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.spinner_item, sort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((AppCompatSpinner)navigationViewFilter.findViewById(R.id.spinner_sort_by)).setAdapter(adapter);

        mDatabaseCategorys = FirebaseDatabase.getInstance().getReference().child("categorys");
        mDatabaseCities = FirebaseDatabase.getInstance().getReference().child("cities");

        mDatabaseCategorys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.add("Odaberite kategoriju");
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    categories.add(item.getKey());
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(MainActivity.this,
                        R.layout.spinner_item, categories);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((AppCompatSpinner)navigationViewFilter.findViewById(R.id.spinner_categories)).setAdapter(adapter1);
                ((AppCompatSpinner)navigationViewFilter.findViewById(R.id.spinner_categories)).setSelection(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseCities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item: dataSnapshot.getChildren()){
                    cities.add(item.getKey());
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, cities);
                ((AutoCompleteTextView)navigationViewFilter.findViewById(R.id.cities_autocomplete)).setAdapter(adapter2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((AutoCompleteTextView)navigationViewFilter.findViewById(R.id.cities_autocomplete)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        navigationViewFilter.findViewById(R.id.remove_autocomplete_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AutoCompleteTextView)navigationViewFilter.findViewById(R.id.cities_autocomplete)).setText("");
            }
        });
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("user_" + mAuth.getUid());

        navigationViewFilter.findViewById(R.id.clearFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextInputEditText)navigationViewFilter.findViewById(R.id.minPriceFilter)).setText("");
                ((TextInputEditText)navigationViewFilter.findViewById(R.id.maxPriceFilter)).setText("");
            }
        });
        navigationViewFilter.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPagerAdapter.getFragment(mViewPager.getCurrentItem()).onDataDelivered();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPagerAdapter.getFragment(mViewPager.getCurrentItem()).onDataDelivered();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add(0, MenuItem_FilterAds, 0, "Filter");
        item1.setIcon(R.drawable.ic_filter_list_white_24dp);
        item1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MenuItem_FilterAds:
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return false;
        }
    }

    @Override
    public NavigationView getNavigationView() {
        return navigationViewFilter;
    }

    @Override
    public void getHashMapValues(Map<String, String> hashMap){
        String sortiranje = ((AppCompatSpinner)navigationViewFilter.findViewById(R.id.spinner_sort_by)).getSelectedItem().toString();
        if (sort.indexOf(sortiranje) != 0){
            hashMap.put("orderBy", sortiranje);
        }

        Object kategorija = ((AppCompatSpinner)navigationViewFilter.findViewById(R.id.spinner_categories)).getSelectedItem();
        if (kategorija != null && !kategorija.equals("Odaberite kategoriju")){
            hashMap.put("category", (String) kategorija);
        }

        String grad = ((AutoCompleteTextView)navigationViewFilter.findViewById(R.id.cities_autocomplete)).getText().toString();
        if (grad.length() != 0){
            hashMap.put("location", grad);
        }

        String minPriceBound = (((TextInputEditText)navigationViewFilter.findViewById(R.id.minPriceFilter)).getText().toString());
        if (minPriceBound.length() != 0){
            hashMap.put("priceLowBound", minPriceBound);
        }
        else{
            hashMap.put("priceLowBound", "0");
        }

        String maxPriceBound = (((TextInputEditText)navigationViewFilter.findViewById(R.id.maxPriceFilter)).getText().toString());
        if (maxPriceBound.length() != 0){
            hashMap.put("priceUpBound", maxPriceBound);
        }
        else{
            hashMap.put("priceUpBound", "100000000");
        }

        hashMap.put("textSearch", searchView.getQuery().toString());
    }
}