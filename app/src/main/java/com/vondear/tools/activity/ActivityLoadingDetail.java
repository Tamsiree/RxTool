package com.vondear.tools.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vondear.rxtools.view.progressing.SpinKitView;
import com.vondear.rxtools.view.progressing.SpriteFactory;
import com.vondear.rxtools.view.progressing.Style;
import com.vondear.rxtools.view.progressing.sprite.Sprite;
import com.vondear.tools.R;
import com.vondear.tools.tools.EvaluatorARGB;

public class ActivityLoadingDetail extends AppCompatActivity {

    int[] colors = new int[]{
            android.graphics.Color.parseColor("#D55400"),
            android.graphics.Color.parseColor("#2B3E51"),
            android.graphics.Color.parseColor("#00BD9C"),
            android.graphics.Color.parseColor("#227FBB"),
            android.graphics.Color.parseColor("#7F8C8D"),
            android.graphics.Color.parseColor("#FFCC5C"),
            android.graphics.Color.parseColor("#D55400"),
            android.graphics.Color.parseColor("#1AAF5D"),
    };

    @SuppressWarnings("WeakerAccess")
    public static void start(Context context, int position) {
        Intent intent = new Intent(context, ActivityLoadingDetail.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Style.values().length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                @SuppressLint("InflateParams") View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager, null);

                SpinKitView spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
                TextView name = (TextView) view.findViewById(R.id.name);
                Style style = Style.values()[position];
                name.setText(style.name());
                Sprite drawable = SpriteFactory.create(style);
                spinKitView.setIndeterminateDrawable(drawable);
                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int color = (int) EvaluatorARGB.getInstance().evaluate(positionOffset,
                        colors[position % colors.length],
                        colors[(position + 1) % colors.length]);
                getWindow().getDecorView().setBackgroundColor(color);
            }

            @Override
            public void onPageSelected(int position) {
                getWindow().getDecorView().setBackgroundColor(colors[position % colors.length]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
    }
}
