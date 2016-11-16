package br.edu.ufcg.projetop1.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.adapters.UsersAdapter;
import br.edu.ufcg.projetop1.core.UserInfo;
import br.edu.ufcg.projetop1.fragments.UserProfile;
import br.edu.ufcg.projetop1.views.MainActivity;

/**
 * Created by root on 23/08/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //String message = data.getString("data");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: Aleatoria");

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d(TAG, data.getString("sender"));
        DatabaseReference myRef = database.getReference("user-info/" + data.getString("sender"));
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo user = dataSnapshot.getValue(UserInfo.class);
                sendNotification(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(listener);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     */
    private void sendNotification(final UserInfo user) {
        new AsyncTask<Object, Object, Object>() {
            Bitmap bitmap;

            @Override
            protected Object doInBackground(Object... objects) {
                Log.d(TAG, "doInBackground");
                try {
                    URL url = new URL(user.picture.toString());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.d(TAG, "onPostExecute");
                super.onPostExecute(o);
                Intent intent = new Intent(MyGcmListenerService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(MyGcmListenerService.this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Intent profile = new Intent(getString(R.string.action_user_info));
                profile.putExtra(UsersAdapter.UUID_KEY, user.uuid);

                PendingIntent piProfile = PendingIntent.getActivity(MyGcmListenerService.this, 0 /* Request code */, profile,
                        PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyGcmListenerService.this)
                        .setSmallIcon(R.drawable.wdicon)
                        .setLargeIcon(bitmap)
                        .setContentTitle(getString(R.string.follow_notification_title))
                        .setContentText(getString(R.string.follow_notification_message))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .addAction(R.drawable.ic_search_white_24dp, "Check Profile", piProfile)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getString(R.string.follow_notification_body_1) + user.name + getString(R.string.follow_notification_body_2)))
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
        }.execute();
    }
}