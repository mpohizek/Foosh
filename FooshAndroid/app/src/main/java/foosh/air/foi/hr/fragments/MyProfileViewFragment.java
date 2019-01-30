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
import android.widget.Button;
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

/**
 * Fragment za prikaz profila korisnika
 * Koristi se kod klika na profil korisnika u prikazu oglasa i kod prikaza profila trenutnog korisnika
 */
public class MyProfileViewFragment extends Fragment {

    private User user;
    private String mUserId;
    private Boolean listingOwner;

    private ConstraintLayout contentLayout;

    /**
     * Dohvaćanje podataka korisnika ovisno o tome jesu li fragmentu poslani agrumenti
     * U slučaju da se profil želi prikazati iz prikaza oglasa fragmentu će biti prosljeđeni argumenti
     * ako korisnik klikne na svoj profil u menu-u aplikacije fragment neće primiti argumente.
     * U slučaju da su argumenti prosljeđeni određuje se je li korisnik vlasnik oglasa ili ne
     * U slučaju da argumenti nisu poslani dohvaća se ID trenutnog korisnika
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentLayout = (ConstraintLayout) container;


        if (getArguments() != null) {
            mUserId = getArguments().getString("userId");
            if (mUserId.equals(FirebaseAuth.getInstance().getUid())){
                listingOwner = false;
            }
            else{
                listingOwner = true;
            }
        }
        else{
            mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            listingOwner = false;
        }



        //Fetching the user data
        DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference("users/" + mUserId);

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





        return inflater.inflate(R.layout.fragment_my_profile_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    /**
     * Prikaz podataka profila
     * Ako se radi o profilu trenutnog korisnika prikazati će se gumb za uređivanje profila
     * @param dataSnapshot
     */
    private void showData(DataSnapshot dataSnapshot) {

        user = dataSnapshot.getValue(User.class);

        CircleImageView profilePhoto = (CircleImageView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userProfileImage);
        TextView displayName = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userDisplayName);
        TextView email = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userEmail);
        ImageView locationIcon = (ImageView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.locationIcon);
        TextView location = (TextView) contentLayout.findViewById(R.id.linearLayout).findViewById(R.id.userLocationName);
        TextView bio = (TextView) contentLayout.findViewById(R.id.linearLayout4).findViewById(R.id.userAboutMe);
        TextView contact = (TextView) contentLayout.findViewById(R.id.linearLayout4).findViewById(R.id.userContact);
        if(user.getProfileImgPath() == null || user.getProfileImgPath().equals("")){
            Picasso.get().load(R.drawable.avatar).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
        }else{
            Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
        }
        email.setText(user.getEmail());
        displayName.setText(user.getDisplayName());
        contact.setText("");
        if(!user.getContact().equals("")){
            contact.setText(user.getContact());
        }


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

        Button editLink = (Button) contentLayout.findViewById(R.id.linearLayout5).findViewById(R.id.editButton);
        if(!listingOwner) {
            editLink.setVisibility(View.VISIBLE);
            editLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEditMyProfileFragment();
                }
            });
        }
        else{
            editLink.setVisibility(View.GONE);
        }
    }

    /**
     * Otvaranje fragmenta za uređivanje profila
     */
    private void openEditMyProfileFragment(){

        Bundle userDataBundle = new Bundle();
        userDataBundle.putString("ProfileImgPath",user.getProfileImgPath());
        userDataBundle.putString("DisplayName",user.getDisplayName());
        userDataBundle.putString("Location",user.getLocation());
        userDataBundle.putString("Bio",user.getBio());
        userDataBundle.putString("contact",user.getContact());

        EditMyProfileFragment mEditMyProfileFragment = new EditMyProfileFragment();
        mEditMyProfileFragment.setArguments(userDataBundle);

        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.main_layout, mEditMyProfileFragment, "MY_PROFILE" )
                .addToBackStack("profileEdit")
                .commit();
    }

    @Override
    public void onDestroyView() {
        if (getView() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            parent.removeAllViews();
        }
        super.onDestroyView();
    }
}
