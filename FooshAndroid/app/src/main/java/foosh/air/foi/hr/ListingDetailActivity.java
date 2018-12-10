package foosh.air.foi.hr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import foosh.air.foi.hr.fragments.ListingDetailFragment;

public class ListingDetailActivity extends NavigationDrawerBaseActivity {
    private static final String KEY_PREFIX = "foosh.air.foi.hr.MyListingsFragment.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "fragment-key";

    private String fragmentKey;
    private String mListingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
