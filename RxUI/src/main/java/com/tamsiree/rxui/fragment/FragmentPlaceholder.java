package com.tamsiree.rxui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamsiree.rxui.R;


public class FragmentPlaceholder extends FragmentLazy {


    public static FragmentPlaceholder newInstance() {
        return new FragmentPlaceholder();
    }

    @Override
    protected View initViews(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_placeholder, viewGroup, false);
        return view;
    }

    @Override
    protected void initData() {

    }

}
