package foosh.air.foi.hr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import foosh.air.foi.hr.model.User;

public class NavigationDrawerBaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navigationHeader = navigationView.getHeaderView(0);

        setUserNameListener(navigationHeader);

        navigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawerBaseActivity.this, MyProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.START, true);
            }
        });

        TextView displayNameText = (TextView) navigationHeader.findViewById(R.id.displayNameText);
        //displayNameText.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        TextView emailText = (TextView) navigationHeader.findViewById(R.id.emailText);
        //emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        navigationView.getMenu().add(Menu.NONE,MyListingsActivity.id,Menu.NONE, MyListingsActivity.getMenuTitle()).setIcon(R.drawable.ic_star_white_24dp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case MyListingsActivity.id:
                        Intent intent = new Intent(NavigationDrawerBaseActivity.this, MyListingsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        drawerLayout.closeDrawer(Gravity.START, true);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    private void setUserNameListener(final View header){


        DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                TextView displayNameText = (TextView) header.findViewById(R.id.displayNameText);
                displayNameText.setText(user.getDisplayName());
                TextView emailText = (TextView) header.findViewById(R.id.emailText);
                emailText.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
