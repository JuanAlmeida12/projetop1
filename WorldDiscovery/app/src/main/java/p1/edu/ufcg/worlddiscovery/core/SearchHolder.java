package p1.edu.ufcg.worlddiscovery.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import p1.edu.ufcg.worlddiscovery.R;

/**
 * Created by root on 30/09/16.
 */
public class SearchHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView name;
    public TextView type;
    public View itemView;

    public SearchHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        image = (ImageView) itemView.findViewById(R.id.image_search);
        name = (TextView) itemView.findViewById(R.id.name_search);
        type = (TextView) itemView.findViewById(R.id.type_search);
    }
}
