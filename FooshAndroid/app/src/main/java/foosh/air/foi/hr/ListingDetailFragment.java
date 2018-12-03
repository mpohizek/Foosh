package foosh.air.foi.hr;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private List<ImageView> listingPictures;
    private TextView listingPrice;
    private TextView listingLocation;
    private TextView listingDate;
    private TextView userName;
    private CircleImageView userProfilePhoto;
    private RatingBar userRating;
    private FrameLayout imageLayout;

    public ListingDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mListingId = getArguments().getString("listingId");
        }
        return inflater.inflate(R.layout.fragment_listing_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentLayout = (ConstraintLayout) getActivity().findViewById(R.id.main_layout);
        mListingReference = FirebaseDatabase.getInstance().getReference().child("listings").child(mListingId);

        listingTitle = (TextView) getActivity().findViewById(R.id.listingTitle);
        //TODO: list of categories
        //listingCategory = findViewById(R.id.listingCategory);
        listingDescription = (TextView) getActivity().findViewById(R.id.listingDescription);

        //TODO list of pictures
        imageLayout = (FrameLayout) getActivity().findViewById(R.id.imageLayout);

        
        listingPrice = (TextView) getActivity().findViewById(R.id.ListingPrice);
        listingLocation = (TextView) getActivity().findViewById(R.id.listingLocation);
        listingDate = (TextView) getActivity().findViewById(R.id.listingDate);
        userName = (TextView) getActivity().findViewById(R.id.listingOwner);
        userProfilePhoto = (CircleImageView) getActivity().findViewById(R.id.userProfileImage);
        userRating = (RatingBar) getActivity().findViewById(R.id.ratingHired);


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
    }


    private void showListingDetailData() {
        listingTitle.setText(mListing.getTitle());
        //TODO: list of categories
        //listingCategory.setText(listing.getCategory());
        listingDescription.setText(mListing.getDescription());
        //TODO: list of pictures
        //
        listingPrice.setText(mListing.getPrice()+" kn");
        listingLocation.setText(mListing.getLocation());
        listingDate.setText(mListing.getDateCreated());
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
