package foosh.air.foi.hr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import foosh.air.foi.hr.interfaces.DialogFragmentItem;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.fragments.CameraDialogFragment;
import foosh.air.foi.hr.fragments.ListingDetailFragment;
import foosh.air.foi.hr.fragments.QRDialogFragment;

public class ListingDetailActivity extends NavigationDrawerBaseActivity implements QRDialogFragment.OnQRBitmapListener, CameraDialogFragment.OnQRCameraListener {
    private static final String KEY_PREFIX = "foosh.air.foi.hr.MyListingsFragment.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "fragment-key";

    private final int MenuItem_ExpandOpt = 0;
    private String fragmentKey;
    private String mListingId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.id_toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            fragmentKey = b.getString(ARG_TYPE_KEY);
            mListingId = b.getString("listingId");
        }

        Bundle bundle = new Bundle();
        bundle.putString("listingId", mListingId);

        // Create new fragment and transaction
        Fragment listingDetailFragment = new ListingDetailFragment();
        listingDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.main_layout, listingDetailFragment, fragmentKey);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onQRScanned(DialogFragmentItem self, String qrCode) {
        ((DialogFragmentItem.FragmentCommunicationCameraDialog)getSupportFragmentManager().findFragmentByTag(fragmentKey)).onQRScannedfromActivity(self, qrCode);
    }

    @Override
    public void onQRShown(DialogFragmentItem self, String qrCode) {
        ((DialogFragmentItem.FragmentCommunicationQRDialog)getSupportFragmentManager().findFragmentByTag(fragmentKey)).onQRShownfromActivity(self, qrCode);
    }
}
