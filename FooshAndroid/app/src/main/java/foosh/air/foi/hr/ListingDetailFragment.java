package foosh.air.foi.hr;


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
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

    private DatabaseReference mListingReference;
    private DatabaseReference mOwnerReference;
    ConstraintLayout contentLayout;

    private TextView listingTitle;
    private TextView listingCategory;
    private TextView listingDescription;
    //private List<ImageView> listingPictures;
    private TextView listingPrice;
    private TextView listingLocation;
    private TextView listingDate;
    private TextView userName;
    private CircleImageView userProfilePhoto;
    private RatingBar userRating;
    private FrameLayout imageLayout;
    
    private ArrayList<Drawable> listingFetchedImages;

    private static ViewPager mPager;

    public ListingDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mListingId = getArguments().getString("listingId");
        }
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.fragment_listing_detail, container, false);

        contentLayout = (ConstraintLayout) scrollView.findViewById(R.id.main_layout);
        //contentLayout = (ConstraintLayout) scrollView.findViewById(R.id.main_layout);
        mListingReference = FirebaseDatabase.getInstance().getReference().child("listings").child(mListingId);

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


        mListingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListing = dataSnapshot.getValue(Listing.class);
                showListingDetailData();
                mOwnerReference = FirebaseDatabase.getInstance().getReference().child("users").child(mListing.getOwnerId());
                mOwnerReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mOwner = dataSnapshot.getValue(User.class);
                        Log.d("listing owner",mOwner.getDisplayName());

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

        return scrollView;
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
        listingDescription.setText(mListing.getDescription());

        mPager.setAdapter(new SlidingImageAdapter(getActivity(), mListing.getImages()));

        listingPrice.setText(mListing.getPrice()+" kn");
        listingLocation.setText(mListing.getLocation());
    }

    //sets the owner display name, profile image and the owners rating based on the type of listing
    private void showListingOwnerData() {
        userName.setText(mOwner.getDisplayName());
        Picasso.get().load(mOwner.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(userProfilePhoto);
        if(mListing.isHiring()){
            userRating.setRating(mOwner.getRatingHired());
        }else{
            userRating.setRating(mOwner.getRatingEmployed());
        }
    }

}
