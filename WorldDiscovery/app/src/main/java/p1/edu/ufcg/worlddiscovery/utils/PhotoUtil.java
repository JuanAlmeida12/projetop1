package p1.edu.ufcg.worlddiscovery.utils;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by root on 04/10/16.
 */
public class PhotoUtil {


    public static void getAllUserPhotos(String id, ValueEventListener listener) {
    }

    public static void getPhotoLink(String path, OnCompleteListener<Uri> complete) {
    }

    public static void sendPhoto(Bitmap imageBitmap, final Location mLastLocation, final Object description) {
    }


}
