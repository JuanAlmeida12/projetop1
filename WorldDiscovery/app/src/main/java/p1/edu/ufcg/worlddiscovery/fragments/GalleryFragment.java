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


public class GalleryFragment extends Fragment {

    private static final String ARG_USERID = "userid";

    private String userId;
    private ImageView selectedImage;
    private GalleryImageAdapter galleryImageAdapter;
    private TextView textTip, photoDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            userId = ParseUser.getCurrentUser().getString(ARG_USERID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_gallery, container, false);
        Gallery gallery = (Gallery) mView.findViewById(R.id.gallery);
        selectedImage = (ImageView) mView.findViewById(R.id.imageView);
        gallery.setSpacing(1);
        galleryImageAdapter = new GalleryImageAdapter(getContext());
        textTip = (TextView) mView.findViewById(R.id.text_tip);
        photoDescription = (TextView) mView.findViewById(R.id.photo_description);
        photoDescription.setVisibility(View.INVISIBLE);
        gallery.setAdapter(galleryImageAdapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                String desc = ((ParseObject) (ParseUser.getCurrentUser().getList("photos").get(position))).getString("description");
                Log.d("ff", desc != null ? desc : "null");
                selectedImage.setImageBitmap(galleryImageAdapter.getImage(position));
                photoDescription.setText(desc);
                photoDescription.setVisibility(View.VISIBLE);
                textTip.setVisibility(View.INVISIBLE);
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
