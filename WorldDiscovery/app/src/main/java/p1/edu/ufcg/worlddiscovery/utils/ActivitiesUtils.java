package p1.edu.ufcg.worlddiscovery.utils;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by root on 20/09/16.
 */
public class ActivitiesUtils {
    public static final int ACTIVITY_NEW_PLACE = 0;
    public static final int ACTIVITY_NEW_BADGE = 1;
    public static final int ACTIVITY_NEW_PHOTO = 2;
    public static final int ACTIVITY_FISRT_LOGIN = 3;
    public static final int ACTIVITY_PROFILE_UPDATE = 4;

    private static DatabaseReference activityRef = FirebaseDatabase.getInstance().getReference("activity");

    public static void newActivity(final int type) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String userid = UserUtils.getCurrentUser().getUid();
                DatabaseReference newActivity = activityRef.push();
                Long tsLong = System.currentTimeMillis();
                HashMap<String, Object> update = new HashMap<>();
                update.put("owner", userid);
                update.put("date", tsLong);
                update.put("notificated", 0);
                update.put("content", getActivityMessage(type, UserUtils.getCurrentUser()));
                newActivity.updateChildren(update);
                return null;
            }
        }.execute();
    }

    public static void getUserActivity(String id, ValueEventListener listener) {
        activityRef.orderByChild("owner").equalTo(id).addListenerForSingleValueEvent(listener);
    }

    private static String getActivityMessage(int type, FirebaseUser currentUser) {
        return getFomatedName(currentUser.getDisplayName()) + " " + getActivityMessageType(type);
    }

    public static void getRecentActivities(final ValueEventListener listener) {
        activityRef.orderByChild("date").limitToFirst(20).addListenerForSingleValueEvent(listener);
    }

    private static String getActivityMessageType(int type) {
        switch (type) {
            case ACTIVITY_FISRT_LOGIN:
                return "made his first login";
            case ACTIVITY_NEW_BADGE:
                return "won a new badge";
            case ACTIVITY_NEW_PHOTO:
                return "take a new photo";
            case ACTIVITY_NEW_PLACE:
                return "visited a new place";
            case ACTIVITY_PROFILE_UPDATE:
                return "update their profile";
        }
        return null;
    }

    private static String getFomatedName(String displayName) {
        String[] name = displayName.split(" ");
        return name[0] + " " + name[name.length - 1];
    }

}
