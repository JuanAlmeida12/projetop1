package p1.edu.ufcg.worlddiscovery.dialogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.net.URL;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.activities.LoginActivity;
import p1.edu.ufcg.worlddiscovery.activities.MainActivity;
import p1.edu.ufcg.worlddiscovery.adapters.GalleryImageAdapter;
import p1.edu.ufcg.worlddiscovery.adapters.UserPlacesAdapter;
import p1.edu.ufcg.worlddiscovery.fragments.UserPlaces;

/**
 * Created by root on 04/10/16.
 */
public class DialogMap extends DialogFragment implements OnMapReadyCallback {


    private static final String PLACE_ID = "id";
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private String placeId;
    private SupportMapFragment mapFragment;
    private GalleryImageAdapter galleryImageAdapter;

    public static DialogMap onNewInstance(String id) {
        DialogMap dialog = new DialogMap();
        Bundle args = new Bundle();
        args.putString(PLACE_ID, id);
        dialog.setArguments(args);
        return dialog;
    }

    public DialogMap() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mNum = 1;

        Bundle args = getArguments();
        placeId = args.getString(PLACE_ID);

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum - 1) % 6) {
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 4:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 5:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 6:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 7:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 8:
                style = DialogFragment.STYLE_NORMAL;
                break;
        }
        switch ((mNum - 1) % 6) {
            case 4:
                theme = android.R.style.Theme_Holo;
                break;
            case 5:
                theme = android.R.style.Theme_Holo_Light_Dialog;
                break;
            case 6:
                theme = android.R.style.Theme_Holo_Light;
                break;
            case 7:
                theme = android.R.style.Theme_Holo_Light_Panel;
                break;
            case 8:
                theme = android.R.style.Theme_Holo_Light;
                break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottom_sheet_detail, container, false);

        mGoogleApiClient = ((MainActivity) getActivity()).getmGoogleApiClient();

        mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.user_places_m);
        mapFragment.getMapAsync(this);

        Button ok = (Button) v.findViewById(R.id.ok_map_dialog);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMap.this.dismiss();
            }
        });

        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                Log.e("fsdfsdf", "fined " + placeId);
                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                    Place place = places.get(0);
                    MarkerOptions options = new MarkerOptions();
                    options.position(place.getLatLng());
                    options.title(place.getName().toString());
                    TextView title = (TextView) v.findViewById(R.id.place_name_Title);
                    title.setText(place.getName().toString());
                    Gallery gallery = (Gallery) v.findViewById(R.id.galleryMapDialog);
                    gallery.setSpacing(1);
                    galleryImageAdapter = new GalleryImageAdapter(getContext());
                    gallery.setAdapter(galleryImageAdapter);
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Post");
                    query.whereEqualTo("place", place.getName().toString());
                    query.setLimit(10);
                    query.addDescendingOrder("createdAt");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            for (ParseObject data : list) {
                                getImage(data.getParseFile("photo").getUrl());
                            }
                        }
                    });
                    mMap.addMarker(options);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                } else {
                    Log.e("fsdfsdf", "Place not found " + places.getStatus());
                }
                places.release();
            }
        });


        return v;
    }

    private void getImage(final String p_url) {
        new AsyncTask<Void, Void, Void>() {
            private Bitmap image;

            @Override
            protected Void doInBackground(Void... voids) {
                URL url = null;
                try {
                    url = new URL(p_url);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (image != null) {
                    galleryImageAdapter.addImage(image);
                }
            }
        }.execute();
    }

    @Override
    public void onDetach() {
        getFragmentManager().beginTransaction().remove(mapFragment).commit();
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Add a marker in Sydney and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }
}
