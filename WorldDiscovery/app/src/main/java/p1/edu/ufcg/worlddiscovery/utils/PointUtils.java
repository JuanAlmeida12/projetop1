package p1.edu.ufcg.worlddiscovery.utils;

import android.os.AsyncTask;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import p1.edu.ufcg.worlddiscovery.core.User;

/**
 * Created by root on 20/09/16.
 */
public class PointUtils {
    private static DatabaseReference pointRef = FirebaseDatabase.getInstance().getReference("posts");
    private static DatabaseReference userPointRef = FirebaseDatabase.getInstance().getReference("user-place");

    public static void getUserPoints(String uid, final ValueEventListener listener) {
        userPointRef.orderByChild("user").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                            while (i.hasNext()) {
                                DataSnapshot point = i.next();
                                getPoint(point.child("codplace").getValue(String.class), listener);
                            }
                            return null;
                        }
                    }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getAllPoints(ValueEventListener listener) {
        pointRef.addListenerForSingleValueEvent(listener);
    }

    public static void getPoint(String pid, ValueEventListener listener) {
        pointRef.child(pid).addListenerForSingleValueEvent(listener);
    }

    public static void visitedPoint(String id) {
        String userid = UserUtils.getCurrentUser().getUid();
        DatabaseReference newUserPlace = userPointRef.push();
        HashMap<String, Object> data = new HashMap();
        Long tsLong = System.currentTimeMillis();
        data.put("codplace", id);
        data.put("user", userid);
        data.put("date", tsLong);
        newUserPlace.updateChildren(data);
        UserUtils.updateScore(GeofenceUtils.getBindingPoint(id).score);

    }

    public static void searchPoint(String query, ChildEventListener listener) {
        pointRef.orderByChild("placeName").startAt(query).addChildEventListener(listener);
    }

}
