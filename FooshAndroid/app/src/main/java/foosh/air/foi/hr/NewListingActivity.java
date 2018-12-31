package foosh.air.foi.hr;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import foosh.air.foi.hr.adapters.ImagesRecyclerViewAdapter;
import foosh.air.foi.hr.adapters.MyListingsRecyclerViewAdapter;
import foosh.air.foi.hr.helper.RecyclerItemTouchHelper;
import foosh.air.foi.hr.model.Listing;

public class NewListingActivity extends NavigationDrawerBaseActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private ConstraintLayout contentLayout;

    //used in the NavigationDrawerBaseActivity for the menu item id
    public static final int id=2;
    private final int PICK_IMAGES_FOR_LISTING = 1500;
    private final int REQUEST_IMAGE_CAPTURE = 1501;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private final int NUMBER_OF_IMAGES = 20;
    private final int MenuItem_FilterAds = 0, MenuItem_ExpandOpt = 1;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private TextInputEditText listingTitle;
    private TextInputEditText listingDescription;
    private TextInputEditText listingPrice;
    private TextInputEditText listingLocation;

    private Button buttonAddNewListing;
    private Button buttonPayingForService;
    private Button buttonIWantToEarn;
    private Spinner categoriesSpinner;

    private AppCompatImageView appCompatImageViewLibrary;
    private AppCompatImageView appCompatImageViewCamera;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mUserId;

    private ImagesRecyclerViewAdapter imagesRecyclerViewAdapter;

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

        appCompatImageViewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canAddListingImageBefore()){
                    Toast.makeText(NewListingActivity.this, "No more than " + NUMBER_OF_IMAGES +
                            " images can be added!", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Images"), PICK_IMAGES_FOR_LISTING);
            }
        });
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            appCompatImageViewCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!canAddListingImageBefore()){
                        Toast.makeText(NewListingActivity.this, "No more than " + NUMBER_OF_IMAGES +
                                " images can be added!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
        else{
            appCompatImageViewCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NewListingActivity.this, "Camera not found!", Toast.LENGTH_LONG).show();
                }
            });
        }

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

        appCompatImageViewLibrary = contentLayout.findViewById(R.id.appCompatImageViewLibrary);
        appCompatImageViewCamera = contentLayout.findViewById(R.id.appCompatImageViewCamera);

        imagesRecyclerViewAdapter = new ImagesRecyclerViewAdapter(this);

        recyclerView = contentLayout.findViewById(R.id.id_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(imagesRecyclerViewAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.DOWN, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_FOR_LISTING && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.getData() != null){
                    if (!canAddListingImageAfter(1)){
                        Toast.makeText(NewListingActivity.this, "No more than " + NUMBER_OF_IMAGES +
                                " images can be added!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    imagesRecyclerViewAdapter.addImageToDataset(data.getData());
                }
                else{
                    List<Object> imagesList = new ArrayList<>();
                    ClipData mClipData = data.getClipData();
                    if (!canAddListingImageAfter(mClipData.getItemCount())){
                        Toast.makeText(NewListingActivity.this, "No more than " + NUMBER_OF_IMAGES +
                                " images can be added!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (int i=0;i<mClipData.getItemCount();i++){
                        imagesList.add(mClipData.getItemAt(i).getUri());
                    }
                    imagesRecyclerViewAdapter.addImagesToDataset(imagesList);
                }
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            if (data != null && data.getExtras() != null) {
                if (!canAddListingImageAfter(1)){
                    Toast.makeText(NewListingActivity.this, "No more than " + NUMBER_OF_IMAGES +
                            " images can be added!", Toast.LENGTH_LONG).show();
                    return;
                }
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imagesRecyclerViewAdapter.addImageToDataset(imageBitmap);
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ImagesRecyclerViewAdapter.MyViewHolder) {
            String name = "Undo removed image";

            final Object deletedItem = imagesRecyclerViewAdapter.getmDataset().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            imagesRecyclerViewAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar
                    .make(contentLayout, name, Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagesRecyclerViewAdapter.restoreItem(deletedItem, deletedIndex);
                    recyclerView.getLayoutManager().scrollToPosition(deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean canAddListingImageBefore(){
        return imagesRecyclerViewAdapter.getmDataset().size() < NUMBER_OF_IMAGES;
    }

    private boolean canAddListingImageAfter(int plusNumberOfImages){
        return imagesRecyclerViewAdapter.getmDataset().size() + plusNumberOfImages <= NUMBER_OF_IMAGES;
    }
}