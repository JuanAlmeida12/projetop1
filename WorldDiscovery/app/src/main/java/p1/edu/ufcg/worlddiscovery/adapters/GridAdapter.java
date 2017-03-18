package p1.edu.ufcg.worlddiscovery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;

/**
 * Created by juanalmeida on 20/12/16.
 */

public class GridAdapter extends BaseAdapter {

    private Context ctx;
    private List<ParseObject> badges;

    public GridAdapter(Context ctx) {
        badges = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return badges.size();
    }

    @Override
    public Object getItem(int i) {
        return badges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        LayoutInflater layoutinflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.badge, parent, false);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }

        return null;
    }

    static class ViewHolder {
        ImageView imageInListView;
    }
}
