package com.vondear.rxtools.model.wechat.pay;

/**
 * Created by Administrator on 2017/4/17.
 */

public class WechatPayModel {

    private String appid;
    private String partnerid;
    private String prepayid;
    private String packageValue;
    private String noncestr;
    private String timestamp;

    private String sign;

    public WechatPayModel(String appid, String partnerid, String prepayid, String packageValue, String noncestr, String timestamp, String sign) {
        this.appid = appid;
        this.partnerid = partnerid;
        this.prepayid = prepayid;
        this.packageValue = packageValue;
        this.noncestr = noncestr;
        this.timestamp = timestamp;
        this.sign = sign;
    }
}
