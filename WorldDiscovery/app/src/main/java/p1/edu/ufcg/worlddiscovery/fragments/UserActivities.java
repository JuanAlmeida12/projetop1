package p1.edu.ufcg.worlddiscovery.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.adapters.ActivityAdapter;
import p1.edu.ufcg.worlddiscovery.adapters.SearchAdapter;
import p1.edu.ufcg.worlddiscovery.core.SimpleDividerItemDecoration;
import p1.edu.ufcg.worlddiscovery.utils.ActivitiesUtils;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserActivities.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserActivities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserActivities extends Fragment {
    private static final String ARG_USERID = "userid";

    private String userId;

    public UserActivities() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UserActivities.
     */
    // TODO: Rename and change types and number of parameters
    public static UserActivities newInstance(String param1) {
        UserActivities fragment = new UserActivities();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_activities, container, false);
        final ActivityAdapter adapter = new ActivityAdapter(getContext());
//        UserUtils.getUser(userId, new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot user) {
//                ActivitiesUtils.getUserActivity(userId, new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
//                        while (i.hasNext()) {
//                            adapter.add(i.next(),user);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

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
