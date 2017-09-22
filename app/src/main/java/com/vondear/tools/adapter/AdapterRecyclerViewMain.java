package com.vondear.tools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.tools.R;
import com.vondear.tools.bean.ModelMainItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vonde on 2016/11/13.
 */

public class AdapterRecyclerViewMain extends RecyclerView.Adapter<AdapterRecyclerViewMain.ViewHolder> {

    private int mScreenWidth, mItemWidth, mItemHeight;
    private Context context;
    private List<ModelMainItem> mValues;

    public AdapterRecyclerViewMain(List<ModelMainItem> items) {
        mValues = items;
    }

    @Override
    public AdapterRecyclerViewMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_main, parent, false);
        context = view.getContext();
      /*  mScreenWidth = RxDeviceTool.getScreenWidth(context) > RxDeviceTool.getScreenHeight(context) ? RxDeviceTool.getScreenHeight(context) : RxDeviceTool.getScreenWidth(context);
        mItemWidth = (mScreenWidth - 50) / 3;
        mItemHeight = mItemWidth * 6 / 4;
        GridLayoutManager.LayoutParams layoutParams = new GridLayoutManager.LayoutParams(mItemWidth, mItemHeight);
        view.setLayoutParams(layoutParams);*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);

        holder.tvName.setText(holder.mItem.getName());

        Glide.with(context).
                load(holder.mItem.getImage()).
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                thumbnail(0.5f).
                priority(Priority.HIGH).
                placeholder(R.drawable.pikachu_sit).
                error(R.drawable.pikachu_sit).
                fallback(R.drawable.pikachu_sit).
                into(holder.imageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxActivityTool.skipActivity(context, holder.mItem.getActivity());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ModelMainItem mItem;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
