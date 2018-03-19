package com.vondear.tools.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.tools.model.ModelContactCity;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;


/**
 * @author vondear
 */
public class AdapterContactCity extends BaseTurboAdapter<ModelContactCity, BaseViewHolder> {

    public AdapterContactCity(Context context) {
        super(context);
    }

    public AdapterContactCity(Context context, List<ModelContactCity> data) {
        super(context, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        ModelContactCity city = getItem(position);
        return city.type;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new CityHolder(inflateItemView(R.layout.item_wave_contact, parent));
        else
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
    }


    @Override
    protected void convert(BaseViewHolder holder, ModelContactCity item) {
        if (holder instanceof CityHolder) {
            ((CityHolder) holder).city_name.setText(item.name);
        }else {
            String letter = item.pys.substring(0, 1);
            ((PinnedHolder) holder).city_tip.setText(letter);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(getData().get(i).type ==1 && getData().get(i).pys.equals(letter)){
                return i;
            }
        }
        return -1;
    }

    class CityHolder extends BaseViewHolder {

        TextView city_name;

        public CityHolder(View view) {
            super(view);
            city_name = findViewById(R.id.tv_contact_name);
        }
    }


    class PinnedHolder extends BaseViewHolder {

        TextView city_tip;

        public PinnedHolder(View view) {
            super(view);
            city_tip = findViewById(R.id.city_tip);
        }
    }
}
