package com.tamsiree.rxkit.demodata.bank

/**
 * <pre>
 * 银行卡类型枚举类
 * Created by Binary Wang on 2017-3-31.
 * @author [binarywang(Binary Wang)](https://github.com/binarywang)
</pre> *
 */
enum class BankCardTypeEnum(private val cardName: String) {
    /**
     * 借记卡/储蓄卡
     */
    DEBIT("借记卡/储蓄卡"),

    /**
     * 信用卡/贷记卡
     */
    CREDIT("信用卡/贷记卡");

}