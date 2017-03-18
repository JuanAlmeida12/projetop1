package p1.edu.ufcg.worlddiscovery.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.maps.android.SphericalUtil;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.dialogs.PhotoEditDialog;
import p1.edu.ufcg.worlddiscovery.fragments.BadgesFragment;
import p1.edu.ufcg.worlddiscovery.fragments.FeedFragment;
import p1.edu.ufcg.worlddiscovery.fragments.GalleryFragment;
import p1.edu.ufcg.worlddiscovery.fragments.MapFragment;
import p1.edu.ufcg.worlddiscovery.fragments.PlacesFragment;
import p1.edu.ufcg.worlddiscovery.fragments.RecentActivities;
import p1.edu.ufcg.worlddiscovery.fragments.SearchFragment;
import p1.edu.ufcg.worlddiscovery.fragments.SobreFragment;
import p1.edu.ufcg.worlddiscovery.interfaces.Searchable;
import p1.edu.ufcg.worlddiscovery.service.HandleGeofenceService;
import p1.edu.ufcg.worlddiscovery.utils.FollowUtils;
import p1.edu.ufcg.worlddiscovery.utils.PointUtils;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PREMISSION_LOCATION = 2121;

    private Searchable searchable;
    private FragmentManager mSuportFrag;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Bundle about;
    public static HashMap<String, String> currentPlace;
    ParseUser user;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    String[] mPermissionArray = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private LocationManager manager;
    private FragmentManager fragmentManager;

    private Fragment currentFragment;
    private MapFragment mapFragment;
    private FeedFragment feedFragment;
    private BadgesFragment badgesFragment;
    private PlacesFragment placesFragment;
    private GalleryFragment galleryFragment;
    private RecentActivities recentActivitiesFragment;
    private SobreFragment sobreFragment;
    Toolbar toolbar;

    private void testeMarshmallow() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }

                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();

        } else {
            proceedAfterPermission();
        }

    }

    private void proceedAfterPermission() {
        Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }

    private void displayPromptForEnablingGPS() {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Please enable location services by clicking ok";

        builder.setMessage(message)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int idButton) {
                                startActivity(new Intent(action));
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int idButton) {
                                dialog.cancel();
                            }
                        });
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        testeMarshmallow();

        Intent serviceIntent = new Intent(this, HandleGeofenceService.class);
        startService(serviceIntent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.whitebg));
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_dark_background));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("userid",ParseUser.getCurrentUser().getObjectId());
//        ParseCloud.callFunctionInBackground("getBadgeProgess", params, new FunctionCallback<ParseObject[]>() {
//            public void done(ParseObject[] ratings, ParseException e) {
//                if (e == null) {
//                    Log.d("asdasda",ratings.length+"");
//                    // ratings is 4.5
//                }
//                else
//                    Log.d("NO RESPONSE",e + "");
//            }
//        });



        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {


                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PREMISSION_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        Snackbar
                .make(drawer, R.string.welcome, Snackbar.LENGTH_LONG).show();

        setUpFragments();

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
        name.setText(UserUtils.formattedName(ParseUser.getCurrentUser().getString("name")));

        ParseFile imagefile = ParseUser.getCurrentUser().getParseFile("image");
        ImageView image = (ImageView) header.findViewById(R.id.image_user_nav);
        getImage(imagefile != null ?
                        imagefile.getUrl() : "http://s3.amazonaws.com/37assets/svn/765-default-avatar.png"
                , image);

        setUpUserInfo();
        toolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PREMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setFragment(MapFragment.newInstance(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("owner", ParseUser.getCurrentUser().getObjectId());
//        ParseCloud.callFunctionInBackground("getWeekResume", params, new FunctionCallback<Float>() {
//            public void done(Float ratings, ParseException e) {
//                if (e == null) {
//                    // ratings is 4.5
//                }
//            }
//        });

        user = ParseUser.getCurrentUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final View header = navigationView.getHeaderView(0);

        String score = user.getInt("score") != Integer.MIN_VALUE ? user.getInt("score") + "" : "0";
        String since = DateFormat.getDateFormat(this).format(user.getCreatedAt());

        Log.e("dasd", user.getInt("score") + "");

        TextView userScore = (TextView) header.findViewById(R.id.user_nav_score);
        TextView userSince = (TextView) header.findViewById(R.id.user_nav_since);
        userScore.setText(score);
        userSince.setText(since);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            finish();
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
            case R.id.action_checkin:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (mLastLocation != null) {
                    Log.e("ddasdas", "location n é null");
                    currentPlace = PointUtils.getCurrentLocal(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                } else {
                    currentPlace = null;
                }
                if (currentPlace != null) {
                    Log.e("ddasdas", "atualn é null");
                    builder.setMessage("Fazer Checkin em " + currentPlace.get("place_name"))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ArrayList<String> tags = new ArrayList<String>();
                                    int i = 0;
                                    while (currentPlace.containsKey("tags" + i)) {
                                        tags.add(currentPlace.get("tags" + i));
                                        i++;
                                    }
                                    PointUtils.visitedPoint(currentPlace.get("id"), currentPlace.get("place_name"), tags.toArray(new String[tags.size()]));
                                }
                            });
                } else {
                    builder.setMessage("Nenhum lugar proximo para fazer checkin! :(")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                }
                builder.create().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void setUpFragments() {
        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //if gps is disabled
            displayPromptForEnablingGPS();
        }
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }

        mapFragment = null;
        feedFragment = new FeedFragment();
        badgesFragment = new BadgesFragment();
        placesFragment = new PlacesFragment();
        galleryFragment = new GalleryFragment();
        recentActivitiesFragment = RecentActivities.newInstance();
        sobreFragment = new SobreFragment();

//        currentFragment = mapFragment;
//        fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, mapFragment, MAP_TAG);
//        fragmentTransaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            toolbar.setTitle(getString(R.string.app_name));
            setFragment(MapFragment.newInstance(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else if (id == R.id.nav_feed) {
            toolbar.setTitle("Feed");
            setFragment(feedFragment);
        } else if (id == R.id.nav_badges) {
            toolbar.setTitle("Conquistas");
            setFragment(badgesFragment);
        } else if (id == R.id.nav_camera) {
            dispatchTakePictureIntent();
        } else if (id == R.id.nav_places) {
            toolbar.setTitle("Lugares");
            setFragment(placesFragment);
        } else if (id == R.id.nav_gallery) {
            toolbar.setTitle("Galeria");
            setFragment(galleryFragment);
        } else if (id == R.id.nav_actions_user) {
            toolbar.setTitle("Atividade Recente");
            setFragment(recentActivitiesFragment);
        } else if (id == R.id.nav_share) {
            toolbar.setTitle("Compartilhar");
            //dispatchTakePictureIntent();
        } else if (id == R.id.nav_settings) {
            toolbar.setTitle("Configurações");
        } else if (id == R.id.nav_about) {
            toolbar.setTitle("Sobre");
            setFragment(sobreFragment);
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
        if (mLastLocation != null) {
            setFragment(MapFragment.newInstance(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }

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

    public Location getmLastLocation() {
        return mLastLocation;
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
