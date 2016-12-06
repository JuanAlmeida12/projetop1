package p1.edu.ufcg.worlddiscovery.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.adapters.ActivityAdapter;
import p1.edu.ufcg.worlddiscovery.core.SimpleDividerItemDecoration;
import p1.edu.ufcg.worlddiscovery.utils.ActivitiesUtils;
import p1.edu.ufcg.worlddiscovery.utils.FollowUtils;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecentActivities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentActivities extends Fragment {

    public RecentActivities() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecentActivities.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentActivities newInstance() {
        RecentActivities fragment = new RecentActivities();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_activities, container, false);
        final ActivityAdapter adapter = new ActivityAdapter(getContext());
        ActivitiesUtils.getRecentActivities(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    final DataSnapshot activity = i.next();
                    if (FollowUtils.following(activity.child("owner").getValue(String.class))) {
//                        UserUtils.getUser(activity.child("owner").getValue(String.class), new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot user) {
//                                if (user.exists()) {
//                                    Log.d("ffg", user.child("photoURL").getValue(String.class));
//                                    adapter.add(activity, user);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        return v;
    }
}
