package foosh.air.foi.hr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.MyProfileActivity;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.adapters.SlidingImageAdapter;
import foosh.air.foi.hr.helper.DialogFragmentManager;
import foosh.air.foi.hr.model.Listing;
import foosh.air.foi.hr.model.User;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class ListingDetailFragment extends Fragment {
    private String mListingId;
    private Listing mListing;
    private User mOwner;
    private FirebaseAuth mAuth;
    private Boolean authOwner;
    private DatabaseReference mListingReference;
    private DatabaseReference mOwnerReference;
    private DatabaseReference mUsersReference;
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
    private LinearLayout applicantsList;
    private TextView applicantsListTitle;
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
                goToProfile(v, mListing.getOwnerId());
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
        mUsersReference = FirebaseDatabase.getInstance().getReference().child("users");

        listingInterested = (Button) scrollView.findViewById(R.id.buttonInterested);

        // APPLICATION Controls
        buttonApply = (Button) scrollView.findViewById(R.id.buttonInterested);
        buttonUnapply = (Button) scrollView.findViewById(R.id.buttonUnapply);
        buttonMessage = (Button) scrollView.findViewById(R.id.buttonMessage);
        acceptDealQuestion = (JustifyTextView) scrollView.findViewById(R.id.acceptDealQuestion);
        buttonAcceptDeal = (Button) scrollView.findViewById(R.id.buttonAcceptDeal);
        buttonNotAcceptDeal = (Button) scrollView.findViewById(R.id.buttonNotAcceptDeal);
        applicantsList = (LinearLayout) scrollView.findViewById(R.id.applicantsList);
        applicantsListTitle = (TextView) scrollView.findViewById(R.id.applicantsListTitle);
        buttonFinishJob = (Button) scrollView.findViewById(R.id.buttonFinishJob);
        applicantNotAcceptedInfo = (JustifyTextView) scrollView.findViewById(R.id.applicantNotAccepted);

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
        buttonFinishJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFinishJobClickListener();
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
        HashMap currentApplicant = new HashMap();
        String userIsApplied = mListing.getApplications().get(userId);
        if(userIsApplied != null) {
            int applicantStatus = 2;
            if( mListing.getApplicant().size() != 0){
                currentApplicant = mListing.getApplicant();
                if(currentApplicant.get(userId) != null){
                    applicantStatus = mListing.getApplicant().get(userId);
                }

            }
            if(applicantStatus == 1) {
                buttonFinishJob.setVisibility(View.VISIBLE);
            } else {
                if(applicantStatus == 0) {
                    buttonAcceptDeal.setVisibility(View.VISIBLE);
                    buttonNotAcceptDeal.setVisibility(View.VISIBLE);
                    acceptDealQuestion.setVisibility(View.VISIBLE);
                } else {
                    if(!currentApplicant.containsValue(1)){
                        buttonUnapply.setVisibility(View.VISIBLE);
                        buttonMessage.setVisibility(View.VISIBLE);
                    }
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
                    applicantsListTitle.setVisibility(View.VISIBLE);
                    applicantsList.setVisibility(View.VISIBLE);
                    fetchApplicantList();
                }
            }
        }
    }

    private void fetchApplicantList() {
        final ArrayList<User> applicants = new ArrayList<User>();
        final LayoutInflater inflater = getLayoutInflater();
        Set<String> applicantsId = mListing.getApplications().keySet();
        applicantsList.removeAllViews();

        for (String id: applicantsId) {
            mUsersReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    View child = inflater.inflate(R.layout.fragment_listing_applicant, null);
                    TextView applicantName = (TextView)child.findViewById(R.id.applicantName);
                    CircleImageView profilePhoto = (CircleImageView) child.findViewById(R.id.applicantProfileImage);
                    applicantName.setText(user.getDisplayName());
                    if(user.getProfileImgPath() == null || user.getProfileImgPath().equals("")){
                        Picasso.get().load(R.drawable.avatar).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
                    }else{
                        Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
                    }

                    Button makeDeal = (Button)child.findViewById(R.id.buttonMakeDeal);
                    Button message = (Button)child.findViewById(R.id.buttonMessageApplicant);

                    makeDeal.setTag(dataSnapshot.getKey());
                    message.setTag(user.getContact());
                    profilePhoto.setTag(dataSnapshot.getKey());
                    applicantName.setTag(dataSnapshot.getKey());

                    makeDeal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String applicantId = (String) view.getTag();
                            mListingReference = FirebaseDatabase.getInstance().getReference().child("listings").child(mListingId);
                            mListingReference.child("applicant/"+ applicantId).setValue(0);
                            applicantsListTitle.setVisibility(View.GONE);
                        }
                    });
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String applicantContact = (String) view.getTag();
                            openAndroidDefaultSMSApp(applicantContact);
                        }
                    });

                    profilePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToProfile(view, view.getTag().toString());
                        }
                    });

                    applicantName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToProfile(view, view.getTag().toString());
                        }
                    });

                    applicantsList.addView(child);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void goToProfile(View view, String userId) {
        Log.d("ListingDetailFragment","Go to profile " + view.getTag());
        Intent intent = new Intent(scrollView.getContext(), MyProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("foosh.air.foi.hr.MyListingsFragment.fragment-key","listingOwnerProfile");
        intent.putExtra("userId", userId);
        startActivity(intent);
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

    private void openAndroidDefaultSMSApp(String contact){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", contact);
        smsIntent.putExtra("sms_body","");
        startActivity(smsIntent);
    }

    private void buttonApplyOnClickListener(){
        mListingReference.child("applications/"+ userId).setValue("test");
    }
    private void buttonUnapplyOnClickListener(){
        mListingReference.child("applications/"+ userId).setValue(null);
    }
    private void buttonMessageOnClickListener(){
        openAndroidDefaultSMSApp(mOwner.getContact());
    }
    private void buttonAcceptDealOnClickListener(){
        mListingReference.child("applicant/"+ userId).setValue(1);
    }
    private void buttonNotAcceptDealOnClickListener(){
        mListingReference.child("applicant/"+ userId).setValue(null);
    }
    private void buttonFinishJobClickListener(){
        if(authOwner){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            DialogFragmentManager.getInstance().addDialogFragment("qrbitmap", new QRDialogFragment());
            DialogFragmentManager.getInstance().getDialogFragment("qrbitmap").showFragment(fragmentManager, "qrdialog");
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            DialogFragmentManager.getInstance().addDialogFragment("qrscaner", new CameraDialogFragment());
            DialogFragmentManager.getInstance().getDialogFragment("qrscaner").showFragment(fragmentManager, "qrdialog");
        }

    }


}
