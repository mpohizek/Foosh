package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foosh.air.foi.hr.LoadCompletedListener;
import foosh.air.foi.hr.LoadMoreListener;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.Ads;

public class MyAdsEndlessRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<Ads> mDataset = new ArrayList<>();
    private Context mcontext;
    private int mPostsPerPage;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

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


    public ArrayList<Ads> getDataSet(){
        return mDataset;
    }

    public Ads getLastItem(){
        if (mDataset.size() > 0){
            return mDataset.get(mDataset.size() - 1);
        }
        return null;
    }
    public void add(Ads item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.size() - 1);
    }
    public void addList(ArrayList<Ads> ads){
        int oldSize = mDataset.size();
        mDataset.addAll(ads);
        notifyItemRangeInserted(oldSize, mDataset.size());
    }

    public void remove(Ads item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
    public void clear(){
        mDataset.clear();
        notifyDataSetChanged();
    }

    public MyAdsEndlessRecyclerViewAdapter(final Context context, RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout,
                                           final int mPostsPerPage, final LoadMoreListener loadMoreListener) {
        mcontext = context;
        this.mPostsPerPage = mPostsPerPage;
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
                loadMoreListener.loadMore(null, mPostsPerPage, new LoadCompletedListener() {
                    @Override
                    public void onLoadCompleted(ArrayList<Ads> newAds) {
                        if (newAds.size() > 0){
                            addList(newAds);
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
                for (Ads ads : mDataset) {
                    Log.d("mDataset", ads != null ? ads.getNaziv() : "null");
                }
                Log.d("lastItem", getLastItem() != null ? getLastItem().getId() : "null");
                if (!isLoading && totalItemCount <= (lastVisibleItem + 1 + visibleThreshold)) {
                    setLoading(true);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            Ads lastAd = getLastItem();
                            add(null);
                            loadMoreListener.loadMore(lastAd, mPostsPerPage, new LoadCompletedListener() {
                                @Override
                                public void onLoadCompleted(ArrayList<Ads> newAds) {
                                    remove(null);
                                    if (newAds.size() > 0){
                                        addList(newAds);
                                    }
                                    else{
                                        Snackbar.make(recyclerView, "Kraj", Snackbar.LENGTH_LONG)
                                                .setAction("Početak",
                                                        new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                recyclerView.smoothScrollToPosition(0);
                                                            }
                                                        }).show();
                                        //Toast.makeText(context, "Kraj", Toast.LENGTH_LONG).show();
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
                loadMoreListener.loadMore(null, mPostsPerPage, new LoadCompletedListener() {
                    @Override
                    public void onLoadCompleted(ArrayList<Ads> newAds) {
                        remove(null);
                        if (newAds.size() > 0){
                            addList(newAds);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_item, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderRow){
            Ads ad = mDataset.get(position);
            ViewHolderRow viewHolderRow = (ViewHolderRow)holder;
            viewBinderHelper.setOpenOnlyOne(true);
            viewBinderHelper.bind(viewHolderRow.swipeRevealLayout, String.valueOf(ad.getId()));

            viewHolderRow.status.setText(ad.getStatus());
            StringBuilder kategorije = new StringBuilder(ad.getKategorije().get(0));
            for (String s : ad.getKategorije().subList(1, ad.getKategorije().size())) {
                kategorije.append(", " + s);
            }
            viewHolderRow.kategorije.setText(kategorije);
            viewHolderRow.naslov.setText(ad.getId());
            viewHolderRow.opis.setText(ad.getOpis());
            Picasso.get().load(ad.getSlike().get(0)).placeholder(R.drawable.avatar)
                    .error(R.drawable.ic_launcher_foreground).into(viewHolderRow.slika);

            if (ad.isZaposljavam()){
                if (ad.getStatus().equals("OBJAVLJEN")){
                    viewHolderRow.prvi.setText("Obriši");
                    viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    viewHolderRow.drugi.setText("Uredi");
                    viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                else if (ad.getStatus().equals("U DOGOVORU")){
                    viewHolderRow.prvi.setText("Poruke");
                    viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    viewHolderRow.drugi.setText("Uredi");
                    viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                else{
                    viewHolderRow.prvi.setText("Poruke");
                    viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    viewHolderRow.drugi.setText("Zaključi");
                    viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }
            else{
                if (ad.getStatus().equals("U DOGOVORU")){
                    viewHolderRow.prvi.setText("Poruke");
                    viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    viewHolderRow.drugi.setText("Odjavi");
                    viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                else if (ad.getStatus().equals("DOGOVOREN")){
                    viewHolderRow.prvi.setText("Poruke");
                    viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    viewHolderRow.drugi.setText("Skeniraj QR");
                    viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                else{
                    viewHolderRow.prvi.setText("Poruke");
                    viewHolderRow.prvi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    viewHolderRow.drugi.setText("Recenziraj");
                    viewHolderRow.drugi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }
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

        public ViewHolderRow(View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.id_swipe);
            prvi = itemView.findViewById(R.id.info_button);
            drugi = itemView.findViewById(R.id.edit_button);
            slika = itemView.findViewById(R.id.my_ad_picture);
            naslov = itemView.findViewById(R.id.textView);
            kategorije = itemView.findViewById(R.id.textView2);
            opis = itemView.findViewById(R.id.textView3);
            opis.setMovementMethod(new ScrollingMovementMethod());
            status = itemView.findViewById(R.id.textView4);
        }
    }
}