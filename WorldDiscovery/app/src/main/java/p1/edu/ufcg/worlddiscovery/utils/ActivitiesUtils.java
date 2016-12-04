package p1.edu.ufcg.worlddiscovery.utils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    }
    public static void getUserActivity(String id, ValueEventListener listener) {
    }

    private static String getActivityMessage(int type, FirebaseUser currentUser) {
        return "";
    }

    public static void getRecentActivities(final ValueEventListener listener) {
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
