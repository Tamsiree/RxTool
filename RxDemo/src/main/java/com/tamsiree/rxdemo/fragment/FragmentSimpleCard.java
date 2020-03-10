package com.tamsiree.rxdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tamsiree.rxdemo.R;


public class FragmentSimpleCard extends Fragment {
    private String mTitle;

    public FragmentSimpleCard() {
    }

    public static FragmentSimpleCard getInstance(String title) {
        FragmentSimpleCard sf = new FragmentSimpleCard();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_simple_card, null);
        TextView card_title_tv = v.findViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);

        return v;
    }
}