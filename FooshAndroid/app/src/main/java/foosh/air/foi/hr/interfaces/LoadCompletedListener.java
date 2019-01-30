package foosh.air.foi.hr.interfaces;

import java.util.ArrayList;

import foosh.air.foi.hr.model.Listing;

/**
 * Sučelje za komunikaciju adaptera i fragmenta kod završetka dohvaćanja oglasa
 */
public interface LoadCompletedListener {
    void onLoadCompleted(ArrayList<Listing> newListings);
}
