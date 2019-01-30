package foosh.air.foi.hr.interfaces;

import foosh.air.foi.hr.model.Listing;

/**
 * Sučelje za komunikaciju adaptera i fragmenta za potrebe dohvaćanja korisnikovih oglasa
 */
public interface LoadMoreListener {
    void loadMore(boolean owner, Listing last, int startAt, int limit, LoadCompletedListener loadCompletedListener);
}