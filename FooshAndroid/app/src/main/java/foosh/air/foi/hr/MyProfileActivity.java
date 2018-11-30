package foosh.air.foi.hr;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import foosh.air.foi.hr.fragments.MyProfileViewFragment;

public class MyProfileActivity extends NavigationDrawerBaseActivity {
    private List<Fragment> mFragments;
    private FirebaseAuth mAuth;
    private ConstraintLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments.add(new MyProfileViewFragment());
        /*
        contentLayout = findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.fragment_my_profile_view, contentLayout);
        contentLayout.setVisibility(ConstraintLayout.GONE);*/
    }

    public void startProfileView(){
        FragmentManager mFragmentManager = getSupportFragmentManager();
        // mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mFragmentManager.beginTransaction()
             .add(R.id.main_layout, mFragments.get(0))
             .commit();
    }

}
