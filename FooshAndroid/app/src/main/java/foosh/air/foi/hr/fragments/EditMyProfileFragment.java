package foosh.air.foi.hr.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.User;

public class EditMyProfileFragment extends Fragment {
    private ConstraintLayout contentLayout;
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
        contentLayout = (ConstraintLayout) container;

        CircleImageView profilePhoto = contentLayout.findViewById(R.id.userProfileImage);
        TextInputEditText displayName = contentLayout.findViewById(R.id.userDisplayName);
        // TextInputEditText city = contentLayout.findViewById(R.id.userCity);
        TextInputEditText bio = contentLayout.findViewById(R.id.userDescription);

        Picasso.get().load(user.getProfileImgPath()).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(profilePhoto);
        displayName.setText(user.getDisplayName());
        if(user.getLocation() != null){
            // city.setText(user.getLocation());
        }

        bio.setText(user.getBio());

        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
