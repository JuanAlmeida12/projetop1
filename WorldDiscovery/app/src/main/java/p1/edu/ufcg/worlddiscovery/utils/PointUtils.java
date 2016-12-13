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

    public static void getUserPoints(String uid, final ValueEventListener listener) {
    }

    public static void getAllPoints(ValueEventListener listener) {
    }

    public static void getPoint(String pid, ValueEventListener listener) {
    }

    public static void visitedPoint(String id) {
        ActivitiesUtils.newPlace(id);
    }

    public static void searchPoint(String query, ChildEventListener listener) {
    }

}
