package com.vondear.tools.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vondear.rxtools.view.progress.SpinKitView;
import com.vondear.rxtools.view.progress.SpriteFactory;
import com.vondear.rxtools.view.progress.Style;
import com.vondear.rxtools.view.progress.sprite.Sprite;
import com.vondear.tools.R;
import com.vondear.tools.activity.ActivityLoadingDetail;

/**
 * Created by ybq.
 */
public class Page1Fragment extends Fragment  {

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

    public static Page1Fragment newInstance() {
        return new Page1Fragment();
    }


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page1, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                holder.bind(position);
            }

            @Override
            public int getItemCount() {
                return Style.values().length;
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder {

        SpinKitView spinKitView;

        public Holder(View itemView) {
            super(itemView);
            spinKitView = (SpinKitView) itemView.findViewById(R.id.spin_kit);
        }

        public void bind(int position) {
            itemView.setBackgroundColor(colors[position % colors.length]);
            final int finalPosition = position;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityLoadingDetail.start(v.getContext(), finalPosition);
                }
            });
            position = position % 15;
            Style style = Style.values()[position];
            Sprite drawable = SpriteFactory.create(style);
            spinKitView.setIndeterminateDrawable(drawable);
        }
    }

}
