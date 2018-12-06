package foosh.air.foi.hr;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.adapters.SlidingImageAdapter;
import foosh.air.foi.hr.model.Listing;
import foosh.air.foi.hr.model.User;

public class ListingDetailFragment extends Fragment {

    private String mListingId;
    private Listing mListing;
    private User mOwner;
    private FirebaseAuth mAuth;
    private Boolean authOwner; //true if owner and auth user are the same person

    private DatabaseReference mListingReference;
    private DatabaseReference mOwnerReference;
    ConstraintLayout contentLayout;

    private ScrollView scrollView;
    private TextView listingTitle;
    private TextView listingCategory;
    private TextView listingDescription;
    private TextView listingPrice;
    private TextView listingLocation;
    private TextView listingDate;
    private TextView userName;
    private CircleImageView userProfilePhoto;
    private RatingBar userRating;
    private Button listingInterested;
    private TextView behindImages;

    private static ViewPager mPager;

    public ListingDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mListingId = getArguments().getString("listingId");
        }

        init(inflater, container);

        //fetch listing data
        mListingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListing = dataSnapshot.getValue(Listing.class);

                if (mAuth.getCurrentUser().getUid().equals(mListing.getOwnerId())){
                    listingInterested.setText("Uredi oglas");
                    authOwner = true;
                }
                listingInterested.setEnabled(true);

                showListingDetailData();

                //fetch listing owner data
                mOwnerReference = FirebaseDatabase.getInstance().getReference().child("users").child(mListing.getOwnerId());
                mOwnerReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mOwner = dataSnapshot.getValue(User.class);
                        showListingOwnerData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listingInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authOwner){
                    //TODO: link to the edit listing fragment
                    Log.d("ListingDetailFragment","Go to edit listing");
                }else{
                    //TODO: set the status for interested
                    //TODO: if the user already clicked and the status is interested?
                    Log.d("ListingDetailFragment","Set to interested");
                }
            }
        });

        View.OnClickListener profileOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListingDetailFragment","Go to profile " + mListing.getOwnerId());
                Intent intent = new Intent(scrollView.getContext(), MyProfileActivity.class);
                //TODO: sync with the MyProfileActivity tag
                intent.putExtra("userId", mListing.getOwnerId());
                startActivity(intent);
            }
        };
        userProfilePhoto.setOnClickListener(profileOnClickListener);
        userName.setOnClickListener(profileOnClickListener);

        return scrollView;
    }

    private void init(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        scrollView = (ScrollView) inflater.inflate(R.layout.fragment_listing_detail, container, false);

        mAuth = FirebaseAuth.getInstance();
        authOwner = false;
        contentLayout = (ConstraintLayout) scrollView.findViewById(R.id.main_layout);
        mListingReference = FirebaseDatabase.getInstance().getReference().child("listings").child(mListingId);

        listingInterested = (Button) scrollView.findViewById(R.id.buttonInterested);
        listingInterested.setEnabled(false);

        listingTitle = (TextView) scrollView.findViewById(R.id.listingTitle);
        listingCategory = (TextView) scrollView.findViewById(R.id.listingCategory);
        listingDescription = (TextView) scrollView.findViewById(R.id.listingDescription);
        mPager = (ViewPager) scrollView.findViewById(R.id.viewpager);
        listingPrice = (TextView) scrollView.findViewById(R.id.ListingPrice);
        listingLocation = (TextView) scrollView.findViewById(R.id.listingLocation);
        listingDate = (TextView) scrollView.findViewById(R.id.listingDate);
        userName = (TextView) scrollView.findViewById(R.id.listingOwner);
        userProfilePhoto = (CircleImageView) scrollView.findViewById(R.id.userProfileImage);
        userRating = (RatingBar) scrollView.findViewById(R.id.ratingHired);
        behindImages = (TextView) scrollView.findViewById(R.id.behindImages);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showListingDetailData() {
        listingTitle.setText(mListing.getTitle());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
            Date d = sdf.parse(mListing.getDateCreated());
            sdf.applyPattern("dd.MM.yyyy.");
            Log.d("datumm",sdf.format(d));
            listingDate.setText(sdf.format(d));
        } catch (ParseException e) {
            listingDate.setText(mListing.getDateCreated());
            e.printStackTrace();
        }
        listingCategory.setText(mListing.getCategory());
        listingDescription.setText(mListing.getDescription()+"\n");

        if(mListing.getImages() == null){
            behindImages.setText("Vlasnik oglasa nije dodao sliku");
        }else{
            mPager.setAdapter(new SlidingImageAdapter(scrollView.getContext(), mListing.getImages()));
        }

        listingPrice.setText(mListing.getPrice()+" kn");
        listingLocation.setText(mListing.getLocation());
    }

    //sets the owner display name, profile image and the owners rating based on the type of listing
    private void showListingOwnerData() {
        userName.setText(mOwner.getDisplayName());
        Picasso.get().load((mOwner.getProfileImgPath()).equals("")?null:mOwner.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(userProfilePhoto);
        if(mListing.isHiring()){
            userRating.setRating(mOwner.getRatingHired());
        }else{
            userRating.setRating(mOwner.getRatingEmployed());
        }
    }

}
