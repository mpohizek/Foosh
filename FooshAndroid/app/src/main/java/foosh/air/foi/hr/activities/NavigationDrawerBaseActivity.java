package foosh.air.foi.hr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import am.appwise.components.ni.NoInternetDialog;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.User;

/**
 * Aktivnost za bočni glavni meni.
 * Bazna aktivnost koju druge aktivnosti da bi mogle imati bočni glavni meni.
 */
public class NavigationDrawerBaseActivity extends AppCompatActivity {

    private static final int RC_MAIN = 1001;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationViewFilter;
    FirebaseUser mUser;
    private NoInternetDialog noInternetDialog;

    /**
     * Ovisno o odabiru opcije iz glavnog menija, otvara odgovarajuću aktivnost.
     * Dodaje ikone i nazive opcija u meniju.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navigationHeader = navigationView.getHeaderView(0);

        navigationViewFilter = (NavigationView) findViewById(R.id.nav_main_feed);

        setUserNameListener(navigationHeader);

        navigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NavigationDrawerBaseActivity.this instanceof MyProfileActivity){
                    String fragmentKey = "myProfile";
                    String mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Bundle b = new Bundle();
                    b.putString("foosh.air.foi.hr.MyListingsFragment.fragment-key", fragmentKey);
                    b.putString("userId", mUserId);
                    ((MyProfileActivity) NavigationDrawerBaseActivity.this).startFragment(b);
                }else{
                    Intent intent = new Intent(NavigationDrawerBaseActivity.this, MyProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("foosh.air.foi.hr.MyListingsFragment.fragment-key","myProfile");
                    intent.putExtra("userId", FirebaseAuth.getInstance().getUid());
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(Gravity.START, true);
            }
        });

        TextView displayNameText = (TextView) navigationHeader.findViewById(R.id.displayNameText);
        //displayNameText.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        TextView emailText = (TextView) navigationHeader.findViewById(R.id.emailText);
        //emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        navigationView.getMenu().add(Menu.NONE,MainActivity.id,Menu.NONE,MainActivity.getMenuTitle()).setIcon(R.drawable.ic_home_white_24dp);

        navigationView.getMenu().add(Menu.NONE,NewListingActivity.id,Menu.NONE, NewListingActivity.getMenuTitle()).setIcon(R.drawable.ic_add_black_24dp);

        navigationView.getMenu().add(Menu.NONE,MyListingsActivity.id,Menu.NONE, MyListingsActivity.getMenuTitle()).setIcon(R.drawable.ic_star_white_24dp);

        navigationView.getMenu().add(Menu.NONE, 3, Menu.NONE, "Odjavi se").setIcon(R.drawable.ic_subdirectory_arrow_right_white_24dp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case MainActivity.id:
                        if (!(NavigationDrawerBaseActivity.this instanceof MainActivity)){
                            intent = new Intent(NavigationDrawerBaseActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawer(Gravity.START, true);
                        break;
                    case MyListingsActivity.id:
                        if (!(NavigationDrawerBaseActivity.this instanceof MyListingsActivity)){
                            intent = new Intent(NavigationDrawerBaseActivity.this, MyListingsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawer(Gravity.START, true);
                        break;

                    case NewListingActivity.id:
                        if (!(NavigationDrawerBaseActivity.this instanceof NewListingActivity)){
                            intent = new Intent(NavigationDrawerBaseActivity.this, NewListingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawer(Gravity.START, true);
                        break;
                    case 3:
                        //Sign out
                        AuthUI.getInstance().signOut(NavigationDrawerBaseActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(NavigationDrawerBaseActivity.this, SignInActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(NavigationDrawerBaseActivity.this, "Sign out failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        break;
                }
                return false;
            }
        });
        noInternetDialog = new NoInternetDialog.Builder(this).build();

    }

    /**
     * Sinkronizira toggle.
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    /**
     * Prikaz imena i e-maila korisnika u bočnom meniju.
     * @param header
     */
    private void setUserNameListener(final View header){
        DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    TextView displayNameText = (TextView) header.findViewById(R.id.displayNameText);
                    displayNameText.setText(user.getDisplayName());
                    TextView emailText = (TextView) header.findViewById(R.id.emailText);
                    emailText.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }
        else if (drawerLayout.isDrawerOpen(Gravity.END)){
            drawerLayout.closeDrawer(Gravity.END);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}
