package foosh.air.foi.hr.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import foosh.air.foi.hr.interfaces.DataDelivered;
import foosh.air.foi.hr.interfaces.LoadCompletedListener;
import foosh.air.foi.hr.interfaces.MainFeedLoadMoreListener;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.adapters.MainFeedListingsEndlessRecyclerViewAdapter;
import foosh.air.foi.hr.model.Listing;

public class MainFeedFragment extends Fragment implements DataDelivered {
    /**
     * Ponovno učitavanje oglasa s prikazom od početne pozicije
     */
    @Override
    public void onDataDelivered() {
        mainFeedListingsEndlessRecyclerViewAdapter.sortOperation();
    }

    /**
     * Sučelje za oslušivanje interakcija s fragmentom
     */
    public interface onFragmentInteractionListener{
        NavigationView getNavigationView();
        void getHashMapValues(Map<String, String> hashMap);
    }

    private static final String KEY_PREFIX = "foosh.air.foi.hr.activities.MainActivity.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "MainFeedFragment";
    private onFragmentInteractionListener mListener;

    private MainFeedListingsEndlessRecyclerViewAdapter mainFeedListingsEndlessRecyclerViewAdapter;
    private String mType;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainFeedLoadMoreListener mainFeedLoadMoreListener = new MainFeedLoadMoreListener() {
        /**
         * Pozivanje Firebase funkcije za dohvaćanje popisa oglasa.
         * @param hiring
         * @param last
         * @param startAt
         * @param limit
         * @param loadCompletedListener
         */
        @Override
        public void loadMore(boolean hiring, Listing last, int startAt, int limit, final LoadCompletedListener loadCompletedListener){
            Map<String, String> data = new HashMap<>();
            mListener.getHashMapValues(data);

            data.put("hiring", hiring ? "1" : "");
            data.put("skip", String.valueOf(startAt));
            data.put("limit", String.valueOf(limit));

            FirebaseFunctions.getInstance().getHttpsCallable("api/mainfeed")
                    .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                /**
                 * Spremanje dohvaćenih oglasa u listu oglasa
                 * @param httpsCallableResult
                 */
                @Override
                public void onSuccess(HttpsCallableResult httpsCallableResult) {
                    ArrayList<Listing> listings = new ArrayList<>();
                    ArrayList<HashMap> result = (ArrayList<HashMap>) httpsCallableResult.getData();
                    if(httpsCallableResult.getData() != null){
                        for (HashMap listingHashMap : result){
                            Listing listing = listingHashMapToListing(listingHashMap);
                            listings.add(listing);
                        }
                    }
                    loadCompletedListener.onLoadCompleted(listings);
                }
            }).addOnFailureListener(new OnFailureListener() {
                /**
                 * Vraćanje prazne liste oglasa u slučaju pogreške s Firebase funkcijom.
                 * @param e
                 */
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadCompletedListener.onLoadCompleted(new ArrayList<Listing>());
                }
            });
        }
    };

    /**
     * Pretvaranje oglasa iz HashMap oblika u objekt klase Listing
     * @param listingHashMap
     * @return
     */
    private Listing listingHashMapToListing(HashMap listingHashMap) {
        Listing listing = new Listing();
        listing.setCategory((String) listingHashMap.get("category"));
        listing.setDateCreated((String) listingHashMap.get("dateCreated"));
        listing.setDescription((String) listingHashMap.get("description"));
        listing.setHiring((Boolean) listingHashMap.get("hiring"));
        listing.setId((String) listingHashMap.get("id"));
        listing.setImages((ArrayList<String>) listingHashMap.get("images"));
        listing.setLocation((String) listingHashMap.get("location"));
        listing.setOwnerId((String) listingHashMap.get("ownerId"));
        listing.setPrice((Integer) listingHashMap.get("price"));
        listing.setQrCode((String) listingHashMap.get("qrCode"));
        listing.setStatus((String) listingHashMap.get("status"));
        listing.setTitle((String) listingHashMap.get("title"));
        if(listingHashMap.get("active") != null){
            listing.setActive((Boolean) listingHashMap.get("active"));
        }
        if(listingHashMap.get("applications") != null){
            listing.setApplications((HashMap<String, String>) listingHashMap.get("applications"));
        }
        if(listingHashMap.get("applicant") != null){
            listing.setApplicant((HashMap<String, Integer>) listingHashMap.get("applicant"));
        }
        return listing;
    }

    public MainFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Za dohvaćanje instance fragmenta.
     * @param type
     * @return
     */
    public static MainFeedFragment getInstance(String type) {
        MainFeedFragment fragment = new MainFeedFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_TYPE_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE_KEY);
        }
    }

    /***
     * Kreiranje View-a za prikaz oglasa glavnog feed-a
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_my_listings, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN);
        recyclerView = view.findViewById(R.id.id_recycle_view);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        boolean isOwner = mType.equals("OBJAVLJENI");
        mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(
                isOwner, getContext(), recyclerView, swipeRefreshLayout, 10,
                mainFeedLoadMoreListener, mListener.getNavigationView());
        recyclerView.setAdapter(mainFeedListingsEndlessRecyclerViewAdapter);
        return swipeRefreshLayout;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentInteractionListener) {
            mListener = (onFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Odjavljivanje listenera mListener nakon izlaska iz fragmenta
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getType() {
        return mType;
    }
}
