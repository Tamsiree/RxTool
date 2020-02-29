package com.tamsiree.rxpay.alipay;

/**
 * @author Tamsiree
 * @date 2017/4/18
 */

public class AliPayModel {
    private String outTradeNo;
    private String money;
    private String name;
    private String detail;

    public AliPayModel(String outTradeNo, String money, String name, String detail) {
        this.outTradeNo = outTradeNo;
        this.money = money;
        this.name = name;
        this.detail = detail;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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
