package com.vondear.tools.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vondear.rxtools.view.progress.style.ChasingDots;
import com.vondear.rxtools.view.progress.style.Circle;
import com.vondear.rxtools.view.progress.style.DoubleBounce;
import com.vondear.rxtools.view.progress.style.Wave;
import com.vondear.tools.R;

/**
 * Created by ybq.
 */
public class Page2Fragment extends Fragment{

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

    private Wave mWaveDrawable;
    private Circle mCircleDrawable;
    private ChasingDots mChasingDotsDrawable;

    public static Page2Fragment newInstance() {
        return new Page2Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page2, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ProgressBar
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        doubleBounce.setBounds(0, 0, 100, 100);
        doubleBounce.setColor(colors[7]);
        progressBar.setIndeterminateDrawable(doubleBounce);

        //Button
        Button button = (Button) view.findViewById(R.id.button);
        mWaveDrawable = new Wave();
        mWaveDrawable.setBounds(0, 0, 100, 100);
        //noinspection deprecation
        mWaveDrawable.setColor(getResources().getColor(R.color.colorAccent));
        button.setCompoundDrawables(mWaveDrawable, null, null, null);

        //TextView
        TextView textView = (TextView) view.findViewById(R.id.text);
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        textView.setCompoundDrawables(null, null, mCircleDrawable, null);
        textView.setBackgroundColor(colors[2]);

        //ImageView
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(Color.WHITE);
        imageView.setImageDrawable(mChasingDotsDrawable);
        imageView.setBackgroundColor(colors[0]);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveDrawable.start();
        mCircleDrawable.start();
        mChasingDotsDrawable.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWaveDrawable.stop();
        mCircleDrawable.stop();
        mChasingDotsDrawable.stop();
    }
}
