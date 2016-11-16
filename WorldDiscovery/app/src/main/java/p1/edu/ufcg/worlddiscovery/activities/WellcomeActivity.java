package p1.edu.ufcg.worlddiscovery.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import p1.edu.ufcg.worlddiscovery.R;
import p1.edu.ufcg.worlddiscovery.adapters.WellcomeAdapter;

public class WellcomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private WellcomeAdapter mAdapter;
    private ImageView[] indicators;
    private Button btnNext, btnFinish, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        mAdapter = new WellcomeAdapter(this);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);

        btnNext = (Button) findViewById(R.id.btn_next);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnBack = (Button) findViewById(R.id.btn_back);

        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        setupIndicators();
    }

    private void setupIndicators() {
        indicators = new ImageView[WellcomeAdapter.NUM_PAGES];
        for (int i = 0; i < WellcomeAdapter.NUM_PAGES; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(indicators[i], params);
        }
        indicators[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < WellcomeAdapter.NUM_PAGES; i++) {
            indicators[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        indicators[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        if (position + 1 == WellcomeAdapter.NUM_PAGES) {
            btnNext.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        }
        if(position == 0) {
            btnBack.setVisibility(View.GONE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                intro_images.setCurrentItem((intro_images.getCurrentItem() < WellcomeAdapter.NUM_PAGES)
                        ? intro_images.getCurrentItem() + 1 : 0);
                break;
            case R.id.btn_back:
                intro_images.setCurrentItem((intro_images.getCurrentItem() > 0)
                        ? intro_images.getCurrentItem() - 1 : 0);
                break;
            case R.id.btn_finish:
                Intent i = new Intent(WellcomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
