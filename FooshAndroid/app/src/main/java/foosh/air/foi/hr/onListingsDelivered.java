package foosh.air.foi.hr;

import android.content.Context;

import java.util.ArrayList;

import foosh.air.foi.hr.model.Listing;

public interface onListingsDelivered {
    ArrayList<Listing> fetchListings(Context context);
}
