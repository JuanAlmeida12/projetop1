package p1.edu.ufcg.worlddiscovery.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.UUID;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.core.Constants;
import p1.edu.ufcg.worlddiscovery.core.Point;
import p1.edu.ufcg.worlddiscovery.service.HandleGeofenceService;

/**
 * Created by root on 20/09/16.
 */
public class GeofenceUtils {
    private static HashMap<String, Point> fences = new HashMap<>();

    private static GeofencingRequest.Builder builder;

    static {
        initBuider();
    }

    private static void initBuider() {
        builder = new GeofencingRequest.Builder();
        builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
    }

    public static Point getBindingPoint(String id) {
        return fences.get(id);
    }

    public static void removePointOfMap(String id) {
        fences.remove(id);
    }

    public static void createGeofece(String key, Point point, Context ctx, GoogleApiClient mGoogleApiClient, PendingIntent pedi, ResultCallback<? super Status> handleservice) {
        if (!isBidingPoint(key, ctx)) {
            Log.d("gg", "Biding");
            fences.put(key, point);
            updateSharedPref(key, ctx);
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(key)
                    .setCircularRegion(
                            point.lat,
                            point.lng,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build();

            builder.addGeofence(geofence);
            bindGeofences(mGoogleApiClient, pedi, handleservice);
        }else {
            Log.d("gg", "Nao Biding");
        }
    }

    private static void updateSharedPref(String key, Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.preference_geofece), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, true);
        editor.commit();
    }

    public static void bindGeofences(GoogleApiClient mGoogleApiClient, PendingIntent intent, ResultCallback<? super Status> callback) {
        try {
            if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        builder.build(),
                        intent
                ).setResultCallback(callback);
            }// Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            //logSecurityException(securityException);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isBidingPoint(String id, Context ctx) {

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.preference_geofece), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(id, false);
    }
}