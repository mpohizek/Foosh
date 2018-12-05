package foosh.air.foi.hr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import foosh.air.foi.hr.MyAdActivity;
import foosh.air.foi.hr.MyAdsEndlessRecyclerViewAdapter;
import foosh.air.foi.hr.MyAdsRecyclerViewAdapter;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.AdsManager;

public class MyAdsFragment extends Fragment {

    private static final String KEY_PREFIX = "foosh.air.foi.hr.MyAdsFragment.";

    private static final String ARG_TYPE_KEY = KEY_PREFIX + "type-key";

    private OnFragmentInteractionListener mListener;
    private String mType;
    private AdsManager adsManager;
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
        Log.d("onCreate", "usao");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE_KEY);
            adsManager = mListener.onFragmentInteraction(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_my_ads, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.id_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final MyAdsEndlessRecyclerViewAdapter myAdsEndlessRecyclerViewAdapter;
        if (mType == "OBJAVLJENI"){
            myAdsEndlessRecyclerViewAdapter = new MyAdsEndlessRecyclerViewAdapter(getContext(), adsManager.getAllAds(), recyclerView, 10);
        }
        else if (mType == "PRIJAVLJENI"){
            myAdsEndlessRecyclerViewAdapter = new MyAdsEndlessRecyclerViewAdapter(getContext(), adsManager.getAllAds(), recyclerView, 10);
        }
        else {
            myAdsEndlessRecyclerViewAdapter = new MyAdsEndlessRecyclerViewAdapter(getContext(), adsManager.getAllAds(), recyclerView, 10);
        }
        recyclerView.setAdapter(myAdsEndlessRecyclerViewAdapter);
        myAdsEndlessRecyclerViewAdapter.setOnLoadMoreListener(new MyAdsEndlessRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adsManager.getAdsByUserId(adsManager.getLastKey(), 10,true,
                        adsManager.getAllAds(), myAdsEndlessRecyclerViewAdapter);
            }
        });

        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        AdsManager onFragmentInteraction(Fragment fragment);
    }

    public String getType() {
        return mType;
    }
}
