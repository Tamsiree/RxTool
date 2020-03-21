package com.tamsiree.rxdemo.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain;
import com.tamsiree.rxdemo.model.ModelDemo;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxRecyclerViewDividerTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTabLayout extends ActivityBase {

    private final String[] mItems = {"TGlideTabLayout", "CommonTabLayout", "TSectionTabLayout"};
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
//    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class, SegmentTabActivity.class};

    private int mColumnCount = 2;
    private List<ModelDemo> mData;

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
        mData.add(new ModelDemo("TTabLayout", R.drawable.circle_elves_ball, ActivityTTabLayout.class));
        mData.add(new ModelDemo("TGlideTabLayout", R.drawable.circle_elves_ball, ActivityTGlideTabLayout.class));
        mData.add(new ModelDemo("TSectionTabLayout", R.drawable.circle_elves_ball, ActivityTSectionTabLayout.class));
    }

    private void initView() {
        mRxTitle.setLeftFinish(this);

        if (mColumnCount <= 1) {
            mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }

        mRecyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData, new AdapterRecyclerViewMain.ContentListener() {
            @Override
            public void setListener(int position) {
                RxActivityTool.skipActivity(mContext, mData.get(position).getActivity());
            }
        });

        mRecyclerview.setAdapter(recyclerViewMain);
    }

}
