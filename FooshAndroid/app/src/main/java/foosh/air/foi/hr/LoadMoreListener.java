package foosh.air.foi.hr;

import foosh.air.foi.hr.model.Ads;

public interface LoadMoreListener {
    void loadMore(Ads id, int mPostsPerPage, LoadCompletedListener loadCompletedListener);
}