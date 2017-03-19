package p1.edu.ufcg.worlddiscovery.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.core.Badge;

public class BadgesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_badges, container, false);

        GridLayout badgesGrid = (GridLayout) view.findViewById(R.id.badges_grid);
        List<Badge> badges = getBadges();

        for (int i = 0; i < badges.size(); i++){
            Badge badge = badges.get(i);
            int level = badge.getLevel();
            int type = badge.getType();

            badgesGrid.getChildAt(type * 3 + level).setAlpha(1);
        }
        

        return view;
    }

    private List<Badge> getBadges() {
        final List<Badge> badges = new ArrayList<Badge>();

        ParseQuery query = new ParseQuery("BadgeProgessResume");
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject obj: list){
                    try {
                        String title = obj.getParseObject("badgeModel").fetchIfNeeded().getString("title");
                        String description = obj.getParseObject("badgeModel").fetchIfNeeded().getString("message");
                        int type = obj.getParseObject("badgeModel").fetchIfNeeded().getInt("type");
                        int level = 1;

                        Badge badge = new Badge(title,description,type,level);
                        badges.add(badge);

                        Log.d("aaaaaaaaaaaaaaa",obj.getParseObject("badgeModel").fetchIfNeeded().getString("title"));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        return badges;
    }

}
