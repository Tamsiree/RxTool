package com.vondear.tools.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vondear.rxtools.view.likeview.ShineButton;
import com.vondear.tools.R;

public class ActivityLike extends AppCompatActivity {

    ShineButton shineButton;
    ShineButton porterShapeImageView1;
    ShineButton porterShapeImageView2;
    ShineButton porterShapeImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        shineButton = (ShineButton) findViewById(R.id.po_image0);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wrapper);

        if (shineButton != null)
            shineButton.init(this);
        porterShapeImageView1 = (ShineButton) findViewById(R.id.po_image1);
        if (porterShapeImageView1 != null)
            porterShapeImageView1.init(this);
        porterShapeImageView2 = (ShineButton) findViewById(R.id.po_image2);
        if (porterShapeImageView2 != null)
            porterShapeImageView2.init(this);
        porterShapeImageView3 = (ShineButton) findViewById(R.id.po_image3);
        if (porterShapeImageView3 != null)
            porterShapeImageView3.init(this);

        ShineButton shineButtonJava = new ShineButton(this);

        shineButtonJava.setBtnColor(Color.GRAY);
        shineButtonJava.setBtnFillColor(Color.RED);
        shineButtonJava.setShapeResource(R.raw.heart);
        shineButtonJava.setAllowRandomColor(true);
        shineButtonJava.setShineSize(100);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        shineButtonJava.setLayoutParams(layoutParams);
        if (linearLayout != null) {
            linearLayout.addView(shineButtonJava);
        }


        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        shineButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
            }
        });

        porterShapeImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        porterShapeImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }
}
