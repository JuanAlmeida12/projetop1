package p1.edu.ufcg.worlddiscovery.utils;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by root on 04/10/16.
 */
public class FollowUtils {

    private static HashMap<String, DatabaseReference> following = new HashMap<>();

    private static DatabaseReference followRef = FirebaseDatabase.getInstance().getReference("follow");

    public static void init() {
        followRef.orderByChild("follower").equalTo(UserUtils.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ff", dataSnapshot.child("following").getValue(String.class));
                following.put(dataSnapshot.child("following").getValue(String.class), dataSnapshot.getRef());
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
        });
    }

    public static void followAction(String id){
        if(following(id)){
            following.get(id).removeValue();
            following.remove(id);
        } else {
            DatabaseReference newFollow = followRef.push();
            HashMap<String,Object> update = new HashMap<>();
            update.put("follower",UserUtils.getCurrentUser().getUid());
            update.put("following", id);
            update.put("date", System.currentTimeMillis());
            newFollow.updateChildren(update);
        }

    }

    public static boolean following(String id) {
        return following.containsKey(id);
    }
}
