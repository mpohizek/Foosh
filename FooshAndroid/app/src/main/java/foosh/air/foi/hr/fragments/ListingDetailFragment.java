package foosh.air.foi.hr.fragments;


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
import android.widget.ImageView;
import android.widget.ListView;
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
import foosh.air.foi.hr.MyProfileActivity;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.adapters.SlidingImageAdapter;
import foosh.air.foi.hr.model.Listing;
import foosh.air.foi.hr.model.User;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class ListingDetailFragment extends Fragment {

    private String mListingId;
    private Listing mListing;
    private User mOwner;
    private FirebaseAuth mAuth;
    private Boolean authOwner; //true if owner and auth user are the same person

    private DatabaseReference mListingReference;
    private DatabaseReference mOwnerReference;
    ConstraintLayout contentLayout;
    String userId;
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
    private Button buttonUnapply;
    private Button buttonMessage;
    private JustifyTextView acceptDealQuestion;
    private Button buttonAcceptDeal;
    private Button buttonNotAcceptDeal;
    private ListView applicantsList;
    private Button buttonFinishJob;
    private Button buttonApply;
    private JustifyTextView applicantNotAcceptedInfo;
    private ImageView listingsQrCode;

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
                userId = mAuth.getCurrentUser().getUid();
                if (mAuth.getCurrentUser().getUid().equals(mListing.getOwnerId())){
                    listingInterested.setText("Uredi oglas");
                    authOwner = true;
                    hideAllControls();
                    showControlsOwner();

                } else {
                    hideAllControls();
                    showControlsApplicant();
                }
                //listingInterested.setEnabled(true);


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


        View.OnClickListener profileOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListingDetailFragment","Go to profile " + mListing.getOwnerId());
                Intent intent = new Intent(scrollView.getContext(), MyProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("foosh.air.foi.hr.MyListingsFragment.fragment-key","listingOwnerProfile");
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
        mListingReference = FirebaseDatabase.getInstance().getReference().child("listings-borna").child(mListingId);

        listingInterested = (Button) scrollView.findViewById(R.id.buttonInterested);
        //listingInterested.setEnabled(false);

        // APPLICATION Controls
        buttonApply = (Button) scrollView.findViewById(R.id.buttonInterested);
        buttonUnapply = (Button) scrollView.findViewById(R.id.buttonUnapply);
        buttonMessage = (Button) scrollView.findViewById(R.id.buttonMessage);
        acceptDealQuestion = (JustifyTextView) scrollView.findViewById(R.id.acceptDealQuestion);
        buttonAcceptDeal = (Button) scrollView.findViewById(R.id.buttonAcceptDeal);
        buttonNotAcceptDeal = (Button) scrollView.findViewById(R.id.buttonNotAcceptDeal);
        applicantsList = (ListView) scrollView.findViewById(R.id.applicantsList);
        buttonFinishJob = (Button) scrollView.findViewById(R.id.buttonFinishJob);
        applicantNotAcceptedInfo = (JustifyTextView) scrollView.findViewById(R.id.applicantNotAccepted);
        listingsQrCode = (ImageView) scrollView.findViewById(R.id.listingQrCode);

        // SET onClick listeners
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonApplyOnClickListener();
            }
        });
        buttonUnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUnapplyOnClickListener();
            }
        });
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMessageOnClickListener();
            }
        });
        buttonAcceptDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAcceptDealOnClickListener();
            }
        });
        buttonNotAcceptDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNotAcceptDealOnClickListener();
            }
        });


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

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
            Date d = sdf.parse(mListing.getDateCreated());
            sdf.applyPattern("dd.MM.yyyy.");
            listingDate.setText(sdf.format(d));
        }catch (Exception ex){
            Log.e("ListingDetailFragment",ex.toString());
            listingDate.setText(mListing.getDateCreated());
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


    private void showControlsApplicant(){
        String userIsApplied = mListing.getApplications().get(userId);
        if(userIsApplied != null) {
            int applicantStatus = 2;
            if( mListing.getApplicant().size() != 0){
                applicantStatus = mListing.getApplicant().get(userId);
            }
            if(applicantStatus == 1) {
                buttonFinishJob.setVisibility(View.VISIBLE);
            } else {
                if(applicantStatus == 0) {
                    buttonAcceptDeal.setVisibility(View.VISIBLE);
                    buttonNotAcceptDeal.setVisibility(View.VISIBLE);
                    acceptDealQuestion.setVisibility(View.VISIBLE);
                } else {
                    buttonUnapply.setVisibility(View.VISIBLE);
                    buttonMessage.setVisibility(View.VISIBLE);
                }
            }

        } else {
            buttonApply.setVisibility(View.VISIBLE);
        }



    }

    private void showControlsOwner(){

        if(mListing.getApplications() != null){
            if(mListing.getApplicant().containsValue(1)) {
                listingsQrCode.setVisibility(View.VISIBLE);
            }else {
                if(mListing.getApplicant().containsValue(0)) {
                    applicantNotAcceptedInfo.setVisibility(View.VISIBLE);
                } else {
                    applicantsList.setVisibility(View.VISIBLE);
                }
            }


        }
    }

    public void hideAllControls(){
        buttonApply.setVisibility(View.GONE);
        buttonUnapply.setVisibility(View.GONE);
        buttonMessage.setVisibility(View.GONE);
        acceptDealQuestion.setVisibility(View.GONE);
        buttonAcceptDeal.setVisibility(View.GONE);
        buttonNotAcceptDeal.setVisibility(View.GONE);
        applicantsList.setVisibility(View.GONE);
        buttonFinishJob.setVisibility(View.GONE);
        applicantNotAcceptedInfo.setVisibility(View.GONE);
    }

    private void buttonApplyOnClickListener(){
        mListingReference.child("applications/"+ userId).setValue("test");
    }
    private void buttonUnapplyOnClickListener(){
        mListingReference.child("applications/"+ userId).setValue(null);
    }
    private void buttonMessageOnClickListener(){

    }
    private void buttonAcceptDealOnClickListener(){
        mListingReference.child("applicant/"+ userId).setValue(1);
    }
    private void buttonNotAcceptDealOnClickListener(){
        mListingReference.child("applicant/"+ userId).setValue(null);
    }

}
