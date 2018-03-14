package com.vondear.rxtools.view.wheelhorizontal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author vondear
 * The simple Array spinnerwheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {
    
    // items
    private T items[];

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public ArrayWheelAdapter(Context context, T items[]) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            T item = items[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
    	// TODO Auto-generated method stub
    	return super.getItem(index, convertView, parent);
    }
    
    @Override
    public int getItemsCount() {
        return items.length;
    }
}
