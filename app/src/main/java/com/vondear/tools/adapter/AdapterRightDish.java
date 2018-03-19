package com.vondear.tools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.tools.model.ModelDish;
import com.vondear.tools.model.ModelDishMenu;
import com.vondear.tools.model.ModelShopCart;
import com.vondear.tools.interfaces.ShopCartInterface;

import java.util.ArrayList;

/**
 * @author vondear
 * @date 16-11-10
 */
public class AdapterRightDish extends RecyclerView.Adapter {
    private final int MENU_TYPE = 0;
    private final int DISH_TYPE = 1;
    private final int HEAD_TYPE = 2;

    private Context mContext;
    private ArrayList<ModelDishMenu> mMenuList;
    private int mItemCount;
    private ModelShopCart mModelShopCart;
    private ShopCartInterface shopCartImp;

    public AdapterRightDish(Context mContext, ArrayList<ModelDishMenu> mMenuList, ModelShopCart modelShopCart){
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.mItemCount = mMenuList.size();
        this.mModelShopCart = modelShopCart;
        for(ModelDishMenu menu:mMenuList){
            mItemCount+=menu.getModelDishList().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int sum=0;
        for(ModelDishMenu menu:mMenuList){
            if(position==sum){
                return MENU_TYPE;
            }
            sum+=menu.getModelDishList().size()+1;
        }
        return DISH_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==MENU_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_menu_item, parent, false);
            MenuViewHolder viewHolder = new MenuViewHolder(view);
            return viewHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_dish_item1, parent, false);
            DishViewHolder viewHolder = new DishViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position)==MENU_TYPE){
            MenuViewHolder menuholder = (MenuViewHolder)holder;
            if(menuholder!=null) {
                menuholder.right_menu_title.setText(getMenuByPosition(position).getMenuName());
                menuholder.right_menu_layout.setContentDescription(position+"");
            }
        }else {
            final DishViewHolder dishholder = (DishViewHolder) holder;
            if (dishholder != null) {

                final ModelDish modelDish = getDishByPosition(position);
                dishholder.right_dish_name_tv.setText(modelDish.getDishName());
                dishholder.right_dish_price_tv.setText(modelDish.getDishPrice()+"");
                dishholder.right_dish_layout.setContentDescription(position+"");

                int count = 0;
                if(mModelShopCart.getShoppingSingleMap().containsKey(modelDish)){
                    count = mModelShopCart.getShoppingSingleMap().get(modelDish);
                }
                if(count<=0){
                    dishholder.right_dish_remove_iv.setVisibility(View.GONE);
                    dishholder.right_dish_account_tv.setVisibility(View.GONE);
                }else {
                    dishholder.right_dish_remove_iv.setVisibility(View.VISIBLE);
                    dishholder.right_dish_account_tv.setVisibility(View.VISIBLE);
                    dishholder.right_dish_account_tv.setText(count+"");
                }
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
                            notifyItemChanged(position);
                            if(shopCartImp!=null)
                                shopCartImp.remove(view,position);
                        }
                    }
                });
            }
        }
    }

    public ModelDishMenu getMenuByPosition(int position){
        int sum =0;
        for(ModelDishMenu menu:mMenuList){
            if(position==sum){
                return menu;
            }
            sum+=menu.getModelDishList().size()+1;
        }
        return null;
    }

    public ModelDish getDishByPosition(int position){
        for(ModelDishMenu menu:mMenuList){
            if(position>0 && position<=menu.getModelDishList().size()){
                return menu.getModelDishList().get(position-1);
            }
            else{
                position-=menu.getModelDishList().size()+1;
            }
        }
        return null;
    }

    public ModelDishMenu getMenuOfMenuByPosition(int position){
        for(ModelDishMenu menu:mMenuList){
            if(position==0)return menu;
            if(position>0 && position<=menu.getModelDishList().size()){
                return menu;
            }
            else{
                position-=menu.getModelDishList().size()+1;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout right_menu_layout;
        private TextView right_menu_title;
        public MenuViewHolder(View itemView) {
            super(itemView);
            right_menu_layout = (LinearLayout)itemView.findViewById(R.id.right_menu_item);
            right_menu_title = (TextView)itemView.findViewById(R.id.right_menu_tv);
        }
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
