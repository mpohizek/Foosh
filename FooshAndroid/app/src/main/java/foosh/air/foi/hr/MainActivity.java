package foosh.air.foi.hr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends NavigationDrawerBaseActivity {

    private static final int RC_MAIN = 1001;
    private FirebaseAuth mAuth;
    Button signOutButton;
    //TODO: remove
    Button listingDetailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout contentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_main_feed, contentLayout);
        mAuth = FirebaseAuth.getInstance();
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Sign out failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }


        //TODO: remove
        listingDetailButton = (Button) findViewById(R.id.listingDetailButton);
        listingDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListingDetailActivity.class));
            }
        });

    }
    public void callSignInActivity(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_MAIN);
    }
}
