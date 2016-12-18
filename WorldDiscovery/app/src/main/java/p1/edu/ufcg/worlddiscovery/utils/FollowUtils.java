package p1.edu.ufcg.worlddiscovery.utils;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;

/**
 * Created by root on 04/10/16.
 */
public class FollowUtils {


    public static void init() {
    }

    public static void followAction(String id) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", id);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                ParseRelation relation = ParseUser.getCurrentUser().getRelation("follows");
                relation.add(list.get(0));
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

    }

    public static boolean following(String id) {
        return false;
    }
}
