package com.vondear.tools.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.vondear.tools.R;
import com.vondear.tools.adapter.AdapterRecyclerViewMain;
import com.vondear.tools.bean.MainItem;
import com.vondear.tools.scaner.ActivityScanerCode;
import com.vondear.vontools.VonImageUtils;
import com.vondear.vontools.VonRecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<MainItem> mData;

    private int mColumnCount = 3;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new MainItem("VonPhotoUtils操作UZrop裁剪图片", R.drawable.elves_ball, ActivityVonPhoto.class));
        mData.add(new MainItem("二维码与条形码的扫描与生成", R.drawable.scan_barcode, ActivityScanerCode.class));
        mData.add(new MainItem("WebView的封装可播放视频", R.drawable.webpage, com.vondear.vontools.activity.ActivityWebView.class));
        mData.add(new MainItem("常用的Dialog展示", R.drawable.dialog, ActivityDialog.class));
        mData.add(new MainItem("VonTextUtils操作Demo", R.drawable.text_editor, ActivityTextUtils.class));
        mData.add(new MainItem("进度条的艺术", R.drawable.signal_wifi, ActivityProgressBar.class));
        mData.add(new MainItem("横向滑动选择日期", R.drawable.bookshelf, ActivityWheelHorizontal.class));
        mData.add(new MainItem("横向左右自动滚动的ImageView", R.drawable.picture, ActivityAutoImageView.class));
        mData.add(new MainItem("SlidingDrawerSingle使用", R.drawable.sliding_drawer, ActivitySlidingDrawerSingle.class));
        mData.add(new MainItem("app检测更新与安装", R.mipmap.ic_launcher, ActivitySplash.class));
    }

    private void initView() {
        if (mColumnCount <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerview.addItemDecoration(new VonRecyclerViewUtils.SpaceItemDecoration(VonImageUtils.dp2px(context, 5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        recyclerview.setAdapter(recyclerViewMain);
    }


    //----------------------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
    //==============================================================================================
}
