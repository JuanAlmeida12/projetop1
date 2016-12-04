package p1.edu.ufcg.worlddiscovery.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.activities.UserDetailDetailActivity;
import p1.edu.ufcg.worlddiscovery.utils.FollowUtils;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

/**
 * A fragment representing a single UserDetail detail screen.
 * in two-pane mode (on tablets) or a {@link UserDetailDetailActivity}
 * on handsets.
 */
public class UserDetailDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_USER_ID = "user_id";
    public static final String JOB_KEY = "job_key";
    public static final String ADDRESS_KEY = "address_key";
    public static final String GENDER_KEY = "gender_key";
    public static final String MESSAGE_KEY = "message_key";
    public static final String IS_CURRENT_USER_KEY = "current_user";

    private String userId;

    private Bundle about;
    private FloatingActionButton editFb;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserDetailDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_USER_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            userId = getArguments().getString(ARG_USER_ID);
            about = new Bundle();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.userdetail_detail, container, false);

        Activity activity = UserDetailDetailFragment.this.getActivity();

        FloatingActionButton aboutFb = (FloatingActionButton) activity.findViewById(R.id.fb_about);
        aboutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(AboutFragment.newInstance(about));
            }
        });

        FloatingActionButton placesFb = (FloatingActionButton) activity.findViewById(R.id.fb_places);
        placesFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(UserPlaces.newInstance(userId));
            }
        });

        FloatingActionButton photosFb = (FloatingActionButton) activity.findViewById(R.id.fb_photos);
        photosFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(PhotosFragment.newInstance(userId));
            }
        });

        editFb = (FloatingActionButton) activity.findViewById(R.id.fb_edit);

        FloatingActionButton activitiesFb = (FloatingActionButton) activity.findViewById(R.id.fb_activity);
        activitiesFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(UserActivities.newInstance(userId));
            }
        });

        final FloatingActionButton follow
                = (FloatingActionButton) activity.findViewById(R.id.fb_follow);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FollowUtils.followAction(userId);
                stateFollowFb(follow);
            }
        });

        stateFollowFb(follow);
        if (isCurrentUser(userId)) {
            follow.setVisibility(View.GONE);
        } else {
            follow.setVisibility(View.VISIBLE);
        }

        UserUtils.getUser(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = UserDetailDetailFragment.this.getActivity();
                CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                ImageView image = (ImageView) appBarLayout.findViewById(R.id.user_image_detail);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(UserUtils.formattedName(dataSnapshot.child("name").getValue(String.class)));
                    getImage(dataSnapshot.child("photoURL").getValue(String.class), image);
                    about.putBoolean(IS_CURRENT_USER_KEY,isCurrentUser(userId));
                    about.putString(JOB_KEY, dataSnapshot.hasChild("job") ? dataSnapshot.child("job").getValue(String.class) : activity.getString(R.string.not_specified));
                    about.putString(ADDRESS_KEY, dataSnapshot.hasChild("city") ? dataSnapshot.child("city").getValue(String.class) : activity.getString(R.string.not_specified));
                    about.putString(GENDER_KEY, dataSnapshot.hasChild("gender") ? dataSnapshot.child("gender").getValue(String.class) : activity.getString(R.string.not_specified));
                    about.putString(MESSAGE_KEY, dataSnapshot.hasChild("message") ? dataSnapshot.child("message").getValue(String.class) : activity.getString(R.string.not_specified));
                    setFragment(AboutFragment.newInstance(about));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private void stateFollowFb(FloatingActionButton follow) {
        if(FollowUtils.following(userId)){
            follow.setLabelText(getString(R.string.unfollow));
        } else {
            follow.setLabelText(getString(R.string.follow));
        }
    }

    private void setFragment(Fragment fragment) {
        editFb.setVisibility(View.GONE);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.frame_detail_type, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

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
    private boolean isCurrentUser(String id) {
        return false;
    }
}
