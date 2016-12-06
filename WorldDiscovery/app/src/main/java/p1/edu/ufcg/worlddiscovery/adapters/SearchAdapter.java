package p1.edu.ufcg.worlddiscovery.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.activities.UserDetailDetailActivity;
import p1.edu.ufcg.worlddiscovery.core.SearchHolder;
import p1.edu.ufcg.worlddiscovery.fragments.UserDetailDetailFragment;
import p1.edu.ufcg.worlddiscovery.utils.UserUtils;

/**
 * Created by root on 29/09/16.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    public static final String POINT_TYPE = "Point";
    public static final String USER_TYPE = "User";

    private List<ParseUser> data;
    private List<String> types;
    private Context ctx;

    public SearchAdapter(Context ctx) {
        this.data = new ArrayList<>();
        this.types = new ArrayList<>();
        this.ctx = ctx;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_adapter_layout, parent, false);
        SearchHolder vh = new SearchHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SearchHolder holder, int position) {
        final ParseUser data = this.data.get(position);
        if (types.get(position).equals(USER_TYPE)) {
            ParseFile image = data.getParseFile("image");
            getImage(image != null? image.getUrl():"http://s3.amazonaws.com/37assets/svn/765-default-avatar.png", holder.image);
            holder.name.setText(UserUtils.formattedName(data.getString("name")));
            holder.type.setText(R.string.user_type);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String transitionName = ctx.getString(R.string.user_image_search);
                    Intent i = new Intent(ctx, UserDetailDetailActivity.class);
                    i.putExtra(UserDetailDetailFragment.ARG_USER_ID, data.getUsername());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ctx.startActivity(i, ActivityOptions.makeSceneTransitionAnimation((Activity) ctx, holder.image , transitionName).toBundle());
                    }
                }
            });
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


    @Override
    public int getItemCount() {
        return data.size();
    }


    public void addUser(ParseUser user) {
        if (!isOnList(user)) {
            data.add(user);
            types.add(USER_TYPE);
            notifyDataSetChanged();
        }
    }

    public void addPoint(ParseUser point) {
        if (!isOnList(point)) {
            data.add(point);
            types.add(POINT_TYPE);
            notifyDataSetChanged();
        }
    }

    private boolean isOnList(ParseUser data) {
        for (ParseUser mData : this.data) {
            if (data.getUsername().equals(mData.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        data.clear();
        types.clear();
        notifyDataSetChanged();
    }
}
