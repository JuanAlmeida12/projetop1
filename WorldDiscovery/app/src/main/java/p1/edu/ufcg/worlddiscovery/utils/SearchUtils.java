package p1.edu.ufcg.worlddiscovery.utils;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.adapters.SearchAdapter;
import p1.edu.ufcg.worlddiscovery.core.Point;

/**
 * Created by root on 29/09/16.
 */
public class SearchUtils {

    public static void search(String query, final SearchAdapter adapter) {
        ParseQuery<ParseUser> query1 = new ParseQuery(ParseUser.class);
        query1.whereContains("name", query);
        query1.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    Log.e("dasd", list.size()+"");
                    for (ParseUser user : list) {
                        Log.e("dasd", user.getUsername());
                        adapter.addUser(user);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
