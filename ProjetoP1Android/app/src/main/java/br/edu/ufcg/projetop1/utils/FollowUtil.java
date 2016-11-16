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

import br.edu.ufcg.projetop1.core.Follow;
import br.edu.ufcg.projetop1.core.UserAction;

/**
 * Created by root on 23/08/16.
 */
public class FollowUtil {
    private static List<String> follows = new ArrayList<String>();
    private static FirebaseDatabase database;
    private static Map<String, String> mapFollow = new HashMap<String, String>();
    private static DatabaseReference refFollow;
    private static DatabaseReference refFollowUser;
    private static DatabaseReference refAction;
    private static String id;

    public static void listenFollowers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database = FirebaseDatabase.getInstance();
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                refFollow = database.getReference("follow");
                refAction = database.getReference("action");
                refFollowUser = database.getReference("follow-ser/" + id);
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
                refFollowUser.addChildEventListener(listener);
            }
        }).start();
    }

    public static boolean following(String uuid) {
        return follows.contains(uuid);
    }

    public static void follow(String uuid, String token) {
        String key = refFollow.push().getKey();
        Follow newFollow = new Follow(token, uuid, id);
        refFollow.child(key).setValue(newFollow);
        refFollowUser.push().setValue(uuid);
        UserAction action = new UserAction(id, ActionUtils.ACTION_NEW_FOLLOW, uuid);
        refAction.push().setValue(action);
    }

    public static void unfollow(String uuid) {
        follows.remove(uuid);
        DatabaseReference tempRef = refFollowUser.child(mapFollow.get(uuid));
        tempRef.removeValue();
    }
}
