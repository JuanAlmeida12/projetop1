package p1.edu.ufcg.worlddiscovery.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.core.UserPlacesHolder;
import p1.edu.ufcg.worlddiscovery.dialogs.DialogMap;

/**
 * Created by root on 04/10/16.
 */
public class UserPlacesAdapter extends RecyclerView.Adapter<UserPlacesHolder> {
    private List<ParseObject> points;
    private FragmentManager manager;

    public UserPlacesAdapter(FragmentManager manager) {
        points = new ArrayList<>();
        this.manager = manager;
    }


    @Override
    public UserPlacesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_place_item, parent, false);
        UserPlacesHolder holder = new UserPlacesHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserPlacesHolder holder, int position) {
        final ParseObject data = points.get(position);
        holder.placeName.setText(data.getString("place"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMap dialog = DialogMap.onNewInstance(data.getString("placeid"));
                dialog.setCancelable(false);
                dialog.show(manager, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    public void addPoint(ParseObject dataSnapshot) {
        points.add(dataSnapshot);
        notifyDataSetChanged();
    }
}
