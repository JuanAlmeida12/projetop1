package br.edu.ufcg.projetop1.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import br.edu.ufcg.projetop1.R;
import br.edu.ufcg.projetop1.core.Badge;
import br.edu.ufcg.projetop1.core.BadgeHolder;
import br.edu.ufcg.projetop1.utils.BadgeUtil;

/**
 * Created by root on 16/08/16.
 */
public class BadgeAdapter extends RecyclerView.Adapter<BadgeHolder> {

    private List<Badge> badges;

    public BadgeAdapter(List<Badge> badges) {
        this.badges = badges;
        Collections.sort(this.badges);
    }

    @Override
    public BadgeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.badge_layout, parent, false);
        BadgeHolder vh = new BadgeHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BadgeHolder holder, int position) {
        Badge badge = badges.get(position);
        holder.title.setText(badge.getName());
        holder.body.setText(badge.getDescription());
        holder.progress.setMax(badge.getMeta());
        holder.progress.setProgress(badge.getAtual());
        holder.progressText.setText(String.format("%d/%d", badge.getAtual(), badge.getMeta()));

        if (badge.badgeCompleted()) {
            holder.completed.setVisibility(View.VISIBLE);
        } else {
            holder.completed.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }
}
