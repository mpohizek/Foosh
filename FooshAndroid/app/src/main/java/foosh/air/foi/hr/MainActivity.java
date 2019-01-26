package foosh.air.foi.hr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    private Toolbar toolbar;
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout contentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        getLayoutInflater().inflate(R.layout.activity_main_feed, contentLayout);


        toolbar = findViewById(R.id.id_toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);


        mAuth = FirebaseAuth.getInstance();
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            /*
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
            */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditListingActivity.class);
                intent.putExtra("listingId", "-LVsKoZ-bGV1Z9T7L_B2");
                startActivity(intent);
            }
        });
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return false;
        }
    }
}
