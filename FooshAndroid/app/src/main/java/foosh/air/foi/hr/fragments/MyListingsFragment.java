package foosh.air.foi.hr.fragments;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import foosh.air.foi.hr.LoadCompletedListener;
import foosh.air.foi.hr.LoadMoreListener;
import foosh.air.foi.hr.adapters.MyListingsEndlessRecyclerViewAdapter;
import foosh.air.foi.hr.R;
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
        public void loadMore(Listing id, int mPostsPerPage, final LoadCompletedListener loadCompletedListener) {
            Query query;
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
            });
        }
    };

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
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(
                R.layout.fragment_my_listings, container, false);
        recyclerView = swipeRefreshLayout.findViewById(R.id.id_recycle_view);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (mType.equals("OBJAVLJENI")){
            myListingsEndlessRecyclerViewAdapter = new MyListingsEndlessRecyclerViewAdapter(getContext(), recyclerView,
                    swipeRefreshLayout, 10, loadMoreListener);
        }
        else if (mType.equals("PRIJAVLJENI")){
            myListingsEndlessRecyclerViewAdapter = new MyListingsEndlessRecyclerViewAdapter(getContext(), recyclerView,
                    swipeRefreshLayout, 10, loadMoreListener);
        }
        else {
            myListingsEndlessRecyclerViewAdapter = new MyListingsEndlessRecyclerViewAdapter(getContext(), recyclerView,
                    swipeRefreshLayout, 10, loadMoreListener);
        }
        recyclerView.setAdapter(myListingsEndlessRecyclerViewAdapter);
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*mDataset.add(null);
        myListingsEndlessRecyclerViewAdapter.notifyItemInserted(mDataset.size() - 1);
        getAdsById(null, 10);*/
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

    public void createFirebaseListings(int n){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ads");
        ArrayList<String> helper = new ArrayList<>();
        helper.add("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Arbres.jpg/250px-Arbres.jpg");
        helper.add("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Aspen-PopulusTremuloides-2001-09-27.jpg/220px-Aspen-PopulusTremuloides-2001-09-27.jpg");

        for (int i = 0; i < n; i++) {
            Listing listing = new Listing();
            listing.setTitle("Oglas" + i);
            listing.setDescription("Opis oglasa " + i);
            listing.setCategory("kategorija");
            listing.setStatus("DOGOVOREN");
            listing.setLocation("Lokacija" + 1);
            listing.setDateCreated(new Date());
            listing.setQrCode("QR");
            listing.setHiring(new Random().nextInt() % 2 == 0);
            listing.setId("testni");
            listing.setImages(helper);
            String key = databaseReference.push().getKey();
            listing.setId(key);
            databaseReference.child(key).setValue(listing);
        }
    }
}
