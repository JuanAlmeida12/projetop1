package p1.edu.ufcg.worlddiscovery.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import p1.edu.ufcg.worlddiscovery.R;

/**
 * Created by root on 04/10/16.
 */
public class UserPlacesHolder extends RecyclerView.ViewHolder {
    public TextView placeName;
    public View itemView;

    public UserPlacesHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        placeName = (TextView) itemView.findViewById(R.id.place_name);
    }
}
