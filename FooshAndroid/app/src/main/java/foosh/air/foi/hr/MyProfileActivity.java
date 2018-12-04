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

    private FirebaseAuth mAuth;
    private ConstraintLayout contentLayout;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startProfileView();
    }

    public void startProfileView(){
        mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction()
             .replace(R.id.main_layout, new MyProfileViewFragment())
             .addToBackStack("test")
             .commit();
    }


}
