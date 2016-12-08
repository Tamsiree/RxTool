package com.vondear.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vondear.vontools.VonRecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<MainItem> mData;

    private int mColumnCount;

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
        mColumnCount = 3;
        mData = new ArrayList<>();
        MainItem mainItem = new MainItem("VonPhotoUtils操作UZrop裁剪图片",0);
        mData.add(mainItem);
        MainItem mainItem1 = new MainItem("二维码的扫描与生成",0);
        mData.add(mainItem1);
    }

    private void initView() {
        if (mColumnCount <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }


        recyclerview.addItemDecoration(new VonRecyclerViewUtils.SpaceItemDecoration(5));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        recyclerview.setAdapter(recyclerViewMain);
    }
}
