package foosh.air.foi.hr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import foosh.air.foi.hr.MyAdsEndlessRecyclerViewAdapter;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.Ads;

public class MyAdsFragment extends Fragment{

    public interface onFragmentInteractionListener{
        void onFragmentInteraction(Fragment fragment);
    }

    private ArrayList<Ads> mDataset = new ArrayList<>();

    private static final String KEY_PREFIX = "foosh.air.foi.hr.MyAdsFragment.";
    private static final String ARG_TYPE_KEY = KEY_PREFIX + "type-key";

    private MyAdsEndlessRecyclerViewAdapter myAdsEndlessRecyclerViewAdapter;
    private onFragmentInteractionListener mListener;
    private String mType;

    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    public MyAdsFragment() {
        // Required empty public constructor
    }

    public static MyAdsFragment getInstance(String type) {
        MyAdsFragment fragment = new MyAdsFragment();

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
                R.layout.fragment_my_ads, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.id_recycle_view);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        if (mType == "OBJAVLJENI"){
            myAdsEndlessRecyclerViewAdapter = new MyAdsEndlessRecyclerViewAdapter(getContext(), mDataset, 10);
        }
        else if (mType == "PRIJAVLJENI"){
            myAdsEndlessRecyclerViewAdapter = new MyAdsEndlessRecyclerViewAdapter(getContext(), mDataset, 10);
        }
        else {
            myAdsEndlessRecyclerViewAdapter = new MyAdsEndlessRecyclerViewAdapter(getContext(), mDataset, 10);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                Log.d("totalItemCount", String.valueOf(totalItemCount));
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.d("lastVisibleItem", String.valueOf(lastVisibleItem));
                for (Ads ads : mDataset) {
                    Log.d("mDataset", ads != null ? ads.getNaziv() : "null");
                }
                Log.d("mDataset", mDataset.toString());
                Log.d("lastItem", myAdsEndlessRecyclerViewAdapter.getLastItem() != null ? myAdsEndlessRecyclerViewAdapter.getLastItem().getId() : "null");
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    recyclerView.post(new Runnable() {
                        public void run() {
                            String last = myAdsEndlessRecyclerViewAdapter.getLastItem() != null ? myAdsEndlessRecyclerViewAdapter.getLastItem().getId() : null;
                            mDataset.add(null);
                            myAdsEndlessRecyclerViewAdapter.notifyItemInserted(mDataset.size() - 1);
                            isLoading = true;
                            getAdsById(last, 10);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(myAdsEndlessRecyclerViewAdapter);
        mDataset.add(null);
        myAdsEndlessRecyclerViewAdapter.notifyItemInserted(mDataset.size() - 1);
        isLoading = true;
        getAdsById(null, 10);
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*mDataset.add(null);
        myAdsEndlessRecyclerViewAdapter.notifyItemInserted(mDataset.size() - 1);
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
        mDataset.clear();
        mDataset = null;
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
    public void getAdsById(final String id, final int mPostsPerPage) {
        Query query;
        if (id == null)
            query = FirebaseDatabase.getInstance().getReference()
                    .child("ads")
                    .orderByKey()
                    .limitToLast(mPostsPerPage);
        else
            query = FirebaseDatabase.getInstance().getReference()
                    .child("ads")
                    .orderByKey()
                    .endAt(id)
                    .limitToLast(mPostsPerPage);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("string_id", id != null ? id : "null");
                mDataset.remove(mDataset.size() - 1);
                myAdsEndlessRecyclerViewAdapter.notifyItemRemoved(mDataset.size());
                int oldSize = mDataset.size();
                if (dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        mDataset.add(oldSize != 0 ? oldSize - 1 : oldSize, userSnapshot.getValue(Ads.class));
                    }

                    myAdsEndlessRecyclerViewAdapter.notifyItemRangeInserted(oldSize, (int)dataSnapshot.getChildrenCount());
                    isLoading = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isLoading = false;
            }
        });
    }
}
