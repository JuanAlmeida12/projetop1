package br.edu.ufcg.projetop1.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.edu.ufcg.projetop1.R;

/**
 * Created by root on 16/08/16.
 */
public class BadgeHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView body;
    public TextView progressText;
    public ImageView completed;
    public ProgressBar progress;

    public BadgeHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title_badge);
        body = (TextView) itemView.findViewById(R.id.body_badge);
        progress = (ProgressBar) itemView.findViewById(R.id.progressBar);
        progressText = (TextView) itemView.findViewById(R.id.progress_text);
        completed = (ImageView) itemView.findViewById(R.id.completed_image);
    }
}
