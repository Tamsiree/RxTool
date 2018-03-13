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

import com.vondear.rxtools.view.progressing.style.Circle;
import com.vondear.rxtools.view.progressing.style.CubeGrid;
import com.vondear.rxtools.view.progressing.style.DoubleBounce;
import com.vondear.rxtools.view.progressing.style.Wave;
import com.vondear.tools.R;

/**
 * Created by Vondear.
 * @author vondear
 */
public class FragmentLoadingDemo extends Fragment{

    int[] colors = new int[]{
            android.graphics.Color.parseColor("#89CFF0"),
            android.graphics.Color.parseColor("#2B3E51"),
    };

    private Wave mWaveDrawable;
    private Circle mCircleDrawable;
    private CubeGrid mChasingDotsDrawable;

    public static FragmentLoadingDemo newInstance() {
        return new FragmentLoadingDemo();
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
        doubleBounce.setColor(colors[0]);
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
        textView.setBackgroundColor(colors[0]);

        //ImageView
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        mChasingDotsDrawable = new CubeGrid();
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
