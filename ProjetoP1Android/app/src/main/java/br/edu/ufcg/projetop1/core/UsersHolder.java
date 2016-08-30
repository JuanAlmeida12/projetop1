package br.edu.ufcg.projetop1.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.ufcg.projetop1.R;

/**
 * Created by root on 21/08/16.
 */
public class UsersHolder extends RecyclerView.ViewHolder {

    public ImageView userImage;
    public TextView userName;
    public TextView userEmail;
    public View itemView;

    public UsersHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        userImage = (ImageView) itemView.findViewById(R.id.user_adapter_image);
        userEmail = (TextView) itemView.findViewById(R.id.user_adapter_email);
        userName = (TextView) itemView.findViewById(R.id.user_adapter_name);
    }
}
