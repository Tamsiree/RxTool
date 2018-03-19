package com.vondear.tools.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.sidebar.PinnedHeaderDecoration;
import com.vondear.rxtools.view.sidebar.WaveSideBarView;
import com.vondear.tools.R;
import com.vondear.tools.adapter.AdapterContactCity;
import com.vondear.tools.model.ModelContactCity;
import com.vondear.tools.tools.ComparatorLetter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author vondear
 */
public class ActivityContact extends ActivityBase {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.side_view)
    WaveSideBarView mSideBarView;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    private AdapterContactCity mAdapterContactCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Type listType = new TypeToken<ArrayList<ModelContactCity>>() {
                }.getType();
                Gson gson = new Gson();
                final List<ModelContactCity> list = gson.fromJson(ModelContactCity.DATA, listType);
                Collections.sort(list, new ComparatorLetter());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterContactCity = new AdapterContactCity(mContext, list);
                        mRecyclerView.setAdapter(mAdapterContactCity);
                    }
                });
            }
        }).start();

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mAdapterContactCity.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }
}
