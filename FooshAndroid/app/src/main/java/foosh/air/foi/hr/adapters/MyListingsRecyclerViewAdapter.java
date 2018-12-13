package foosh.air.foi.hr.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.Listing;

public class MyListingsRecyclerViewAdapter extends RecyclerView.Adapter<MyListingsRecyclerViewAdapter.ViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<Listing> myListings;

    public MyListingsRecyclerViewAdapter(ArrayList<Listing> myListings) {
        this.myListings = myListings;
    }
    public void removeItem(int position) {
        this.myListings.remove(position);
        notifyItemRemoved(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_listing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Listing listing = myListings.get(position);
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(listing.getId()));

        holder.status.setText(listing.getStatus());
        holder.kategorije.setText(listing.getCategory());
        holder.naslov.setText(listing.getTitle());
        holder.opis.setText(listing.getDescription());
        Picasso.get().load(listing.getImages().get(0)).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(holder.slika);

        if (listing.isHiring()){
            if (listing.getStatus() == "OBJAVLJEN"){
                holder.prvi.setText("Obriši");
                holder.prvi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeItem(holder.getAdapterPosition());
                    }
                });
                holder.drugi.setText("Uredi");
                holder.drugi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            else if (listing.getStatus() == "U DOGOVORU"){
                holder.prvi.setText("Poruke");
                holder.prvi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.drugi.setText("Uredi");
                holder.drugi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            else{
                holder.prvi.setText("Poruke");
                holder.prvi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.drugi.setText("Zaključi");
                holder.drugi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }
        else{
            if (listing.getStatus() == "U DOGOVORU"){
                holder.prvi.setText("Poruke");
                holder.prvi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.drugi.setText("Odjavi");
                holder.drugi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            else if (listing.getStatus() == "DOGOVOREN"){
                holder.prvi.setText("Poruke");
                holder.prvi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.drugi.setText("Skeniraj QR");
                holder.drugi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            else{
                holder.prvi.setText("Poruke");
                holder.prvi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.drugi.setText("Recenziraj");
                holder.drugi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return myListings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SwipeRevealLayout swipeRevealLayout;
        private Button prvi, drugi;
        private ImageView slika;
        private TextView naslov, kategorije, opis, status;
        public ViewHolder(View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.id_swipe);
            prvi = itemView.findViewById(R.id.info_button);
            drugi = itemView.findViewById(R.id.edit_button);
            slika = itemView.findViewById(R.id.my_listing_picture);
            naslov = itemView.findViewById(R.id.textView);
            kategorije = itemView.findViewById(R.id.textView2);
            opis = itemView.findViewById(R.id.textView3);
            opis.setMovementMethod(new ScrollingMovementMethod());
            status = itemView.findViewById(R.id.textView4);
        }

    }


}
