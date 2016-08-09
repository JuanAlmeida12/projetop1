package br.edu.ufcg.projetop1.utils;

import android.app.PendingIntent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.edu.ufcg.projetop1.core.Constants;
import br.edu.ufcg.projetop1.core.Point;

/**
 * Created by root on 01/08/16.
 */
public class MapUtil {

    private static GeofencingRequest.Builder builder;

    static {
        builder = new GeofencingRequest.Builder();
        builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
    }

    public static Map<String,Point> geofencesIds = new HashMap<String,Point>();

    public static void createGeofece(Point point){
        String id = UUID.randomUUID().toString();
        geofencesIds.put(id,point);

        Geofence geofence = new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(
                        point.lat,
                        point.lng,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();

        builder.addGeofence(geofence);
    }

    public static void bindGeofences(GoogleApiClient mGoogleApiClient, PendingIntent intent, ResultCallback<? super Status> callback) {
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    builder.build(),
                    intent
            ).setResultCallback(callback); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            //logSecurityException(securityException);
        }
    }
}
