package p1.edu.ufcg.worlddiscovery.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import p1.edu.ufcg.worlddiscovery.R;

/**
 * Created by root on 29/09/16.
 */
public class WellcomeAdapter extends PagerAdapter {
    public static final int NUM_PAGES = 3;
    private Context mContext;

    public WellcomeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.wellcome_page_item, container, false);

        RelativeLayout layout = (RelativeLayout) itemView.findViewById(R.id.layout_wellcome);
        layout.setBackgroundResource(getBackground(position));

        ImageView texto = (ImageView) itemView.findViewById(R.id.text_wellcome);
        texto.setImageResource(getTextImage(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public int getBackground(int page) {
        switch (page) {
            case 0:
                return R.drawable.inicial1bg;
            case 1:
                return R.drawable.inicial2bg;
            case 2:
                return R.drawable.inicial3bg;
        }
        return 0;
    }

    public int getTextImage(int page) {
        switch (page) {
            case 0:
                return R.drawable.inicial1_texto;
            case 1:
                return R.drawable.inicial2_texto;
            case 2:
                return R.drawable.inicial3_texto;
        }
        return 0;
    }
}
