package p1.edu.ufcg.worlddiscovery.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import p1.edu.ufcg.worlddiscovery.R;

/**
 * Created by root on 04/10/16.
 */
public class ActivityHolder extends RecyclerView.ViewHolder {
    public TextView content;
    public ImageView image;
    public TextView userName;
    public ImageView photo;
    public ActivityHolder(View itemView) {
        super(itemView);

        content = (TextView) itemView.findViewById(R.id.content_activity);
        userName = (TextView) itemView.findViewById(R.id.name_user_activity);
        image = (ImageView) itemView.findViewById(R.id.image_activity);
        photo = (ImageView) itemView.findViewById(R.id.photo_activity);
    }
}
