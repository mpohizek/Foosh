package foosh.air.foi.hr;

import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.model.User;
import foosh.air.foi.hr.model.onDataListener;

public class MyProfileActivity extends NavigationDrawerBaseActivity implements onDataListener {
    private FirebaseAuth mAuth;
    private User user;
    Object a;
    private Picasso imageLoader;
    private ConstraintLayout contentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        contentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_my_profile, contentLayout);
        contentLayout.setVisibility(ConstraintLayout.GONE);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference("users/" + mAuth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void updateUI(User updatedData) {

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
            locationIcon.setVisibility(View.INVISIBLE);
            location.setVisibility(View.INVISIBLE);
        }

        bio.setText(user.getBio());


        contentLayout.setVisibility(ConstraintLayout.VISIBLE);
    }
}
