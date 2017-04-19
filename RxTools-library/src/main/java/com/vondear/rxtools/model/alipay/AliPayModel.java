package com.vondear.rxtools.model.alipay;

/**
 * Created by Administrator on 2017/4/18.
 */

public class AliPayModel {
    private String out_trade_no;
    private String money;
    private String name;
    private String detail;

    public AliPayModel(String out_trade_no, String money, String name, String detail) {
        this.out_trade_no = out_trade_no;
        this.money = money;
        this.name = name;
        this.detail = detail;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
