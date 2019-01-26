package foosh.air.foi.hr;

import foosh.air.foi.hr.model.Listing;

public interface MainFeedLoadMoreListener {
    void loadMore(Listing last, int startAt, int limit, LoadCompletedListener loadCompletedListener);
}