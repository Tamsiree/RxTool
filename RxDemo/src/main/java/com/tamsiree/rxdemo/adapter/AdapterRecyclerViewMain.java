package com.tamsiree.rxdemo.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.model.ModelDemo;

import java.util.List;

/**
 * @author tamsiree
 * @date 2016/11/13
 */


public class AdapterRecyclerViewMain extends BaseQuickAdapter<ModelDemo, BaseViewHolder> {

    private ContentListener mOnClickListener;

    public AdapterRecyclerViewMain(List<ModelDemo> data, ContentListener mOnClickListener) {
        super(R.layout.item_recyclerview_main, data);
        this.mOnClickListener = mOnClickListener;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ModelDemo item) {
        helper.setText(R.id.tv_name, item.getName());

        ImageView imageView = helper.getView(R.id.imageView);

        Glide.with(mContext).
                load(item.getImage()).
                thumbnail(0.5f).
                into(imageView);

        helper.setOnClickListener(R.id.ll_root, v -> mOnClickListener.setListerer(helper.getPosition()));
    }

    public interface ContentListener {
        void setListerer(int position);
    }

}
