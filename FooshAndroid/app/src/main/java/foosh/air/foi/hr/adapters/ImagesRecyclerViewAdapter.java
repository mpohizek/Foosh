package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import foosh.air.foi.hr.R;
import foosh.air.foi.hr.helper.ImagesRecyclerViewDatasetItem;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ImagesRecyclerViewDatasetItem> mDataset;
    private Stack<ImagesRecyclerViewDatasetItem> mDeleted  = new Stack<>();
    private Context context;

    public ArrayList<ImagesRecyclerViewDatasetItem> getmDataset() {
        return mDataset;
    }

    public Stack<ImagesRecyclerViewDatasetItem> getmDeleted() {
        return mDeleted;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView deleteImage;
        private RelativeLayout viewBackground, viewForeground;
        public MyViewHolder(View viewItem) {
            super(viewItem);
            viewBackground = viewItem.findViewById(R.id.view_background);
            viewForeground = viewItem.findViewById(R.id.view_foreground);
            image = viewItem.findViewById(R.id.image);
            image.setAdjustViewBounds(true);
            deleteImage = viewItem.findViewById(R.id.delete_icon);
        }
        public void setImage(Uri image){
            Picasso.get().load(image).into(this.image);
        }

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

    public ImagesRecyclerViewAdapter(Context context){
        mDataset = new ArrayList<>();
        this.context = context;
    }

    public ImagesRecyclerViewAdapter(List<ImagesRecyclerViewDatasetItem> myDataset, Context context) {
        mDataset = (ArrayList<ImagesRecyclerViewDatasetItem>) myDataset;
        this.context = context;
    }

    public void addImageToDataset(ImagesRecyclerViewDatasetItem image){
        mDataset.add(image);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void addImagesToDataset(List<ImagesRecyclerViewDatasetItem> images){
        mDataset.addAll(images);
        notifyItemRangeInserted(mDataset.size() - 1, images.size());
    }

    public void removeItem(int position) {
        mDeleted.add(mDataset.get(position));
        mDataset.remove(mDataset.get(position));
        notifyItemRemoved(position);
    }

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

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setImage(mDataset.get(position).getImageUri());
            holder.setDeleteImageVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}