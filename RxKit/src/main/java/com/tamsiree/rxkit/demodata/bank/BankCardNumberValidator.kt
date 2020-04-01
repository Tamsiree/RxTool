package com.tamsiree.rxkit.demodata.bank

import com.tamsiree.rxkit.demodata.kit.LuhnUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

/**
 * <pre>
 * 银行卡号校验类
 * Created by Binary Wang on 2018/3/22.
</pre> *
 *
 * @author [Binary Wang](https://github.com/binarywang)
 */
object BankCardNumberValidator {
    /**
     * 校验银行卡号是否合法
     *
     * @param cardNo 银行卡号
     * @return 是否合法
     */
    fun validate(cardNo: String): Boolean {
        if (StringUtils.isEmpty(cardNo)) {
            return false
        }
        if (!NumberUtils.isDigits(cardNo)) {
            return false
        }
        if (cardNo.length > 19 || cardNo.length < 16) {
            return false
        }
        val luhnSum = LuhnUtils.getLuhnSum(cardNo.substring(0, cardNo.length - 1).trim { it <= ' ' }.toCharArray())
        val checkCode = if (luhnSum % 10 == 0) '0' else (10 - luhnSum % 10 + '0'.toInt()).toChar()
        return cardNo.substring(cardNo.length - 1)[0] == checkCode
    }
}