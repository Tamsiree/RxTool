package com.vondear.tools.bean;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheng on 16-11-12.
 */
public class ShopCart {
    private int shoppingAccount;//商品总数
    private double shoppingTotalPrice;//商品总价钱
    private Map<Dish,Integer> shoppingSingle;//单个物品的总价价钱

    public ShopCart(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle = new HashMap<>();
    }

    public int getShoppingAccount() {
        return shoppingAccount;
    }

    public double getShoppingTotalPrice() {
        return shoppingTotalPrice;
    }

    public Map<Dish, Integer> getShoppingSingleMap() {
        return shoppingSingle;
    }

    public boolean addShoppingSingle(Dish dish){
        int remain = dish.getDishRemain();
        if(remain<=0)
            return false;
        dish.setDishRemain(--remain);
        int num = 0;
        if(shoppingSingle.containsKey(dish)){
            num = shoppingSingle.get(dish);
        }
        num+=1;
        shoppingSingle.put(dish,num);
        Log.e("TAG", "addShoppingSingle: "+shoppingSingle.get(dish));

        shoppingTotalPrice += dish.getDishPrice();
        shoppingAccount++;
        return true;
    }

    public boolean subShoppingSingle(Dish dish){
        int num = 0;
        if(shoppingSingle.containsKey(dish)){
            num = shoppingSingle.get(dish);
        }
        if(num<=0) return false;
        num--;
        int remain = dish.getDishRemain();
        dish.setDishRemain(++remain);
        shoppingSingle.put(dish,num);
        if (num ==0) shoppingSingle.remove(dish);

        shoppingTotalPrice -= dish.getDishPrice();
        shoppingAccount--;
        return true;
    }

    public int getDishAccount() {
        return shoppingSingle.size();
    }

    public void clear(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle.clear();
    }
}
