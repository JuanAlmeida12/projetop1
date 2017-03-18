package p1.edu.ufcg.worlddiscovery.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;

public class BadgesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        ParseQuery query = new ParseQuery("BadgeProgessResume");
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject obj: list){
                    try {
                        Log.d("aaaaaaaaaaaaaaa",obj.getParseObject("badgeModel").fetchIfNeeded().getString("title"));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

}
