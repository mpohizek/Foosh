package foosh.air.foi.hr.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.User;

public class MyProfileViewFragment extends Fragment {

    private FirebaseAuth mAuth;
    private User user;

    private Picasso imageLoader;
    private ConstraintLayout contentLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentLayout = (ConstraintLayout) container;



        return inflater.inflate(R.layout.fragment_my_profile_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        //Fetching the user data
        DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference("users/" + mAuth.getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

             try {
                 showData(dataSnapshot);
             }catch (Exception e){
                Exception a = e;
             }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Fetching the reviews data
        DatabaseReference reviewsRef;
        reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");

        Query reviewsQuery = reviewsRef.orderByChild("aboutUser").equalTo(mAuth.getCurrentUser().getUid());
        reviewsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showReviewData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

        user = dataSnapshot.getValue(User.class);

        CircleImageView profilePhoto = (CircleImageView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userProfileImage);
        TextView displayName = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userDisplayName);
        TextView email = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userEmail);
        ImageView locationIcon = (ImageView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.locationIcon);
        TextView location = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userLocationName);
        TextView bio = (TextView) contentLayout.findViewById(R.id.linearLayout4).findViewById(R.id.userAboutMe);


        Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
        displayName.setText(user.getDisplayName());
        email.setText(user.getEmail());

        //Hiding the location icon and text if the user doesn't have a location specified in the database
        if(user.getLocation() != null){
            locationIcon.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
            location.setText(user.getLocation());
        }else{
            locationIcon.setVisibility(View.GONE);
            location.setVisibility(View.GONE);
        }

        bio.setText(user.getBio());

        contentLayout.setVisibility(ConstraintLayout.VISIBLE);

        TextView editLink = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.editProfileLink);
        editLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openEditMyProfileFragment();
            }
        });
    }

    private void showReviewData(DataSnapshot dataSnapshot) {
        int numHired = 0, numEmployed = 0;
        int numHiredPeople = 0, numPeopleEmployed = 0;
        float sumHired = 0, sumEmployed = 0;
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            float rating = ds.child("rating").getValue(float.class);
            if(ds.child("hired").getValue(boolean.class)){
                sumHired = sumHired + rating;
                numHired++;
            }else{
                sumEmployed = sumEmployed + rating;
                numEmployed++;
            }
        }

        RatingBar ratingHired = (RatingBar) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.reviewsCard).findViewById(R.id.ratingHired);
        ratingHired.setRating(sumHired/numHired);
        RatingBar ratingEmployed = (RatingBar) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.reviewsCard).findViewById(R.id.ratingEmployed);
        ratingEmployed.setRating(sumEmployed/numEmployed);

        //TODO: switch - posao, poslova...
        String employedNumJobsText = " poslova";
        String employedNumPeopleText = " zaposlenih osoba";
        String hiredNumJobsText = " poslova izvršeno";
        String hiredNumPeopleText = " poslodavaca";
        TextView hiredNumJobs = (TextView) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.reviewsCard).findViewById(R.id.hiredNumJobs);
        TextView hiredNumPeople = (TextView) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.reviewsCard).findViewById(R.id.hiredNumPeople);
        TextView employedNumJobs = (TextView) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.reviewsCard).findViewById(R.id.employedNumJobs);
        TextView employedNumPeople = (TextView) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.reviewsCard).findViewById(R.id.employedNumPeople);

        hiredNumJobs.setText(numHired + hiredNumJobsText);
        //TODO: change to the number of people employed
        hiredNumPeople.setText(numHired + hiredNumPeopleText);

        employedNumJobs.setText(numEmployed + employedNumJobsText);
        //TODO: change to the number of people who hired the user
        employedNumPeople.setText(numEmployed + employedNumPeopleText);

    }

    private void openEditMyProfileFragment(){

        Bundle userDataBundle = new Bundle();
        userDataBundle.putString("ProfileImgPath",user.getProfileImgPath());
        userDataBundle.putString("DisplayName",user.getDisplayName());
        userDataBundle.putString("Location",user.getLocation());
        userDataBundle.putString("Bio",user.getBio());

        EditMyProfileFragment mEditMyProfileFragment = new EditMyProfileFragment();
        mEditMyProfileFragment.setArguments(userDataBundle);

        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.main_layout, mEditMyProfileFragment )
                .addToBackStack("profileEdit")
                .commit();
    }

}
