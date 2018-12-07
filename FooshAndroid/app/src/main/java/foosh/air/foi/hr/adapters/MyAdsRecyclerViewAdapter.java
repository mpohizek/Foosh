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
import foosh.air.foi.hr.model.Ads;

public class MyAdsRecyclerViewAdapter extends RecyclerView.Adapter<MyAdsRecyclerViewAdapter.ViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<Ads> myAds;

    public MyAdsRecyclerViewAdapter(ArrayList<Ads> myAds) {
        this.myAds = myAds;
    }
    public void removeItem(int position) {
        this.myAds.remove(position);
        notifyItemRemoved(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Ads ad = myAds.get(position);
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(ad.getId()));

        holder.status.setText(ad.getStatus());
        StringBuilder kategorije = new StringBuilder(ad.getKategorije().get(0));
        for (String s : ad.getKategorije().subList(1, ad.getKategorije().size())) {
            kategorije.append(", " + s);
        }
        holder.kategorije.setText(kategorije);
        holder.naslov.setText(ad.getNaziv());
        holder.opis.setText(ad.getOpis());
        Picasso.get().load(ad.getSlike().get(0)).placeholder(R.drawable.avatar).error(R.drawable.ic_launcher_foreground).into(holder.slika);

        if (ad.isZaposljavam()){
            if (ad.getStatus() == "OBJAVLJEN"){
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
            else if (ad.getStatus() == "U DOGOVORU"){
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
            if (ad.getStatus() == "U DOGOVORU"){
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
            else if (ad.getStatus() == "DOGOVOREN"){
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
        return myAds.size();
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
            slika = itemView.findViewById(R.id.my_ad_picture);
            naslov = itemView.findViewById(R.id.textView);
            kategorije = itemView.findViewById(R.id.textView2);
            opis = itemView.findViewById(R.id.textView3);
            opis.setMovementMethod(new ScrollingMovementMethod());
            status = itemView.findViewById(R.id.textView4);
        }

    }


}
