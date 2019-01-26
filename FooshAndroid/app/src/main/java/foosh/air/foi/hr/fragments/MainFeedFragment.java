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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import foosh.air.foi.hr.LoadCompletedListener;
import foosh.air.foi.hr.MainFeedLoadMoreListener;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.adapters.MainFeedListingsEndlessRecyclerViewAdapter;
import foosh.air.foi.hr.model.Listing;

public class MainFeedFragment extends Fragment {
    public interface onFragmentInteractionListener{
        void onFragmentInteraction(Fragment fragment);
    }


    private static final String KEY_PREFIX = "foosh.air.foi.hr.MainActivity.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "MainFeedFragment";
    private onFragmentInteractionListener mListener;

    private MainFeedListingsEndlessRecyclerViewAdapter mainFeedListingsEndlessRecyclerViewAdapter;
    private String mType;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainFeedLoadMoreListener mainFeedLoadMoreListener = new MainFeedLoadMoreListener() {
        @Override
        public void loadMore(Listing last, int startAt, int limit, final LoadCompletedListener loadCompletedListener){//(Listing last, int mPostsPerPage, final LoadCompletedListener loadCompletedListener) {
            final Listing l = last;
            Map<String, String> data = new HashMap<>();
            if(last == null){
                data.put("hiring","true");
                data.put("ownerId",FirebaseAuth.getInstance().getUid());
            }else{
                data.put("hiring", String.valueOf(l.isHiring()));
                data.put("ownerId", l.getOwnerId());
            }
            data.put("startAt", String.valueOf(startAt));
            data.put("limit", String.valueOf(limit));

            FirebaseFunctions.getInstance().getHttpsCallable("api/mainfeed")
                    .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                @Override
                public void onSuccess(HttpsCallableResult httpsCallableResult) {
                    ArrayList<Listing> listings = new ArrayList<>();
                    Object o = httpsCallableResult.getData();
                    if(httpsCallableResult.getData() != null){
                        /*for(Object l: o){
                            listings.add(0,l);
                        }*/
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadCompletedListener.onLoadCompleted(new ArrayList<Listing>());
                }
            });

            /*Query query;
            final String key = id != null ? id.getId() : null;
            if (key == null)
                query = FirebaseDatabase.getInstance().getReference()
                        .child("listings")
                        .orderByKey()
                        .limitToLast(mPostsPerPage);
            else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("listings")
                        .orderByKey()
                        .endAt(key)
                        .limitToLast(mPostsPerPage);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Listing> listings = new ArrayList<>();
                    if (dataSnapshot.exists()){
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Log.d("key-id", userSnapshot.getValue(Listing.class).getId());
                            if (!userSnapshot.getValue(Listing.class).getId().equals(key))
                                listings.add(0, userSnapshot.getValue(Listing.class));
                        }
                        loadCompletedListener.onLoadCompleted(listings);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    loadCompletedListener.onLoadCompleted(new ArrayList<foosh.air.foi.hr.model.Listing>());
                }
            });*/
        }
    };

    public MainFeedFragment() {
        // Required empty public constructor
    }

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
            mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(true, getContext(), recyclerView,
                    swipeRefreshLayout, 10, mainFeedLoadMoreListener);
            //mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(getContext(), recyclerView,
            //        swipeRefreshLayout, 10, mainFeedLoadMoreListener);
        }
        else if (mType.equals("PRIJAVLJENI")){
            mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(false, getContext(), recyclerView,
                    swipeRefreshLayout, 10, mainFeedLoadMoreListener);
            //mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(getContext(), recyclerView,
            //        swipeRefreshLayout, 10, mainFeedLoadMoreListener);
        }
        else {
            mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(true, getContext(), recyclerView,
                    swipeRefreshLayout, 10, mainFeedLoadMoreListener);
            //mainFeedListingsEndlessRecyclerViewAdapter = new MainFeedListingsEndlessRecyclerViewAdapter(getContext(), recyclerView,
            //        swipeRefreshLayout, 10, mainFeedLoadMoreListener);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getType() {
        return mType;
    }
}
