package com.tamsiree.rxdemo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.activity.ActivityLoadingDetail;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxRecyclerViewDividerTool;
import com.tamsiree.rxui.view.progressing.SpinKitView;
import com.tamsiree.rxui.view.progressing.SpriteFactory;
import com.tamsiree.rxui.view.progressing.Style;
import com.tamsiree.rxui.view.progressing.sprite.Sprite;


/**
 * Created by Tamsiree.
 * @author tamsiree
 */
public class FragmentLoadingWay extends Fragment {

    int[] colors = new int[]{
            android.graphics.Color.parseColor("#99CCFF"),
            android.graphics.Color.parseColor("#34A853"),
    };

    public static FragmentLoadingWay newInstance() {
        return new FragmentLoadingWay();
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
        RecyclerView recyclerView = view.findViewById(R.id.list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
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
            spinKitView = itemView.findViewById(R.id.spin_kit);
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
