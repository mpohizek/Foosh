package foosh.air.foi.hr;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import foosh.air.foi.hr.model.Listing;
import foosh.air.foi.hr.model.User;

public class NewListingActivity extends NavigationDrawerBaseActivity{

    private ConstraintLayout contentLayout;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=2;
    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TextInputEditText listingTitle;
    private TextInputEditText listingDescription;
    private TextInputEditText listingPrice;
    private TextInputEditText listingLocation;

    private Button buttonAddNewListing;
    private Button buttonPayingForService;
    private Button buttonIWantToEarn;
    private Spinner categoriesSpinner;

    private Button buttonAddImageFromGallery;
    private Button buttonTakePhoto;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mUserId;


    private Listing listing = new Listing();

    public static String getMenuTitle(){
        return "Dodaj oglas";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_listing_new, contentLayout);

        init();

        buttonAddImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonAddNewListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listingTitle.getText().length()==0
                        || listingDescription.getText().length()==0
                        || listingPrice.getText().length()==0
                        || listingLocation.getText().length()==0) {
                    Toast.makeText(NewListingActivity.this, "Nisu uneseni svi potrebni podaci!", Toast.LENGTH_LONG).show();
                }
                else {
                    listing.setTitle(listingTitle.getText().toString());
                    listing.setDescription(listingDescription.getText().toString());
                    listing.setPrice(Integer.parseInt(listingPrice.getText().toString()));
                    listing.setLocation(listingLocation.getText().toString());

                    listing.setOwnerId(FirebaseAuth.getInstance().getUid());

                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();

                    listing.setDateCreated(currentDate.toString());

                    createFirebaseListings(listing);

                    Toast.makeText(NewListingActivity.this, "Uspješno kreiran oglas", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

       buttonPayingForService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonPayingForService.setBackgroundColor(Color.rgb(114, 79, 175));
                buttonIWantToEarn.setBackgroundColor(Color.rgb(132, 146, 166));
                listing.setHiring(false);
            }
        });

        buttonIWantToEarn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonIWantToEarn.setBackgroundColor(Color.rgb(114, 79, 175));
                buttonPayingForService.setBackgroundColor(Color.rgb(132, 146, 166));
                listing.setHiring(true);
            }
        });


        final List<String> categories = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("categorys");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                        categories.add(item.getKey().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewListingActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoriesSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemValue = adapterView.getItemAtPosition(i).toString();
                listing.setCategory(itemValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void init() {
        toolbar = findViewById(R.id.id_toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        listingTitle = findViewById(R.id.listingTitle);
        listingDescription = findViewById(R.id.ListingDescription);
        listingPrice = findViewById(R.id.ListingPrice);
        listingLocation = findViewById(R.id.listingLocation);

        buttonAddNewListing = contentLayout.findViewById(R.id.buttonAddListing);
        buttonPayingForService = contentLayout.findViewById(R.id.buttonPaying);
        buttonIWantToEarn = contentLayout.findViewById(R.id.buttonEarning);

        categoriesSpinner = contentLayout.findViewById(R.id.spinner_categories);

        buttonAddImageFromGallery = contentLayout.findViewById(R.id.buttonAddImagesFromGalery);
        buttonTakePhoto = contentLayout.findViewById(R.id.buttonAddImagesCamera);
    }

    public void createFirebaseListings(Listing listing){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("listings");
        String key = databaseReference.push().getKey();
        listing.setId(key);
        databaseReference.child(key).setValue(listing);

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
}
