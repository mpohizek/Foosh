package foosh.air.foi.hr.model;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Date;

public class Ads {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String naziv;
    private String opis;
    private String lokacija;
    private int vlasnik;
    private ArrayList<String> kategorije;
    private ArrayList<String> slike;
    private Date vrijemeKreiranja;
    private String qrCode;
    private boolean zaposljavam;
    private String status;
    public Ads() {
    }

    public Ads(String id, String naziv, String opis, String lokacija, int vlasnik, ArrayList<String> kategorije, ArrayList<String> slike, Date vrijemeKreiranja, String qrCode, boolean zaposljavam, String status) {
        this.id = id;
        this.naziv = naziv;
        this.opis = opis;
        this.lokacija = lokacija;
        this.vlasnik = vlasnik;
        this.kategorije = kategorije;
        this.slike = slike;
        this.vrijemeKreiranja = vrijemeKreiranja;
        this.qrCode = qrCode;
        this.zaposljavam = zaposljavam;
        this.status = status;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public int getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(int vlasnik) {
        this.vlasnik = vlasnik;
    }

    public ArrayList<String> getKategorije() {
        return kategorije;
    }

    public void setKategorije(ArrayList<String> kategorije) {
        this.kategorije = kategorije;
    }

    public ArrayList<String> getSlike() {
        return slike;
    }

    public void setSlike(ArrayList<String> slike) {
        this.slike = slike;
    }

    public Date getVrijemeKreiranja() {
        return vrijemeKreiranja;
    }

    public void setVrijemeKreiranja(Date vrijemeKreiranja) {
        this.vrijemeKreiranja = vrijemeKreiranja;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public boolean isZaposljavam() {
        return zaposljavam;
    }

    public void setZaposljavam(boolean zaposljavam) {
        this.zaposljavam = zaposljavam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
