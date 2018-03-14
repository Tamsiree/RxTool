package com.vondear.rxtools.view.cardstack.tools;

import android.content.Context;
import android.view.LayoutInflater;

import com.vondear.rxtools.view.cardstack.RxCardStackView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vondear
 */
public abstract class RxAdapterStack<T> extends RxCardStackView.Adapter<RxCardStackView.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<T> mData;

    public RxAdapterStack(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList();
    }

    public void updateData(List<T> data) {
        this.setData(data);
        this.notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override
    public void onBindViewHolder(RxCardStackView.ViewHolder holder, int position) {
        T data = this.getItem(position);
        this.bindView(data, position, holder);
    }

    public abstract void bindView(T data, int position, RxCardStackView.ViewHolder holder);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(int position) {
        return this.mData.get(position);
    }

}
