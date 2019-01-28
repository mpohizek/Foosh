package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foosh.air.foi.hr.ListingDetailActivity;
import foosh.air.foi.hr.LoadCompletedListener;
import foosh.air.foi.hr.LoadMoreListener;
import foosh.air.foi.hr.MainFeedLoadMoreListener;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.Listing;

public class MainFeedListingsEndlessRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Listing> mDataset = new ArrayList<>();
    private Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_AD = 2;
    private final int descriptionLength = 100;

    private int startAt;
    private boolean listingHiring;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    private boolean isLoading;
    private int visibleThreshold = 0;
    private int lastVisibleItem, totalItemCount;

    public boolean isStopScrolling() {
        return stopScrolling;
    }

    public void setStopScrolling(boolean stopScrolling) {
        this.stopScrolling = stopScrolling;
    }

    private boolean stopScrolling = false;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainFeedLoadMoreListener mainFeedLoadMoreListener;
    private int mPostsPerPage;

    public ArrayList<Listing> getDataSet(){
        return mDataset;
    }

    public Listing getLastItem(){
        if (mDataset.size() > 1){
            return mDataset.get(mDataset.size() - 2);
        }
        return null;
    }
    public int getLastItemPosition(){
        return mDataset.size()-1;
    }
    public void add(Listing item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.size() - 1);
    }
    public void addList(ArrayList<Listing> listings){
        int oldSize = mDataset.size();
        mDataset.addAll(listings);
        notifyItemRangeInserted(oldSize, mDataset.size());
    }

    public void remove(Listing item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
    public void clear(){
        mDataset.clear();
        notifyDataSetChanged();
    }

    private LoadCompletedListener loadCompletedListener= new LoadCompletedListener(){
        @Override
        public void onLoadCompleted(ArrayList<Listing> newListings) {
            remove(null);
            if (newListings.size() > 0){
                addList(newListings);
                startAt += newListings.size();
                setStopScrolling(false);
            }
            else{
                setStopScrolling(true);
            }
            setLoading(false);
        }
    };

    public void sortOperation(){
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                setStopScrolling(true);
                clear();
                add(null);
                setLoading(true);
                startAt = 0;
                mainFeedLoadMoreListener.loadMore(listingHiring, null, startAt, mPostsPerPage, loadCompletedListener);
            }
        });
    }
    public MainFeedListingsEndlessRecyclerViewAdapter (boolean owner, final Context context, RecyclerView recyclerViewArg, SwipeRefreshLayout swipeRefreshLayoutArg,
                                                       int mPostsPerPageArg, MainFeedLoadMoreListener mainFeedLoadMoreListenerArg, final NavigationView navigationView){

        mContext = context;
        listingHiring = owner;
        recyclerView = recyclerViewArg;
        swipeRefreshLayout = swipeRefreshLayoutArg;
        mPostsPerPage = mPostsPerPageArg;
        mainFeedLoadMoreListener = mainFeedLoadMoreListenerArg;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoading()) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                setStopScrolling(true);
                clear();
                setLoading(true);
                startAt = 0;
                mainFeedLoadMoreListener.loadMore(listingHiring, getLastItem(), 0, mPostsPerPage, new LoadCompletedListener() {
                    @Override
                    public void onLoadCompleted(ArrayList<Listing> newListings) {
                        if (newListings.size() > 0){
                            addList(newListings);
                            startAt += newListings.size();
                        }
                        setLoading(false);
                        setStopScrolling(false);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (stopScrolling)
                    return;
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + 1 + visibleThreshold)) {
                    setLoading(true);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            add(null);
                            mainFeedLoadMoreListener.loadMore(listingHiring, getLastItem(), startAt, mPostsPerPage, loadCompletedListener);
                        }
                    });
                }
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                setStopScrolling(true);
                clear();
                add(null);
                setLoading(true);
                mainFeedLoadMoreListener.loadMore(listingHiring, null, 0, mPostsPerPage, new LoadCompletedListener() {
                    @Override
                    public void onLoadCompleted(ArrayList<Listing> newListings) {
                        remove(null);
                        if (newListings.size() > 0){
                            addList(newListings);
                            startAt += newListings.size();
                        }
                        setStopScrolling(false);
                        setLoading(false);
                    }
                });
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_feed_listing_item, parent, false);
            return new MainFeedViewHolderRow(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar_item, parent, false);
            if (view instanceof SwipeRevealLayout){
                SwipeRevealLayout swipeRevealLayout = (SwipeRevealLayout) view;
                swipeRevealLayout.removeView(swipeRevealLayout.findViewById(R.id.id_swipe_framelayout));
            }
            return new ViewHolderLoading(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainFeedViewHolderRow){
            final Listing listing = mDataset.get(position);
            final MainFeedViewHolderRow viewHolderRow = (MainFeedViewHolderRow)holder;

            viewHolderRow.category.setText(listing.getCategory());
            viewHolderRow.title.setText(listing.getTitle());
            viewHolderRow.price.setText(String.valueOf(listing.getPrice())+" HRK");

            if(listing.getDescription().length()<descriptionLength){
                viewHolderRow.desc.setText(listing.getDescription());
            }else{
                String opis = listing.getDescription().substring(0,descriptionLength);
                viewHolderRow.desc.setText(opis.trim() + "...");
            }

            if(listing.getImages()!= null){
                Picasso.get().load(listing.getImages().get(0)).centerCrop().fit().placeholder(R.drawable.avatar)
                        .error(R.drawable.ic_launcher_foreground).into(viewHolderRow.image);
            }
            viewHolderRow.setListingID(listing.getId());
            viewHolderRow.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ListingDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("foosh.air.foi.hr.MyListingsFragment.fragment-key","listingDetail");
                    intent.putExtra("listingId", listing.getId());
                    mContext.startActivity(intent);
                }
            });
        }
        else if (holder instanceof ViewHolderLoading){
            ViewHolderLoading loadingViewHolder = (ViewHolderLoading) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ViewHolderLoading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.itemProgressbar);
        }
    }

    public class MainFeedViewHolderRow extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, category, desc, price;
        private CardView cardView;

        public String getListingID() {
            return listingID;
        }

        public void setListingID(String listingID) {
            this.listingID = listingID;
        }

        private String listingID;

        public MainFeedViewHolderRow(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.main_feed_listing_image);
            title = itemView.findViewById(R.id.main_feed_listing_title);
            category = itemView.findViewById(R.id.main_feed_listing_category);
            desc = itemView.findViewById(R.id.main_feed_listing_description);
            cardView = itemView.findViewById(R.id.main_feed_card_view);
            price = itemView.findViewById(R.id.main_feed_listing_price);
        }
    }
}
