package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.Listing;

public class MyListingsEndlessRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<Listing> mDataset = new ArrayList<>();
    private Context mcontext;
    private int limit;
    private int startAt = 0;
    private boolean hiring;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_AD = 2;
    private final int descriptionLength = 100;

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
        return mDataset.size() - 1;
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

    public MyListingsEndlessRecyclerViewAdapter(boolean hiring, final Context context, RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout,
                                                final int mPostsPerPage, final LoadMoreListener loadMoreListener) {
        mcontext = context;
        this.hiring = hiring;
        limit = mPostsPerPage;
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
                loadMoreListener.loadMore(getLastItem(), 0, limit, new LoadCompletedListener() {
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
                Log.d("Fragment", this.toString());
                Log.d("totalItemCount", String.valueOf(totalItemCount));
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.d("lastVisibleItem", String.valueOf(lastVisibleItem));
                for (Listing listings : mDataset) {
                    Log.d("mDataset", listings != null ? listings.getTitle() : "null");
                }
                if (!isLoading && totalItemCount <= (lastVisibleItem + 1 + visibleThreshold)) {
                    setLoading(true);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            add(null);
                            loadMoreListener.loadMore(getLastItem(), startAt, mPostsPerPage, new LoadCompletedListener() {
                                @Override
                                public void onLoadCompleted(ArrayList<Listing> newListings) {
                                    remove(null);
                                    if (newListings.size() > 0){
                                        addList(newListings);
                                        startAt += newListings.size();
                                    }
                                    else{
                                        stopScrolling = true;
                                    }
                                    setLoading(false);
                                }
                            });
                        }
                    });
                }
            }
        });
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                add(null);
                setLoading(true);
                loadMoreListener.loadMore(null, 0, mPostsPerPage, new LoadCompletedListener() {
                    @Override
                    public void onLoadCompleted(ArrayList<Listing> newListings) {
                        remove(null);
                        if (newListings.size() > 0){
                            addList(newListings);
                            startAt += newListings.size();
                        }
                        setLoading(false);
                    }
                });
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_listing_item, parent, false);
            return new ViewHolderRow(view);
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
        if (holder instanceof ViewHolderRow){
            final Listing listing = mDataset.get(position);
            final ViewHolderRow viewHolderRow = (ViewHolderRow)holder;
            viewBinderHelper.setOpenOnlyOne(true);
            viewBinderHelper.bind(viewHolderRow.swipeRevealLayout, String.valueOf(listing.getId()));

            viewHolderRow.status.setText(listing.getStatus());
            viewHolderRow.kategorije.setText(listing.getCategory());
            viewHolderRow.naslov.setText(listing.getId());

            if(listing.getDescription().length()<descriptionLength){
                viewHolderRow.opis.setText(listing.getDescription());
            }else{
                String opis = listing.getDescription().substring(0,descriptionLength);
                viewHolderRow.opis.setText(opis.trim() + "...");
            }

            if(listing.getImages()!= null){
                Picasso.get().load(listing.getImages().get(0)).centerCrop().fit().placeholder(R.drawable.avatar)
                        .error(R.drawable.ic_launcher_foreground).into(viewHolderRow.slika);
            }
            viewHolderRow.setListingID(listing.getId());
            viewHolderRow.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ListingDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("foosh.air.foi.hr.MyListingsFragment.fragment-key","listingDetail");
                    intent.putExtra("listingId", listing.getId());
                    mcontext.startActivity(intent);
                }
            });
            viewHolderRow.prvi.setText("Obriši");
            viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mcontext,
                            R.style.CustomDialog);
                    builder.setMessage("Jeste li sigurni?").setTitle("Brisanje oglasa")
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseDatabase.getInstance().getReference().child("listings/" + listing.getId()).removeValue();
                                    Toast.makeText(mcontext, "Oglas obrisan!", Toast.LENGTH_LONG).show();
                                    remove(listing);
                                }
                            })
                            .setNegativeButton("Ne", null).create().show();
                }
            });
            viewHolderRow.drugi.setText("Uredi");
            viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

    public class ViewHolderRow extends RecyclerView.ViewHolder {
        public SwipeRevealLayout swipeRevealLayout;
        private Button prvi, drugi;
        private ImageView slika;
        private TextView naslov, kategorije, opis, status;
        private CardView cardView;

        public String getListingID() {
            return listingID;
        }

        public void setListingID(String listingID) {
            this.listingID = listingID;
        }

        private String listingID;

        public ViewHolderRow(View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.id_swipe);
            prvi = itemView.findViewById(R.id.info_button);
            drugi = itemView.findViewById(R.id.edit_button);
            slika = itemView.findViewById(R.id.my_listing_picture);
            naslov = itemView.findViewById(R.id.textView);
            kategorije = itemView.findViewById(R.id.textView2);
            opis = itemView.findViewById(R.id.textView3);
            status = itemView.findViewById(R.id.textView4);
            cardView = itemView.findViewById(R.id.listingCardView);
        }
    }
}