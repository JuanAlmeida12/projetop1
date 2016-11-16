package br.edu.ufcg.projetop1.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.List;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.core.ActionHolder;
import br.edu.ufcg.projetop1.core.UserAction;
import br.edu.ufcg.projetop1.core.UserInfo;
import br.edu.ufcg.projetop1.utils.ActionUtils;

/**
 * Created by root on 30/08/16.
 */
public class ActionAdapter extends RecyclerView.Adapter<ActionHolder> {
    private final FirebaseStorage storage;
    private final FirebaseDatabase database;
    private List<UserAction> actions;
    private Context ctx;

    public ActionAdapter(List<UserAction> actions, Context ctx) {
        this.actions = actions;
        this.ctx = ctx;
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public ActionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_item_adapter, parent, false);
        ActionHolder vh = new ActionHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ActionHolder holder, int position) {
        final UserAction action = actions.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseReference refInfo = database.getReference("user-info/" + action.user);
                refInfo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final UserInfo user = dataSnapshot.getValue(UserInfo.class);
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userPicture(user, holder.userImage);
                                holder.userName.setText(user.name.split(" ")[0]);
                                if (action.type.equals(ActionUtils.ACTION_NEW_PLACE)) {
                                    holder.userImage2.setVisibility(View.GONE);
                                    holder.userName2.setText(action.content);
                                    holder.action.setText(R.string.arrived_at);
                                    holder.imageAction.setImageResource(R.drawable.place_board);
                                } else if (action.type.equals(ActionUtils.ACTION_NEW_PHOTO)) {
                                    addPhoto(action.content, holder.userImage2);
                                    holder.userName2.setVisibility(View.GONE);
                                    holder.action.setText(R.string.new_photo);
                                    holder.imageAction.setImageResource(R.drawable.ic_menu_camera);
                                }
                            }
                        });
                        if (action.type.equals(ActionUtils.ACTION_NEW_FOLLOW)) {
                            holder.action.setText(R.string.started_following);
                            holder.imageAction.setImageResource(R.drawable.follow);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DatabaseReference refInfo = database.getReference("user-info/" + action.content);
                                    refInfo.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final UserInfo user2 = dataSnapshot.getValue(UserInfo.class);
                                            ((Activity) ctx).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    holder.userName2.setText(user2.name.split(" ")[0]);
                                                    userPicture(user2, holder.userImage2);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).start();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }).start();
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

    private void addPhoto(final String photorefString, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StorageReference storageRef = storage.getReferenceFromUrl(ctx.getString(R.string.storageref));
                final StorageReference photoref = storageRef.child(photorefString);
                photoref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(image);
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public void addAction(UserAction action) {
        actions.add(action);
        notifyDataSetChanged();
    }
}
