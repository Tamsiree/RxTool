package com.vondear.tools.model;

/**
 * @author vondear
 * @date 16-11-10
 */
public class ModelDish {

    private String dishName;
    private double dishPrice;
    private int dishAmount;
    private int dishRemain;

    public ModelDish(String dishName, double dishPrice, int dishAmount){
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishAmount = dishAmount;
        this.dishRemain = dishAmount;
    }


    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public int getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(int dishAmount) {
        this.dishAmount = dishAmount;
    }

    public int getDishRemain() {
        return dishRemain;
    }

    public void setDishRemain(int dishRemain) {
        this.dishRemain = dishRemain;
    }

    @Override
    public int hashCode(){
        int code = this.dishName.hashCode()+(int)this.dishPrice;
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this)return true;

        return obj instanceof ModelDish &&
                this.dishName.equals(((ModelDish)obj).dishName) &&
                this.dishPrice ==  ((ModelDish)obj).dishPrice &&
                this.dishAmount == ((ModelDish)obj).dishAmount &&
                this.dishRemain == ((ModelDish)obj).dishRemain;
    }
}
