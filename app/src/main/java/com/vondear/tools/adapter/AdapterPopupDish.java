package com.vondear.tools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.tools.bean.Dish;
import com.vondear.tools.bean.ShopCart;
import com.vondear.tools.interfaces.ShopCartInterface;

import java.util.ArrayList;

/**
 * Created by cheng on 16-12-23.
 */
public class AdapterPopupDish extends RecyclerView.Adapter{

    private static String TAG = "PopupDishAdapter";
    private ShopCart shopCart;
    private Context context;
    private int itemCount;
    private ArrayList<Dish> dishList;
    private ShopCartInterface shopCartImp;

    public AdapterPopupDish(Context context, ShopCart shopCart){
        this.shopCart = shopCart;
        this.context = context;
        this.itemCount = shopCart.getDishAccount();
        this.dishList = new ArrayList<>();
        dishList.addAll(shopCart.getShoppingSingleMap().keySet());
        Log.e(TAG, "PopupDishAdapter: "+this.itemCount );
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_dish_item1, parent, false);
        DishViewHolder viewHolder = new DishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DishViewHolder dishholder = (DishViewHolder)holder;
        final Dish dish = getDishByPosition(position);
        if(dish!=null) {
            dishholder.right_dish_name_tv.setText(dish.getDishName());
            dishholder.right_dish_price_tv.setText(dish.getDishPrice() + "");
            int num = shopCart.getShoppingSingleMap().get(dish);
            dishholder.right_dish_account_tv.setText(num+"");

            dishholder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopCart.addShoppingSingle(dish)) {
                        notifyItemChanged(position);
                        if(shopCartImp!=null)
                            shopCartImp.add(view,position);
                    }
                }
            });

            dishholder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopCart.subShoppingSingle(dish)){
                        dishList.clear();
                        dishList.addAll(shopCart.getShoppingSingleMap().keySet());
                        itemCount = shopCart.getDishAccount();
                        notifyDataSetChanged();
                        if(shopCartImp!=null)
                            shopCartImp.remove(view,position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    public Dish getDishByPosition(int position){
       return dishList.get(position);
    }

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView right_dish_name_tv;
        private TextView right_dish_price_tv;
        private LinearLayout right_dish_layout;
        private ImageView right_dish_remove_iv;
        private ImageView right_dish_add_iv;
        private TextView right_dish_account_tv;

        public DishViewHolder(View itemView) {
            super(itemView);
            right_dish_name_tv = (TextView)itemView.findViewById(R.id.right_dish_name);
            right_dish_price_tv = (TextView)itemView.findViewById(R.id.right_dish_price);
            right_dish_layout = (LinearLayout)itemView.findViewById(R.id.right_dish_item);
            right_dish_remove_iv = (ImageView)itemView.findViewById(R.id.right_dish_remove);
            right_dish_add_iv = (ImageView)itemView.findViewById(R.id.right_dish_add);
            right_dish_account_tv = (TextView) itemView.findViewById(R.id.right_dish_account);
        }

    }
}
