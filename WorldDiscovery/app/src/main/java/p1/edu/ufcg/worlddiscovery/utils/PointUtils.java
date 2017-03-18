package p1.edu.ufcg.worlddiscovery.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.adapters.UserPlacesAdapter;
import p1.edu.ufcg.worlddiscovery.core.User;

/**
 * Created by root on 20/09/16.
 */
public class PointUtils {

    private static List<HashMap<String, String>> nearbyPlaces = new ArrayList<>();

    public static void getUserPoints(String uid, final UserPlacesAdapter adapter, final View emptyLayout, final ProgressBar bar) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Place");
        query.whereEqualTo("ownerid", uid);
        bar.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                bar.setVisibility(View.GONE);
                if (list.isEmpty()) {
                    emptyLayout.setVisibility(View.VISIBLE);
                } else {
                    emptyLayout.setVisibility(View.GONE);
                }

                for (ParseObject data : list){
                    adapter.addPoint(data);
                }

            }
        });
    }

    public static void setNeabyPlaces(List<HashMap<String, String>> places) {
        nearbyPlaces = places;
    }

    public static HashMap<String, String> getCurrentLocal(double lat, double lng) {
        double lessDistance = Double.MAX_VALUE;
        HashMap<String, String> current = null;
        for (HashMap<String, String> place : nearbyPlaces) {
            LatLng myPosition = new LatLng(lat, lng);
            LatLng placePosition = new LatLng(Double.parseDouble(place.get("lat")), Double.parseDouble(place.get("lng")));
            double distance = SphericalUtil.computeDistanceBetween(myPosition, placePosition);
            if (distance < lessDistance) {
                lessDistance = distance;
                current = place;
            }
        }

        Log.e("ddasdas","distancia: "+ lessDistance);
//        if (lessDistance > 100) {
//            current = null;
//        }

        return current;
    }

    public static void getAllPoints(ValueEventListener listener) {
    }

    public static void getPoint(String pid, ValueEventListener listener) {
    }

    public static void visitedPoint(String id, String name, String[] tags) {
        ActivitiesUtils.newPlace(id, name, tags);
    }

    public static void searchPoint(String query, ChildEventListener listener) {
    }

}
