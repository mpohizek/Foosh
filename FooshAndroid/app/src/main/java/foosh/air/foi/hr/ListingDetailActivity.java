package foosh.air.foi.hr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class ListingDetailActivity extends NavigationDrawerBaseActivity {
    private String mListingId;
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: fetch the listing id from the previous activity
        /*
        mListingId = savedInstanceState.getString("listing_id");
        if (mListingId == null) {
            throw new IllegalArgumentException("Must pass listing_id");
        }
        */

        //TODO: check if the current user is the owner, if true add the edit option
        //mAuth = FirebaseAuth.getInstance();

        //TODO: remove temporary id
        mListingId = "l0004";

        Bundle bundle = new Bundle();
        bundle.putString("listingId", mListingId);

        // Create new fragment and transaction
        Fragment listingDetailFragment = new ListingDetailFragment();
        listingDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.main_layout, listingDetailFragment, "listingDetail");
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
