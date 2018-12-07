package foosh.air.foi.hr;

import java.util.ArrayList;

import foosh.air.foi.hr.model.Ads;

public interface LoadCompletedListener {
    void onLoadCompleted(ArrayList<Ads> newAds);
}
