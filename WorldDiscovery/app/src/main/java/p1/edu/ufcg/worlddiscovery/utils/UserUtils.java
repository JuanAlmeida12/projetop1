package p1.edu.ufcg.worlddiscovery.utils;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by root on 20/09/16.
 */
public class UserUtils {
    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");

    public static void loginUpdate(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final FirebaseUser currentUser = getCurrentUser();
                final HashMap<String,Object> update = new HashMap<String,Object>();
                final Long tsLong = System.currentTimeMillis();
                update.put("lastLogin", tsLong);
                getUser(currentUser.getUid(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            dataSnapshot.getRef().updateChildren(update);
                        } else {
                            update.put("createdAt", tsLong);
                            update.put("name", currentUser.getDisplayName());
                            update.put("photoURL",currentUser.getPhotoUrl().toString());
                            update.put("numPosts",0);
                            update.put("numBadges",0);
                            update.put("numPlaces",0);
                            update.put("message","");
                            update.put("city","");
                            update.put("score",0);
                            userRef.child(currentUser.getUid()).updateChildren(update);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return null;
            }
        }.execute();
    }

    public static void updateUser(final HashMap<String,Object> update) {
        getUser(getCurrentUser().getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().updateChildren(update);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void getUser(String uid, ValueEventListener listener) {
        userRef.child(uid).addListenerForSingleValueEvent(listener);
    }

    public static void search(String name, ChildEventListener litenner){
        userRef.orderByChild("name").startAt(name).addChildEventListener(litenner);
    }

    public static void updateScore(final int score) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getUser(getCurrentUser().getUid(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int scoreAtual = dataSnapshot.child("score").getValue(Integer.class);
                        scoreAtual = scoreAtual + score;
                        HashMap<String, Object> update = new HashMap();
                        update.put("score", scoreAtual);
                        dataSnapshot.getRef().updateChildren(update);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return null;
            }
        }.execute();
    }

    public static String formattedName(String name) {
        String[] tmp = name.split(" ");
        return tmp[0]+" " +tmp[tmp.length -1];
    }

}
