package br.edu.ufcg.projetop1.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;

import java.net.URL;
import java.util.List;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.core.BadgeHolder;
import br.edu.ufcg.projetop1.core.UserInfo;
import br.edu.ufcg.projetop1.core.UsersHolder;

/**
 * Created by root on 21/08/16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersHolder> {

    public static final String UUID_KEY = "uuid";
    private static final String TAG = "UserInfo";
    private List<UserInfo> users;
    private Context ctx;

    public UsersAdapter(List<UserInfo> users, Context ctx) {
        this.users = users;
        this.ctx = ctx;
    }

    @Override
    public UsersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_adapter_layout, parent, false);
        UsersHolder vh = new UsersHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UsersHolder holder, int position) {
        final UserInfo user = users.get(position);
        holder.userName.setText(user.name);
        holder.userEmail.setText(user.email);
        userPicture(user, holder.userImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(ctx.getString(R.string.action_user_info));
                i.putExtra(UUID_KEY, user.uuid);
                ctx.startActivity(i);
            }
        });
    }


    private void userPicture(final UserInfo user, final ImageView view) {
        new AsyncTask<Object, Object, Object>() {
            Bitmap bitmap;

            @Override
            protected Object doInBackground(Object... objects) {
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
                super.onPostExecute(o);
                view.setImageBitmap(bitmap);
            }
        }.execute();
    }


    public void add(UserInfo user) {
        users.add(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
