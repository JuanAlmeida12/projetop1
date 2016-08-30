package br.edu.ufcg.projetop1.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 23/08/16.
 */
public class FollowUtil {
    private static List<String> follows = new ArrayList<String>();
    private static FirebaseDatabase database;
    private static Map<String, String> mapFollow = new HashMap<String, String>();
    private static DatabaseReference refFollow;

    public static void listenFollowers() {
        database = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        refFollow = database.getReference("follow/" + id);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String followId = dataSnapshot.getValue(String.class);
                follows.add(followId);
                mapFollow.put(followId, dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refFollow.addChildEventListener(listener);

    }

    public static boolean following(String uuid) {
        return follows.contains(uuid);
    }

    public static void follow(String uuid) {
        refFollow.push().setValue(uuid);
    }

    public static void unfollow(String uuid) {
        follows.remove(uuid);
        DatabaseReference tempRef = refFollow.child(mapFollow.get(uuid));
        tempRef.removeValue();
    }
}
