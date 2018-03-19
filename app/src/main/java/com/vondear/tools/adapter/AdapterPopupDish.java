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
import com.vondear.tools.model.ModelDish;
import com.vondear.tools.model.ModelShopCart;
import com.vondear.tools.interfaces.ShopCartInterface;

import java.util.ArrayList;

/**
 *
 * @author vondear
 * @date 16-12-23
 */
public class AdapterPopupDish extends RecyclerView.Adapter{

    private static String TAG = "PopupDishAdapter";
    private ModelShopCart mModelShopCart;
    private Context context;
    private int itemCount;
    private ArrayList<ModelDish> mModelDishList;
    private ShopCartInterface shopCartImp;

    public AdapterPopupDish(Context context, ModelShopCart modelShopCart){
        this.mModelShopCart = modelShopCart;
        this.context = context;
        this.itemCount = modelShopCart.getDishAccount();
        this.mModelDishList = new ArrayList<>();
        mModelDishList.addAll(modelShopCart.getShoppingSingleMap().keySet());
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
        final ModelDish modelDish = getDishByPosition(position);
        if(modelDish !=null) {
            dishholder.right_dish_name_tv.setText(modelDish.getDishName());
            dishholder.right_dish_price_tv.setText(modelDish.getDishPrice() + "");
            int num = mModelShopCart.getShoppingSingleMap().get(modelDish);
            dishholder.right_dish_account_tv.setText(num+"");

            dishholder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mModelShopCart.addShoppingSingle(modelDish)) {
                        notifyItemChanged(position);
                        if(shopCartImp!=null)
                            shopCartImp.add(view,position);
                    }
                }
            });

            dishholder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mModelShopCart.subShoppingSingle(modelDish)){
                        mModelDishList.clear();
                        mModelDishList.addAll(mModelShopCart.getShoppingSingleMap().keySet());
                        itemCount = mModelShopCart.getDishAccount();
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

    public ModelDish getDishByPosition(int position){
       return mModelDishList.get(position);
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
