package p1.edu.ufcg.worlddiscovery.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.adapters.GalleryImageAdapter;
import p1.edu.ufcg.worlddiscovery.utils.PhotoUtil;
import p1.edu.ufcg.worlddiscovery.utils.PointUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    private static final String ARG_USERID = "userid";

    private String userId;
    private ImageView selectedImage;
    private List<DataSnapshot> photos;
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
        photos = new ArrayList<>();
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
                String desc = photos.get(position).child("description").getValue(String.class);
                Log.d("ff", desc != null ? desc : "null");
                selectedImage.setImageBitmap(galleryImageAdapter.getImage(position));
                photodescription.setText(desc);
            }
        });
        PhotoUtil.getAllUserPhotos(userId, new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                    while (i.hasNext()) {
                        final DataSnapshot data = i.next();
                        PhotoUtil.getPhotoLink(data.child("content").getValue(String.class), new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull final Task<Uri> task) {
                                new AsyncTask<Void, Void, Void>() {
                                    private Bitmap image;

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        URL url = null;
                                        try {
                                            url = new URL(task.getResult().toString());
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
                                            photos.add(data);
                                        }
                                    }
                                }.execute();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mView;
    }

}
