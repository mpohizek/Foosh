package foosh.air.foi.hr;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.Collection;

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
        TextView displayName = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userDisplayName);
        TextView email = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userEmail);
        TextView bio = (TextView) contentLayout.findViewById(R.id.linearLayout4).findViewById(R.id.userAboutMe);
        CircleImageView profilePhoto = (CircleImageView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userProfileImage);
        displayName.setText(user.getDisplayName());
        email.setText(user.getEmail());
        bio.setText(user.getBio());
        Picasso.get().load(user.getImageUrl()).into(profilePhoto);
        //profilePhoto.setdra
        contentLayout.setVisibility(ConstraintLayout.VISIBLE);
    }
}
