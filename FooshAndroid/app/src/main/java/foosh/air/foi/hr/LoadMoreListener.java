package foosh.air.foi.hr;

import foosh.air.foi.hr.model.Listing;

public interface LoadMoreListener {
    void loadMore(Listing id, int mPostsPerPage, LoadCompletedListener loadCompletedListener);
}