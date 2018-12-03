package foosh.air.foi.hr.model;

import android.net.Uri;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import foosh.air.foi.hr.onAdsDelivered;

public class AdsManager {
    private ArrayList<Ads> allAds;
    private onAdsDelivered listener;

    {
        allAds = new ArrayList<>();
    }
    public AdsManager(onAdsDelivered listener) {
        this.listener = listener;
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
            if (ad.getVlasnik() == user && ad.isZaposljavam() == zaposljavam && ad.getStatus().second == status){
                helper.add(ad);
            }
        }
        return helper;
    }
    public ArrayList<Ads> getAllAds(boolean zaposljavam, String status) {
        ArrayList<Ads> helper = new ArrayList<>();
        for (Ads ad : allAds) {
            if (ad.isZaposljavam() == zaposljavam && ad.getStatus().second == status){
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
    public boolean fetchAds(){
        return false;
    }

    public void setUpDummyData(){
        ArrayList<Uri> helper = new ArrayList<>();
        helper.add(Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Arbres.jpg/250px-Arbres.jpg"));
        helper.add(Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Aspen-PopulusTremuloides-2001-09-27.jpg/220px-Aspen-PopulusTremuloides-2001-09-27.jpg"));
        allAds.add(new Ads(1, "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")), helper,
                new Date(), "qrcode", true, new Pair<Date, String>(new Date(), "OBJAVLJEN")));
        allAds.add(new Ads(1, "Drugi oglas", "Opis drugog oglasa", "Lokacija2", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2", "kategorija3")),
                helper, new Date(), "qrcode", true, new Pair<Date, String>(new Date(), " U DOGOVORU")));
        allAds.add(new Ads(1, "Treći oglas", "Opis trećeg oglasa", "Lokacija2", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", true, new Pair<Date, String>(new Date(), "DOGOVOREN")));
        allAds.add(new Ads(1, "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", false, new Pair<Date, String>(new Date(), "U DOGOVORU")));
        allAds.add(new Ads(1, "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", false, new Pair<Date, String>(new Date(), "DOGOVOREN")));
        allAds.add(new Ads(1, "Prvi oglas", "Opis prvog oglasa", "Lokacija1", 1, new ArrayList<String>(Arrays.asList("kategorija1", "kategorija2")),
                helper, new Date(), "qrcode", false, new Pair<Date, String>(new Date(), "IZVRSEN")));
    }
}
