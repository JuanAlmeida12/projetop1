package p1.edu.ufcg.worlddiscovery.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import p1.edu.ufcg.worlddiscovery.R;

/**
 * Created by root on 04/10/16.
 */
public class GalleryImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> images;

    public GalleryImageAdapter(Context context)
    {
        images = new ArrayList<>();
        mContext = context;
    }

    public void addImage(Bitmap image) {
        images.add(image);
        notifyDataSetChanged();
    }

    public Bitmap getImage(int index) {
        return images.get(index);
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageBitmap(images.get(index));
        i.setLayoutParams(new Gallery.LayoutParams(400, 400));

        i.setScaleType(ImageView.ScaleType.FIT_XY);




        return i;
    }

}
