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

    private static StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://worlddiscovery-f60f3.appspot.com");
    private static DatabaseReference userPhotoRef = FirebaseDatabase.getInstance().getReference("photo");

    public static void getAllUserPhotos(String id, ValueEventListener listener) {
        userPhotoRef.orderByChild("owner").equalTo(id).addListenerForSingleValueEvent(listener);
    }

    public static void getPhotoLink(String path, OnCompleteListener<Uri> complete) {
        StorageReference photoref = storageRef.child(path);
        photoref.getDownloadUrl().addOnCompleteListener(complete);
    }

    public static void sendPhoto(Bitmap imageBitmap, final Location mLastLocation, final Object description) {
        final String photoId = UUID.randomUUID().toString();
        final String ref = UserUtils.getCurrentUser().getUid() + "/" + photoId + ".jpg";
        final StorageReference photoRef = storageRef.child(ref);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datafirebase = baos.toByteArray();
        UploadTask uploadTask = photoRef.putBytes(datafirebase);
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
                DatabaseReference newPhoto = userPhotoRef.push();
                HashMap<String, Object> update = new HashMap<String, Object>();
                update.put("owner", UserUtils.getCurrentUser().getUid());
                update.put("content", ref);
                update.put("description", description);
                if (mLastLocation != null) {
                    update.put("lat", mLastLocation.getLatitude() + "");
                    update.put("lng", mLastLocation.getLongitude() + "");
                }
                newPhoto.updateChildren(update);
                ActivitiesUtils.newActivity(ActivitiesUtils.ACTIVITY_NEW_PHOTO);
            }
        });
    }


}
