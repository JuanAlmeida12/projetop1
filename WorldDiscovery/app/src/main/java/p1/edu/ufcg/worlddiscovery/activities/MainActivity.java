package p1.edu.ufcg.worlddiscovery.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.core.User;
import p1.edu.ufcg.worlddiscovery.dialogs.PhotoEditDialog;
import p1.edu.ufcg.worlddiscovery.fragments.MapFragment;
import p1.edu.ufcg.worlddiscovery.fragments.RecentActivities;
import p1.edu.ufcg.worlddiscovery.fragments.SearchFragment;
import p1.edu.ufcg.worlddiscovery.fragments.UserDetailDetailFragment;
import p1.edu.ufcg.worlddiscovery.interfaces.Searchable;
import p1.edu.ufcg.worlddiscovery.service.HandleGeofenceService;
import p1.edu.ufcg.worlddiscovery.utils.FollowUtils;
import p1.edu.ufcg.worlddiscovery.utils.PhotoUtil;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Searchable searchable;
    private FragmentManager mSuportFrag;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, HandleGeofenceService.class);
        startService(serviceIntent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        Snackbar
                .make(drawer, R.string.welcome, Snackbar.LENGTH_LONG).show();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mSuportFrag = getSupportFragmentManager();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FollowUtils.init();
            }
        }).start();


        final View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.user_nav_name);
        name.setText(UserUtils.formattedName(UserUtils.getCurrentUser().getDisplayName()));

        ImageView image = (ImageView) header.findViewById(R.id.image_user_nav);
        getImage(UserUtils.getCurrentUser().getPhotoUrl().toString(),image);

        View bottomSheetMain = findViewById(R.id.bs_main);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetMain);
        mBottomSheetBehavior.setPeekHeight(260);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        setUpUserInfo();
        setFragment(MapFragment.newInstance());
        toolbar.setTitle(getString(R.string.app_name));
    }

    private void getImage(final String imageUrl, final ImageView image) {
        new AsyncTask<Object, Object, Object>() {
            Bitmap bitmap;

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    URL url = new URL(imageUrl);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                image.setImageBitmap(bitmap);
            }
        }.execute();


    }


    private void setUpUserInfo() {
        UserUtils.getUser(UserUtils.getCurrentUser().getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView userScore = (TextView) findViewById(R.id.user_score);
                TextView userLL = (TextView) findViewById(R.id.user_last_login);
                TextView userSince = (TextView) findViewById(R.id.user_since);
                TextView userBadges = (TextView) findViewById(R.id.user_badges);
                TextView userPlaces = (TextView) findViewById(R.id.user_places);
                TextView userPosts = (TextView) findViewById(R.id.user_posts);

                userScore.setText(dataSnapshot.child("score").getValue().toString());
                userLL.setText(getDate(dataSnapshot.child("lastLogin").getValue(Long.class)));
                userPosts.setText(dataSnapshot.child("numPosts").getValue().toString());
                userBadges.setText(dataSnapshot.child("numBadges").getValue().toString());
                userPlaces.setText(dataSnapshot.child("numPlaces").getValue().toString());
                userSince.setText(getDate(dataSnapshot.child("createdAt").getValue(Long.class)));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_find_users);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment fragment = SearchFragment.newInstance();
                searchable = fragment;
                setFragment(fragment);
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));
        searchView.setIconifiedByDefault(true);

        return true;
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

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.7
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                dispatchTakePictureIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            Intent i = new Intent(this, UserDetailDetailActivity.class);
            i.putExtra(UserDetailDetailFragment.ARG_USER_ID,UserUtils.getCurrentUser().getUid());
            startActivity(i);
        } else if (id == R.id.nav_home) {
            setFragment(MapFragment.newInstance());
        } else if (id == R.id.nav_actions_user) {
            setFragment(RecentActivities.newInstance());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (null != searchable) {
            searchable.getQuery(query);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    PhotoEditDialog dialog = PhotoEditDialog.onNewInstance(imageBitmap);
                    dialog.setCancelable(false);
                    dialog.show(mSuportFrag, "someTag");
                }
            }).start();
        }
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

    @Override
    public boolean onQueryTextChange(String newText) {
        if (null != searchable) {
            searchable.getQuery(newText);
            return true;
        }
        return false;
    }

    public Location getmLastLocation(){
        return mLastLocation;
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
