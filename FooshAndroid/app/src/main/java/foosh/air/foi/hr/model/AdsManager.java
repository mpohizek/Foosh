package foosh.air.foi.hr.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Stack;

import foosh.air.foi.hr.onAdsDelivered;

public class AdsManager {
    private ArrayList<Ads> allAds;
    private onAdsDelivered listener;
    private onDataFetched adapter;

    public interface onDataFetched{
        void adapter_success(boolean first_time);
        void adapter_failure();
    }

    {
        allAds = new ArrayList<>();
    }
    public AdsManager(onAdsDelivered listener) {
        this.listener = listener;
    }
    public String getLastKey(){
        try{
            return allAds.get(allAds.size() - 1).getStatus();
        } catch(Exception e){
            return null;
        }

    }
    public ArrayList<Ads> getAllAds() {
        return allAds;
    }
    public ArrayList<Ads> getAllAds(int user) {
        ArrayList<Ads> helper = new ArrayList<>();
        for (Ads ad : allAds) {
            if (ad.getVlasnik() == user){
                helper.add(ad);
            }
        }
        return helper;
    }
    public ArrayList<Ads> getAllAds(int user, boolean zaposljavam, String status) {
        ArrayList<Ads> helper = new ArrayList<>();
        for (Ads ad : allAds) {
            if (ad.getVlasnik() == user && ad.isZaposljavam() == zaposljavam && ad.getStatus() == status){
                helper.add(ad);
            }
        }
        return helper;
    }
    public ArrayList<Ads> getAllAds(boolean zaposljavam, String status) {
        ArrayList<Ads> helper = new ArrayList<>();
        for (Ads ad : allAds) {
            if (ad.isZaposljavam() == zaposljavam && ad.getStatus() == status){
                helper.add(ad);
            }
        }
        return helper;
    }
    public ArrayList<Ads> getAllAds(boolean zaposljavam) {
        ArrayList<Ads> helper = new ArrayList<>();
        for (Ads ad : allAds) {
            if (ad.isZaposljavam() == zaposljavam){
                helper.add(ad);
            }
        }
        return helper;
    }
    /*public ArrayList<Ads> fetchAds(){

    }*/

    public void setUpDummyData(){
        ArrayList<String> helper = new ArrayList<>();
        helper.add("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Arbres.jpg/250px-Arbres.jpg");
        helper.add("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Aspen-PopulusTremuloides-2001-09-27.jpg/220px-Aspen-PopulusTremuloides-2001-09-27.jpg");
        allAds.add(new Ads("1", "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")), helper,
                new Date(), "qrcode", true, "OBJAVLJEN"));
        allAds.add(new Ads("1", "Drugi oglas", "Opis drugog oglasa", "Lokacija2", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2", "kategorija3")),
                helper, new Date(), "qrcode", true, "U DOGOVORU"));
        allAds.add(new Ads("1", "Treći oglas", "Opis trećeg oglasa", "Lokacija2", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", true, "DOGOVOREN"));
        allAds.add(new Ads("1", "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", false, "U DOGOVORU"));
        allAds.add(new Ads("1", "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", false, "DOGOVOREN"));
        allAds.add(new Ads("1", "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", false, "IZVRSEN"));
    }
    public void createFirebaseListings(int n){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ads");
        ArrayList<String> helper = new ArrayList<>();
        helper.add("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Arbres.jpg/250px-Arbres.jpg");
        helper.add("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Aspen-PopulusTremuloides-2001-09-27.jpg/220px-Aspen-PopulusTremuloides-2001-09-27.jpg");

        for (int i = 0; i < n; i++) {
            Ads ad = new Ads();
            ad.setNaziv("Oglas" + i);
            ad.setOpis("Opis oglasa " + i);
            ad.setKategorije(new ArrayList<String>(Arrays.asList("-LRlRviJIaUK7LbFHUv-", "-LRlS6iopNAcV66UL-Sb")));
            ad.setStatus("DOGOVOREN");
            ad.setLokacija("Lokacija" + 1);
            ad.setVrijemeKreiranja(new Date());
            ad.setQrCode("QR");
            ad.setZaposljavam(new Random().nextInt() % 2 == 0);
            ad.setId("testni");
            ad.setSlike(helper);
            String key = databaseReference.push().getKey();
            ad.setId(key);
            databaseReference.child(key).setValue(ad);
        }
    }
    public void getAdsByUserId(final String userId, final int mPostsPerPage, boolean zaposljavam,
                                final ArrayList<Ads> ads, final onDataFetched adapter) {
        Query query;
        this.adapter = adapter;
        if (userId == null)
            query = FirebaseDatabase.getInstance().getReference()
                    .child("ads")
                    .orderByKey()
                    .limitToLast(mPostsPerPage);
        else
            query = FirebaseDatabase.getInstance().getReference()
                    .child("ads")
                    .orderByKey()
                    .endAt(userId)
                    .limitToLast(mPostsPerPage);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (userSnapshot.getValue(Ads.class).getId() != userId)
                            ads.add(0, userSnapshot.getValue(Ads.class));
                    }
                    adapter.adapter_success(ads.size() <= mPostsPerPage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adapter.adapter_failure();
            }
        });
    }
}
