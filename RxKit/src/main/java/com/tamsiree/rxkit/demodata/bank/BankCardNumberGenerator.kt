package com.tamsiree.rxkit.demodata.bank

import com.tamsiree.rxkit.demodata.base.GenericGenerator
import com.tamsiree.rxkit.demodata.kit.LuhnUtils
import org.apache.commons.lang3.StringUtils.leftPad
import java.util.*

/**
 * <pre>
 * 生成随机银行卡号：
 *
 * 参考：效验是否为银行卡，用于验证：
 * 现行 16 位银联卡现行卡号开头 6 位是 622126～622925 之间的，7 到 15 位是银行自定义的，
 * 可能是发卡分行，发卡网点，发卡序号，第 16 位是校验码。
 * 16 位卡号校验位采用 Luhm 校验方法计算：
 * 1，将未带校验位的 15 位卡号从右依次编号 1 到 15，位于奇数位号上的数字乘以 2
 * 2，将奇位乘积的个十位全部相加，再加上所有偶数位上的数字
 * 3，将加法和加上校验位能被 10 整除。
</pre> *
 */
class BankCardNumberGenerator private constructor() : GenericGenerator() {
    override fun generate(): String {
        val random = randomInstance
        //        ContiguousSet<Integer> sets = ContiguousSet
//            .create(Range.closed(622126, 622925), DiscreteDomain.integers());
//        ImmutableList<Integer> list = sets.asList();
        val prev = 622126 + random.nextInt(925 + 1 - 126)
        return generateByPrefix(prev)
    }

    companion object {
        val instance: GenericGenerator = BankCardNumberGenerator()

        /**
         * <pre>
         * 根据给定前六位生成卡号
        </pre> *
         */
        fun generateByPrefix(prefix: Int): String {
            val random = Random(System.currentTimeMillis())
            val bardNo = prefix
                    .toString() + leftPad(random.nextInt(999999999).toString() + "", 9, "0")
            val chs = bardNo.trim { it <= ' ' }.toCharArray()
            val luhnSum = LuhnUtils.getLuhnSum(chs)
            val checkCode = if (luhnSum % 10 == 0) '0' else (10 - luhnSum % 10 + '0'.toInt()).toChar()
            return bardNo + checkCode
        }

        /**
         * 根据银行名称 及银行卡类型生成对应卡号
         *
         * @param bankName 银行名称
         * @param cardType 银行卡类型
         * @return 银行卡号
         */
        fun generate(bankName: BankNameEnum, cardType: BankCardTypeEnum?): String {
            var candidatePrefixes: Array<Int>? = null
            if (cardType == null) {
                candidatePrefixes = bankName.allCardPrefixes
            } else {
                when (cardType) {
                    BankCardTypeEnum.DEBIT -> candidatePrefixes = bankName.debitCardPrefixes
                    BankCardTypeEnum.CREDIT -> candidatePrefixes = bankName.creditCardPrefixes
                    else -> {
                    }
                }
            }
            if (candidatePrefixes == null || candidatePrefixes.size == 0) {
                throw RuntimeException("没有该银行的相关卡号信息")
            }
            val prefix = candidatePrefixes[Random().nextInt(candidatePrefixes.size)]
            return generateByPrefix(prefix)
        }
    }
}