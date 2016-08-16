package br.edu.ufcg.projetop1.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.fragments.BadgesFragment;
import br.edu.ufcg.projetop1.fragments.MapFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    public static FragmentManager fragmentManager;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseStorage storage;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FirebaseDatabase database;
    public static List<String> userPhotos = new ArrayList<String>();
    private boolean isHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        setFragment(MapFragment.getNewInstance(false));

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.user_name);
        name.setText(user.getDisplayName());

        TextView email = (TextView) header.findViewById(R.id.user_email);
        email.setText(user.getEmail());

        userPictureNav(user, header);

        DatabaseReference userPhotosRef = database.getReference("user-photo");
        userPhotosRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userPhotos.add(dataSnapshot.getValue(String.class));
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
        });

    }

    private void userPictureNav(final FirebaseUser user, final View header) {
        new AsyncTask<Object, Object, Object>() {
            Bitmap bitmap;

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    URL url = new URL(user.getPhotoUrl().toString());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Log.i(TAG, user.getPhotoUrl().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ImageView image = (ImageView) header.findViewById(R.id.user_image);
                image.setImageBitmap(bitmap);
            }
        }.execute();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isHome) {
                finish();
            } else {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.frame_main, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            dispatchTakePictureIntent();
        } else if (id == R.id.nav_gallery) {
            setFragment(MapFragment.getNewInstance(true));
        } else if (id == R.id.nav_badges) {
            setFragment(BadgesFragment.newInstance());

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        if (id == R.id.nav_home) {
            setFragment(MapFragment.getNewInstance(false));
            isHome = true;
        } else {
            isHome = false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            sendPhoto(data);
        }
    }


    private void sendPhoto(Intent data) {
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String photoId = UUID.randomUUID().toString();
        StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.storageref));
        final String ref = userid + "/" + photoId + ".jpg";
        StorageReference userPhotoRef = storageRef.child(ref);
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datafirebase = baos.toByteArray();
        UploadTask uploadTask = userPhotoRef.putBytes(datafirebase);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (mLastLocation != null) {
                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setCustomMetadata("lat", mLastLocation.getLatitude() + "")
                            .setCustomMetadata("lng", mLastLocation.getLongitude() + "")
                            .build();
                    taskSnapshot.getStorage().updateMetadata(metadata);
                }
                DatabaseReference myRef = database.getReference("user-photo");
                myRef.child(userid).child(photoId).setValue(ref);
                Log.e(TAG, downloadUrl.toString());
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void downloadImages() {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.storageref));
        StorageReference photosRef = storageRef.child(userid);
        photosRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

            }
        });
    }
}
