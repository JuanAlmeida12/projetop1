package p1.edu.ufcg.worlddiscovery.utils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;

/**
 * Created by root on 20/09/16.
 */
public class UserUtils {


    public static void updateUser(final HashMap<String, Object> update) {

    }

    public static void getUser(String uid, FindCallback<ParseUser> callback) {
        ParseQuery<ParseUser> query = new ParseQuery<ParseUser>(ParseUser.class);
        query.whereEqualTo("username", uid);
        query.findInBackground(callback);
    }

    public static void search(String name, ChildEventListener litenner) {
    }

    public static void updateScore(final int score) {
        int current_score = ParseUser.getCurrentUser().getInt("score");
        ParseUser.getCurrentUser().put("score", current_score == Integer.MIN_VALUE ? current_score + score : score);
        ParseUser.getCurrentUser().saveInBackground();
    }

    public static String formattedName(String name) {
        String[] tmp = name.split(" ");
        return tmp[0] + " " + tmp[tmp.length - 1];
    }

}
