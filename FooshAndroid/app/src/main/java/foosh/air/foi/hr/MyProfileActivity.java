package foosh.air.foi.hr;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.List;

import foosh.air.foi.hr.fragments.EditMyProfileFragment;
import foosh.air.foi.hr.fragments.MyProfileViewFragment;

public class MyProfileActivity extends NavigationDrawerBaseActivity {
    private static final String KEY_PREFIX = "foosh.air.foi.hr.MyListingsFragment.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "fragment-key";

    private FirebaseAuth mAuth;
    private ConstraintLayout contentLayout;
    private FragmentManager mFragmentManager;

    private String fragmentKey;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startProfileView();
    }

    public void startProfileView(){
        mFragmentManager = getSupportFragmentManager();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            fragmentKey = b.getString(ARG_TYPE_KEY);
            mUserId = b.getString("userId");
        }
        else{
            fragmentKey = "myProfile";
            mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            b = new Bundle();
            b.putString(ARG_TYPE_KEY, fragmentKey);
            b.putString("userId", mUserId);
        }

        // Create new fragment and transaction
        Fragment myProfileViewFragment = new MyProfileViewFragment();
        myProfileViewFragment.setArguments(b);
        mFragmentManager.beginTransaction().replace(R.id.main_layout, myProfileViewFragment, fragmentKey).commit();

    }
}
