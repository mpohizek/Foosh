package foosh.air.foi.hr.interfaces;

import foosh.air.foi.hr.model.Listing;

/**
 * Sučelje za komunikaciju adaptera i fragmenta za potrebe dohvaćanja svih oglasa
 */
public interface MainFeedLoadMoreListener {
    void loadMore(boolean hiring, Listing last, int startAt, int limit, LoadCompletedListener loadCompletedListener);
}