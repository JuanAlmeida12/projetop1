package p1.edu.ufcg.worlddiscovery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.adapters.ActivityAdapter;
import p1.edu.ufcg.worlddiscovery.utils.ActivitiesUtils;


public class FeedFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    LinearLayout layoutNoActivities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_feed, container, false);

        mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        ActivityAdapter adapter = new ActivityAdapter(getContext());
        recyclerView.setAdapter(adapter);


        layoutNoActivities = (LinearLayout) mView.findViewById(R.id.layout_no_feed);
        ProgressBar bar = (ProgressBar) mView.findViewById(R.id.progressFeed);
        ActivitiesUtils.getActivityFeed(adapter, layoutNoActivities, bar);


        return mView;
    }
}
