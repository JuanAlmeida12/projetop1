package p1.edu.ufcg.worlddiscovery.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.net.URL;
import java.util.Iterator;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.adapters.GalleryImageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    private static final String ARG_USERID = "userid";

    private String userId;
    private ImageView selectedImage;
    private GalleryImageAdapter galleryImageAdapter;
    private TextView photodescription;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String userId) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_photos, container, false);
        Gallery gallery = (Gallery) mView.findViewById(R.id.gallery);
        selectedImage = (ImageView) mView.findViewById(R.id.imageView);
        gallery.setSpacing(1);
        galleryImageAdapter = new GalleryImageAdapter(getContext());
        photodescription = (TextView) mView.findViewById(R.id.photo_description);
        gallery.setAdapter(galleryImageAdapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                String desc = ((ParseObject) (ParseUser.getCurrentUser().getList("photos").get(position))).getString("description");
                Log.d("ff", desc != null ? desc : "null");
                selectedImage.setImageBitmap(galleryImageAdapter.getImage(position));
                photodescription.setText(desc);
            }
        });

        try {

            ParseUser user = ParseUser.getCurrentUser().fetchIfNeeded();
            if (user.has("photos")) {
                Iterator<Object> i = user.getList("photos").iterator();
                while (i.hasNext()) {
                    ParseObject photo = ((ParseObject) i.next()).fetchIfNeeded();
                    getImage(photo.getParseFile("photo").getUrl());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mView;
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

}
