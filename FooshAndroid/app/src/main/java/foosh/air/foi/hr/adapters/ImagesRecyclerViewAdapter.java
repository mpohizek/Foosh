package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import foosh.air.foi.hr.R;
import foosh.air.foi.hr.helper.ImagesRecyclerViewDatasetItem;

/**
 * Adapter za prikaz slika u RecyclerView-u u formi za dodavanje i formi za uređivanje oglasa.
 */
public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ImagesRecyclerViewDatasetItem> mDataset;
    private Stack<ImagesRecyclerViewDatasetItem> mDeleted  = new Stack<>();
    private Context context;

    /**
     * Dohvaća listu slika.
     * @return
     */
    public ArrayList<ImagesRecyclerViewDatasetItem> getmDataset() {
        return mDataset;
    }

    /**
     * Dohvaća popis izbrisanih slika.
     * @return
     */
    public Stack<ImagesRecyclerViewDatasetItem> getmDeleted() {
        return mDeleted;
    }

    /**
     * Klasa za prikaz jedne slike u RecyclerView-u.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView deleteImage;
        private RelativeLayout viewBackground, viewForeground;

        /**
         * Konstruktor.
         * @param viewItem
         */
        public MyViewHolder(View viewItem) {
            super(viewItem);
            viewBackground = viewItem.findViewById(R.id.view_background);
            viewForeground = viewItem.findViewById(R.id.view_foreground);
            image = viewItem.findViewById(R.id.image);
            image.setAdjustViewBounds(true);
            deleteImage = viewItem.findViewById(R.id.delete_icon);
        }

        /**
         * Učitava sliku na ImageViewHolder.
         * @param image
         */
        public void setImage(Uri image){
            Picasso.get().load(image).into(this.image);
        }

        /**
         * Uklanja vidljivost slike.
         * @param imageVisibility
         */
        public void setDeleteImageVisibility(int imageVisibility) {
            deleteImage.setVisibility(imageVisibility);
        }

        public RelativeLayout getViewBackground() {
            return viewBackground;
        }

        public void setViewBackground(RelativeLayout viewBackground) {
            this.viewBackground = viewBackground;
        }

        public RelativeLayout getViewForeground() {
            return viewForeground;
        }

        public void setViewForeground(RelativeLayout viewForeground) {
            this.viewForeground = viewForeground;
        }
    }

    /**
     * Konstruktor.
     * @param context
     */
    public ImagesRecyclerViewAdapter(Context context){
        mDataset = new ArrayList<>();
        this.context = context;
    }

    /**
     * Konstruktor.
     * @param myDataset
     * @param context
     */
    public ImagesRecyclerViewAdapter(List<ImagesRecyclerViewDatasetItem> myDataset, Context context) {
        mDataset = (ArrayList<ImagesRecyclerViewDatasetItem>) myDataset;
        this.context = context;
    }

    /**
     * Dodaje sliku u Dataset.
     * @param image
     */
    public void addImageToDataset(ImagesRecyclerViewDatasetItem image){
        mDataset.add(image);
        notifyItemInserted(mDataset.size() - 1);
    }

    /**
     * Dodaje više slika u Dataset.
     * @param images
     */
    public void addImagesToDataset(List<ImagesRecyclerViewDatasetItem> images){
        mDataset.addAll(images);
        notifyItemRangeInserted(mDataset.size() - 1, images.size());
    }

    /**
     * Uklanja element na indeksu.
     * @param position
     */
    public void removeItem(int position) {
        mDeleted.add(mDataset.get(position));
        mDataset.remove(mDataset.get(position));
        notifyItemRemoved(position);
    }

    /**
     * Undo brisanja slike.
     * @param item
     * @param position
     */
    public void restoreItem(Object item, int position) {
        mDataset.add(mDeleted.pop());
        notifyItemInserted(position);
    }

    @Override
    public ImagesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_images_item, parent, false);
        return new MyViewHolder(frameLayout);
    }

    /**
     * Postavlja slike u ViewHolder.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setImage(mDataset.get(position).getImageUri());
            holder.setDeleteImageVisibility(View.VISIBLE);
    }

    /**
     * Vraća broj slika u ViewHolderu.
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}