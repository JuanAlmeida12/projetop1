package p1.edu.ufcg.worlddiscovery.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.w3c.dom.Comment;

import java.util.List;
import java.util.UUID;

import p1.edu.ufcg.worlddiscovery.adapters.ActivityAdapter;

/**
 * Created by root on 20/09/16.
 */
public class ActivitiesUtils {
    public static final int ACTIVITY_NEW_PLACE = 2;
    public static final int ACTIVITY_NEW_BADGE = 1;
    public static final int ACTIVITY_NEW_PHOTO = 0;
    public static final int ACTIVITY_FISRT_LOGIN = 3;
    public static final int ACTIVITY_PROFILE_UPDATE = 4;

    private static void newActivity(final int type, ParseObject subactivity) {
        ParseObject activity = new ParseObject("Activity");
        final String id = UUID.randomUUID().toString();
        activity.put("type", type);
        activity.put("activityid", id);
        activity.put("owner", ParseUser.getCurrentUser());
        activity.put("subtype", subactivity);
        activity.put("ownerid", ParseUser.getCurrentUser().getUsername());
        activity.saveInBackground();
    }

    public static void newPhoto(ParseFile file, String comment, String place) {
        ParseObject photo = new ParseObject("Post");
        photo.put("photo", file);
        photo.put("message", comment);
        if (null == place || place == "") {
            place = "lugar não identificado";
        }
        photo.put("place", place);
        photo.put("content", "Adicionou uma nova photo em " + place);
        photo.saveInBackground();
        newActivity(ACTIVITY_NEW_PHOTO, photo);
    }

    public static void getUserActivity(String id, final ActivityAdapter adapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Activity");
        query.whereEqualTo("ownerid", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.e("dasdas", list.size()+"");
                for (ParseObject activity : list) {
                    adapter.add(activity);
                }
            }
        });
    }

    public static void getActivityFeed(final ActivityAdapter adapter) {
        ParseRelation follows = ParseUser.getCurrentUser().getRelation("follows");
        final ParseQuery<ParseUser> query = follows.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                ParseQuery<ParseObject> queryFeed = new ParseQuery<ParseObject>("Activity");
                queryFeed.whereNotEqualTo("owner", ParseUser.getCurrentUser());
                queryFeed.whereContainedIn("owner", list);
                queryFeed.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject activity :
                                list) {
                            adapter.add(activity);
                        }
                    }
                });
            }
        });
    }

    public static void getRecentActivities(final ValueEventListener listener) {
    }

    private static String getFomatedName(String displayName) {
        String[] name = displayName.split(" ");
        return name[0] + " " + name[name.length - 1];
    }

    public static void newPlace(String id, String placeName) {
        ParseObject place = new ParseObject("Place");
        place.put("message", "");
        place.put("place", placeName);
        place.put("placeid",id);
        place.put("content", "Fez Checkin em " + placeName);
        place.saveInBackground();
        newActivity(ACTIVITY_NEW_PLACE, place);

        UserUtils.updateScore(40);
    }
}
