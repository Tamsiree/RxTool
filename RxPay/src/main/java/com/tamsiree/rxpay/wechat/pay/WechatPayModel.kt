package com.tamsiree.rxpay.wechat.pay

/**
 * @author Tamsiree
 * @date 2017/4/17
 */
class WechatPayModel(private val appid: String, private val partnerid: String, private val prepayid: String, private val packageValue: String, private val noncestr: String, private val timestamp: String, private val sign: String)