package p1.edu.ufcg.worlddiscovery.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.core.ActivityHolder;

/**
 * Created by root on 04/10/16.
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {

    private List<DataSnapshot> data;
    private List<DataSnapshot> users;
    private Context ctx;

    public ActivityAdapter(Context ctx) {
        this.ctx = ctx;
        this.users = new ArrayList<>();
        data = new ArrayList<>();
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_adapter_layout, parent, false);
        ActivityHolder holder = new ActivityHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        holder.content.setText(data.get(position).child("content").getValue(String.class));
        getImage(users.get(position).child("photoURL").getValue(String.class),holder.image);
    }

    public void add(DataSnapshot activity, DataSnapshot user){
        data.add(activity);
        users.add(user);
        notifyDataSetChanged();
    }

    private void getImage(final String imageUrl, final ImageView image) {
        new AsyncTask<Object, Object, Object>() {
            Bitmap bitmap;

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    Log.d("image", imageUrl);
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


    @Override
    public int getItemCount() {
        return data.size();
    }
}
