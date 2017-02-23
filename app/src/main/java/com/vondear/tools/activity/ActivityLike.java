package com.vondear.tools.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.vondear.rxtools.view.likeview.RxShineButton;
import com.vondear.tools.R;

public class ActivityLike extends AppCompatActivity {

    RxShineButton mRxShineButton;
    RxShineButton porterShapeImageView1;
    RxShineButton porterShapeImageView2;
    RxShineButton porterShapeImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        mRxShineButton = (RxShineButton) findViewById(R.id.po_image0);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wrapper);

        if (mRxShineButton != null)
            mRxShineButton.init(this);
        porterShapeImageView1 = (RxShineButton) findViewById(R.id.po_image1);
        if (porterShapeImageView1 != null)
            porterShapeImageView1.init(this);
        porterShapeImageView2 = (RxShineButton) findViewById(R.id.po_image2);
        if (porterShapeImageView2 != null)
            porterShapeImageView2.init(this);
        porterShapeImageView3 = (RxShineButton) findViewById(R.id.po_image3);
        if (porterShapeImageView3 != null)
            porterShapeImageView3.init(this);

        RxShineButton rxShineButtonJava = new RxShineButton(this);

        rxShineButtonJava.setBtnColor(Color.GRAY);
        rxShineButtonJava.setBtnFillColor(Color.RED);
        rxShineButtonJava.setShapeResource(R.raw.heart);
        rxShineButtonJava.setAllowRandomColor(true);
        rxShineButtonJava.setShineSize(100);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        rxShineButtonJava.setLayoutParams(layoutParams);
        if (linearLayout != null) {
            linearLayout.addView(rxShineButtonJava);
        }


        mRxShineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mRxShineButton.setOnCheckStateChangeListener(new RxShineButton.OnCheckedChangeListener() {
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
