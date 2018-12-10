package foosh.air.foi.hr;

import android.content.Context;

import java.util.ArrayList;

import foosh.air.foi.hr.model.Ads;

public interface onListingsDelivered {
    ArrayList<Ads> fetchAds(Context context);
}
