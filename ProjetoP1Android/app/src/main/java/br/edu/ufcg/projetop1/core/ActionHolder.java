package br.edu.ufcg.projetop1.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.ufcg.projetop1.R;

/**
 * Created by root on 30/08/16.
 */
public class ActionHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView action;
    public ImageView imageAction;
    public ImageView userImage;
    public ImageView userImage2;
    public TextView userName2;

    public ActionHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.action_adapter_name1);
        action = (TextView) itemView.findViewById(R.id.action_text);
        imageAction = (ImageView) itemView.findViewById(R.id.action_image);
        userImage = (ImageView) itemView.findViewById(R.id.action_adapter_image1);
        userImage2 = (ImageView) itemView.findViewById(R.id.action_adapter_image2);
        userName2 = (TextView) itemView.findViewById(R.id.action_adapter_name2);
    }
}
