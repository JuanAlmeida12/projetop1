package p1.edu.ufcg.worlddiscovery.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

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
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.activities.MainActivity;

import static android.R.attr.bitmap;
import static android.R.attr.id;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by root on 04/10/16.
 */
public class PhotoUtil {


    public static void getAllUserPhotos(String id, ValueEventListener listener) {
    }

    public static void getPhotoLink(String path, OnCompleteListener<Uri> complete) {
    }

    public static void sendPhoto(Bitmap imageBitmap, final Location mLastLocation, final String description, Context ctx) {
        File file = new File("path");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] data = os.toByteArray();
        ParseFile parseFile = new ParseFile(data);
        final ProgressNotification progess = new ProgressNotification(ctx);
        progess.start();
        parseFile.saveInBackground(new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                progess.setProgress(integer);
            }
        });

        ParseObject userPhoto = new ParseObject("UserPhoto");
        userPhoto.put("lat", mLastLocation.getLatitude());
        userPhoto.put("lng", mLastLocation.getLongitude());
        userPhoto.put("photo", parseFile);
        userPhoto.put("description", description);
        userPhoto.saveInBackground();

        ActivitiesUtils.newPhoto(parseFile, description, MainActivity.currentPlace != null ? MainActivity.currentPlace.getPlace().toString() : "");

        ParseUser user = ParseUser.getCurrentUser();
        user.add("photos", userPhoto);
        user.saveInBackground();
    }

    private static class ProgressNotification extends Thread {

        private final NotificationManager mNotifyManager;
        private final NotificationCompat.Builder mBuilder;

        private int progress;

        public ProgressNotification(Context ctx) {
            this.progress = 0;

            mNotifyManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(ctx);
            mBuilder.setContentTitle("Salvando Foto")
                    .setColor(0x007fff)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.upload))
                    .setContentText("Progresso do Upload")
                    .setSmallIcon(R.mipmap.ic_launcher);
        }

        public void setProgress(int newProgress) {
            this.progress = newProgress;
        }

        @Override
        public void run() {
            while (progress < 100) {
                mBuilder.setProgress(100, progress, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(1, mBuilder.build());
                // Sleeps the thread, simulating an operation
                // that takes time
            }
            // When the loop is finished, updates the notification
            mBuilder.setContentText("Upload completo")
                    // Removes the p1rogress bar
                    .setProgress(0, 0, false);
            mNotifyManager.notify(1, mBuilder.build());
        }
    }
}

