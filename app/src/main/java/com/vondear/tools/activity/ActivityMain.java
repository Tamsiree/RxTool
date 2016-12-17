package com.vondear.tools.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vondear.tools.R;
import com.vondear.tools.activity.ActivitySplash;
import com.vondear.tools.activity.ActivityVonPhoto;
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
        mData.add(new MainItem("VonPhotoUtils操作UZrop裁剪图片",R.drawable.elves_ball,ActivityVonPhoto.class));
        mData.add(new MainItem("二维码与条形码的扫描与生成",R.drawable.scan_barcode,ActivityScanerCode.class));
        mData.add(new MainItem("app的检测更新与安装",R.mipmap.ic_launcher,ActivitySplash.class));
        mData.add(new MainItem("常用的Dialog展示",R.drawable.dialog,ActivityDialog.class));
    }

    private void initView() {
        if (mColumnCount <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerview.addItemDecoration(new VonRecyclerViewUtils.SpaceItemDecoration(VonImageUtils.dp2px(context,5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        recyclerview.setAdapter(recyclerViewMain);
    }


}
