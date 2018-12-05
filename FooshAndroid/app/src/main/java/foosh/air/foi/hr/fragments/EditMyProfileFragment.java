package foosh.air.foi.hr.fragments;

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
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.User;

public class EditMyProfileFragment extends Fragment {
    private ScrollView contentLayout;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        CircleImageView profilePhoto =  scrollView.findViewById(R.id.userProfileImage);
        TextInputEditText displayName = scrollView.findViewById(R.id.userDisplayName);
        TextInputEditText city = scrollView.findViewById(R.id.userCity);
        TextInputEditText bio = scrollView.findViewById(R.id.userDescription);

        Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
        displayName.setText(user.getDisplayName());
        bio.setText(user.getBio());
        if(user.getLocation() != null){
            city.setText(user.getLocation());
        }

        

    }
}
