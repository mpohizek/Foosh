package foosh.air.foi.hr.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.User;

public class EditMyProfileFragment extends Fragment {
    private ScrollView contentLayout;
    private User user;
    private int PICK_PHOTO_FOR_AVATAR = 100;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private CircleImageView profilePhoto;
    private TextInputEditText displayName;
    private TextInputEditText city;
    private TextInputEditText bio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = new User();
        user.setDisplayName(getArguments().getString("DisplayName"));
        user.setLocation(getArguments().getString("Location"));
        user.setBio(getArguments().getString("Bio"));
        user.setProfileImgPath(getArguments().getString("ProfileImgPath"));


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ScrollView scrollView = (ScrollView) view;
        contentLayout = scrollView;

        profilePhoto =  scrollView.findViewById(R.id.userProfileImage);
        displayName = scrollView.findViewById(R.id.userDisplayName);
        city = scrollView.findViewById(R.id.userCity);
        bio = scrollView.findViewById(R.id.userDescription);

        Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
        displayName.setText(user.getDisplayName());
        bio.setText(user.getBio());
        if(user.getLocation() != null){
            city.setText(user.getLocation());
        }

        TextView changePhotoLink = scrollView.findViewById(R.id.linkChangeProfilePhoto);
        changePhotoLink.setOnClickListener(
            new View.OnClickListener()
                   {
                       @Override
                       public void onClick(View v)
                       {
                           openPhotoChoice();
                       }
                   }        );
        Button saveChanges = scrollView.findViewById(R.id.buttonSaveProfile);
        saveChanges.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        saveProfileChanges();
                        getActivity().getFragmentManager().popBackStack();
                    }
                }
        );


    }

    private void saveProfileChanges(){

        user.setDisplayName(displayName.getText().toString());
        user.setBio(bio.getText().toString());
        user.setLocation(city.getText().toString());

        String uid = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(uid).child("displayName").setValue(user.getDisplayName());
        mDatabase.child("users").child(uid).child("profileImgPath").setValue(user.getProfileImgPath());
        mDatabase.child("users").child(uid).child("location").setValue(user.getLocation());
        mDatabase.child("users").child(uid).child("bio").setValue(user.getBio());

    }

    private void openPhotoChoice() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                runUpload(inputStream);
            }catch (Exception e){

            }
        }
    }

    public void runUpload(InputStream stream){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String imageName = user.getDisplayName().replace(' ', '-') + '-' + timeStamp;

        final StorageReference profilePhotosRef = storageRef.child("profile_images/" + imageName);

        UploadTask uploadTask = profilePhotosRef.putStream(stream);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                profilePhotosRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                CircleImageView profilePhoto =  contentLayout.findViewById(R.id.userProfileImage);
                                user.setProfileImgPath(uri.toString());
                                Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
                            }});


        }
        });
    }

}
