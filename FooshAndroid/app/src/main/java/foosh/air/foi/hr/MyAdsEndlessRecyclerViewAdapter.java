package foosh.air.foi.hr;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foosh.air.foi.hr.model.Ads;

public class MyAdsEndlessRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<Ads> mDataset;
    private Context mcontext;
    private int mPostsPerPage;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public Ads getLastItem(){
        if (mDataset.size() > 0 && mDataset.get(mDataset.size() - 1) != null){
            return mDataset.get(mDataset.size() - 1);
        }
        return null;
    }
    public void add(int position, Ads item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Ads item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public MyAdsEndlessRecyclerViewAdapter(Context context, ArrayList<Ads> ads, int mPostsPerPage) {

        mcontext = context;
        mDataset = ads;
        this.mPostsPerPage = mPostsPerPage;
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
            Picasso.get().load(ad.getSlike().get(0)).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(viewHolderRow.slika);

            if (ad.isZaposljavam()){
                if (ad.getStatus() == "OBJAVLJEN"){
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
                else if (ad.getStatus() == "U DOGOVORU"){
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
                if (ad.getStatus() == "U DOGOVORU"){
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
                else if (ad.getStatus() == "DOGOVOREN"){
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