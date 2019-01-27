package foosh.air.foi.hr.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import foosh.air.foi.hr.LoadCompletedListener;
import foosh.air.foi.hr.LoadMoreListener;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.adapters.MyListingsEndlessRecyclerViewAdapter;
import foosh.air.foi.hr.model.Listing;

public class MyListingsFragment extends Fragment{

    public interface onFragmentInteractionListener{
        void onFragmentInteraction(Fragment fragment);
    }

    private static final String KEY_PREFIX = "foosh.air.foi.hr.MyListingsFragment.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "type-key";

    private MyListingsEndlessRecyclerViewAdapter myListingsEndlessRecyclerViewAdapter;
    private onFragmentInteractionListener mListener;
    private String mType;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreListener loadMoreListener = new LoadMoreListener() {
        @Override
        public void loadMore(Listing last, int startAt, int limit, final LoadCompletedListener loadCompletedListener) {
            final Listing l = last;
            Map<String, String> data = new HashMap<>();
            if (last == null) {
                data.put("hiring", "true");
                data.put("ownerId", FirebaseAuth.getInstance().getUid());
            } else {
                data.put("hiring", String.valueOf(l.isHiring()));
                data.put("ownerId", l.getOwnerId());
            }

            data.put("startAt", String.valueOf(startAt));
            data.put("limit", String.valueOf(limit));

            FirebaseFunctions.getInstance().getHttpsCallable("api/mylistings")
                    .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                @Override
                public void onSuccess(HttpsCallableResult httpsCallableResult) {
                    ArrayList<Listing> listings = new ArrayList<>();
                    ArrayList<HashMap> result = (ArrayList<HashMap>) httpsCallableResult.getData();
                    if (httpsCallableResult.getData() != null) {
                        for (HashMap listingHashMap : result) {
                            Listing listing = listingHashMapToListing(listingHashMap);
                            listings.add(listing);
                        }
                    }
                    loadCompletedListener.onLoadCompleted(listings);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadCompletedListener.onLoadCompleted(new ArrayList<Listing>());
                }
            });
        }
    };

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
            listing.setActive((boolean) listingHashMap.get("active"));
        }
        if(listingHashMap.get("applications") != null){
            listing.setApplications((HashMap<String, String>) listingHashMap.get("applications"));
        }
        if(listingHashMap.get("applicant") != null){
            listing.setApplicant((HashMap<String, Integer>) listingHashMap.get("applicant"));
        }
        return listing;
    }

    public MyListingsFragment() {
        // Required empty public constructor
    }

    public static MyListingsFragment getInstance(String type) {
        MyListingsFragment fragment = new MyListingsFragment();

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

        if (mType.equals("OBJAVLJENI")){
            myListingsEndlessRecyclerViewAdapter = new MyListingsEndlessRecyclerViewAdapter(true, getContext(), recyclerView,
                    swipeRefreshLayout, 10, loadMoreListener);
        }
        else if (mType.equals("PRIJAVLJENI")){
            myListingsEndlessRecyclerViewAdapter = new MyListingsEndlessRecyclerViewAdapter(false, getContext(), recyclerView,
                    swipeRefreshLayout, 10, loadMoreListener);
        }
        else {
            myListingsEndlessRecyclerViewAdapter = new MyListingsEndlessRecyclerViewAdapter(true, getContext(), recyclerView,
                    swipeRefreshLayout, 10, loadMoreListener);
        }
        recyclerView.setAdapter(myListingsEndlessRecyclerViewAdapter);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getType() {
        return mType;
    }
}
