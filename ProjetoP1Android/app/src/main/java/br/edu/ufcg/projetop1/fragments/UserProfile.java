package br.edu.ufcg.projetop1.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.adapters.UsersAdapter;
import br.edu.ufcg.projetop1.core.MyImageView;
import br.edu.ufcg.projetop1.core.PhotoUser;
import br.edu.ufcg.projetop1.core.UserInfo;
import br.edu.ufcg.projetop1.dialogs.PhotoDialog;
import br.edu.ufcg.projetop1.utils.FollowUtil;
import br.edu.ufcg.projetop1.views.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile extends Fragment implements ResultCallback<Status>, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseDatabase database;
    private DatabaseReference refInfo;
    private GoogleMap mMap;
    private FirebaseStorage storage;
    private String uuid;
    public static Map<String, PhotoUser> photoUserMap;

    public UserProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uuid
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance(String uuid) {
        UserProfile fragment = new UserProfile();
        Bundle args = new Bundle();
        args.putString(UsersAdapter.UUID_KEY, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.uuid = getArguments().getString(UsersAdapter.UUID_KEY);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        photoUserMap = new HashMap<String, PhotoUser>();
        refInfo = database.getReference("user-info/" + uuid);

    }

    private void userPicture(final UserInfo user, final ImageView view) {
        new AsyncTask<Object, Object, Object>() {
            Bitmap bitmap;

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    URL url = new URL(user.picture.toString());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                view.setImageBitmap(bitmap);
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.e("kaaaaaaaaaaaaaaaaaaa", dataSnapshot.toString());
                final UserInfo user = dataSnapshot.getValue(UserInfo.class);
                TextView name = (TextView) view.findViewById(R.id.user_profile_name);
                name.setText(user.name);
                TextView email = (TextView) view.findViewById(R.id.user_profile_email);
                email.setText(user.email);

                ImageView photo = (ImageView) view.findViewById(R.id.user_profile_photo);
                userPicture(user, photo);
                final Button follow = (Button) view.findViewById(R.id.bt_follow);
                if (FollowUtil.following(user.uuid)) {
                    follow.setText(R.string.unfollow);
                } else {
                    follow.setText(R.string.follow);
                }
                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (FollowUtil.following(user.uuid)) {
                            FollowUtil.unfollow(user.uuid);
                            follow.setText(R.string.follow);
                            ValueEventListener myValue = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.e("asdasd", "data: " + dataSnapshot.getValue(String.class));
                                    sendMessage(null, dataSnapshot.getValue(String.class));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            DatabaseReference token = database.getReference("user-token/" + uuid);
                            token.addValueEventListener(myValue);

                        } else {
                            FollowUtil.follow(user.uuid);
                            follow.setText(R.string.unfollow);
                        }
                    }
                });
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        refInfo.addValueEventListener(listener);

        // Inflate the layout for this fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_profile);
        mapFragment.getMapAsync(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void showDialog(String id) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = PhotoDialog.newInstance(9, id, true);
        newFragment.setCancelable(true);
        newFragment.show(ft, "dialog");
    }

    public void sendMessage(final Bundle data, final String to) {
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getContext());
        final String senderId = getString(R.string.sender_id);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... data) {
                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://gcm-http.googleapis.com/gcm/send");

                try {
                    //add data
                    JSONObject data2 = new JSONObject();
                    data2.put("to", to);
                    httppost.addHeader("Authorization", "key=AIzaSyAlibYmw1xGx_JjADozWZyb_-kAghd18CE");
                    httppost.addHeader("Content-Type", "application/json");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("data", data.toString()));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //execute http post
                    HttpResponse response = httpclient.execute(httppost);
                    Log.e("asdasd", response.getStatusLine().getStatusCode() + "");

                } catch (ClientProtocolException e) {
                    Log.e("asdasd", "Erro client");

                } catch (IOException e) {
                    Log.e("asdasd", "Ioexept");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "ok";
            }

            @Override
            protected void onPostExecute(String result) {
                //Update the UI
            }
        }.execute(null, null, null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        DatabaseReference refPhotos = database.getReference("user-photo/" + uuid);
        ChildEventListener photoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addPhoto(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refPhotos.addChildEventListener(photoListener);

    }

    private void addPhoto(String photorefString) {
        final StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.storageref));
        final StorageReference photoref = storageRef.child(photorefString);
        photoref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                final Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                photoref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(final StorageMetadata storageMetadata) {
                        new AsyncTask<Void, Void, Bitmap>() {
                            @Override
                            protected Bitmap doInBackground(Void... voids) {
                                return getMarkerBitmapFromView(image);
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                                double lat = Double.parseDouble(storageMetadata.getCustomMetadata("lat"));
                                double lng = Double.parseDouble(storageMetadata.getCustomMetadata("lng"));
                                String descript = storageMetadata.getCustomMetadata("descript");
                                LatLng pointLatLng = new LatLng(lat, lng);
                                MarkerOptions marker = new MarkerOptions();
                                marker.position(pointLatLng);
                                marker.icon(icon);

                                PhotoUser newPhoto = new PhotoUser(image, descript);
                                photoUserMap.put(mMap.addMarker(marker).getId(), newPhoto);
                            }
                        }.execute();

                    }
                });
            }
        });
    }

    public Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private Bitmap getMarkerBitmapFromView(Bitmap content_image) {

        MyImageView customMarkerView = (MyImageView) ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_custom, null);

        customMarkerView.setResourseBitmap(content_image);
        return createDrawableFromView(getActivity(), customMarkerView);
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        showDialog(marker.getId());
        return false;
    }

}
