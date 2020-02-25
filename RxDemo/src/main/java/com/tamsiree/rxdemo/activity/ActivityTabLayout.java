package com.tamsiree.rxdemo.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain;
import com.tamsiree.rxdemo.model.ModelMainItem;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxRecyclerViewDividerTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTabLayout extends ActivityBase {

    private final String[] mItems = {"SlidingTabLayout", "CommonTabLayout", "SegmentTabLayout"};
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
//    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class, SegmentTabActivity.class};

    private int mColumnCount = 1;
    private List<ModelMainItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new ModelMainItem("TTabLayout", R.drawable.circle_elves_ball, ActivityTTabLayout.class));
        mData.add(new ModelMainItem("TGlideTabLayout", R.drawable.circle_elves_ball, ActivityTGlideTabLayout.class));
        mData.add(new ModelMainItem("TSectionTabLayout", R.drawable.circle_elves_ball, ActivityTSectionTabLayout.class));
    }

    private void initView() {
        if (mColumnCount <= 1) {
            mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }

        mRecyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        mRecyclerview.setAdapter(recyclerViewMain);
    }

}
