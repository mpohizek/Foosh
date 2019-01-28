package foosh.air.foi.hr;

import android.content.Context;

import java.util.ArrayList;

import foosh.air.foi.hr.model.Listing;

/**
 * Sučelje za komunikaciju fragmenta i MyListingActivity
 */
public interface onListingsDelivered {
    ArrayList<Listing> fetchListings(Context context);
}
