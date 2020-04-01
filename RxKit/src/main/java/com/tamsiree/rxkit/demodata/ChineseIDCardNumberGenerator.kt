package com.tamsiree.rxkit.demodata

import com.tamsiree.rxkit.demodata.base.GenericGenerator
import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

/**
 * 身份证号码
 * 1、号码的结构
 * 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 2、地址码(前六位数）
 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
 * 3、出生日期码（第七位至十四位）
 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * 4、顺序码（第十五位至十七位）
 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
 * 顺序码的奇数分配给男性，偶数分配给女性。
 * 5、校验码（第十八位数）
 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
 * X 9 8 7 6 5 4 3 2
 */
class ChineseIDCardNumberGenerator private constructor() : GenericGenerator() {
    override fun generate(): String {
        val code = areaCode
        val areaCode = code.keys.toTypedArray()[RandomUtils
                .nextInt(0, code.size)] + StringUtils.leftPad((RandomUtils.nextInt(0, 9998) + 1).toString() + "", 4,
                "0")
        val birthday = SimpleDateFormat("yyyyMMdd").format(randomDate())
        val randomCode = (1000 + RandomUtils.nextInt(0, 999)).toString().substring(1)
        val pre = areaCode + birthday + randomCode
        val verifyCode = getVerifyCode(pre)
        return pre + verifyCode
    }

    companion object {
        val instance: GenericGenerator = ChineseIDCardNumberGenerator()

        /**
         * 生成签发机关：XXX公安局/XX区分局
         * Authority
         */
        fun generateIssueOrg(): String {
            return ChineseAreaList.cityNameList[RandomUtils.nextInt(0, ChineseAreaList.cityNameList.size)]
                    .toString() + "公安局某某分局"
        }

        /**
         * 生成有效期限：20150906-20350906
         * Valid Through
         */
        fun generateValidPeriod(): String {
            val beginDate = DateTime(randomDate())
            val formater = "yyyyMMdd"
            val endDate = beginDate.withYear(beginDate.year + 20)
            return beginDate.toString(formater) + "-" + endDate.toString(formater)
        }

        fun randomDate(): Date {
            val calendar = Calendar.getInstance()
            calendar[1970, 1] = 1
            val earlierDate = calendar.time.time
            calendar[2000, 1] = 1
            val laterDate = calendar.time.time
            val chosenDate = RandomUtils.nextLong(earlierDate, laterDate)
            return Date(chosenDate)
        }

        private fun getVerifyCode(cardId: String): String {
            val ValCodeArr = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4",
                    "3", "2")
            val Wi = arrayOf("7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                    "9", "10", "5", "8", "4", "2")
            var tmp = 0
            for (i in Wi.indices) {
                tmp += cardId[i].toString().toInt() * Wi[i].toInt()
            }
            val modValue = tmp % 11
            return ValCodeArr[modValue]
        }

        private val areaCode: Map<String, String>
            private get() {
                val map: MutableMap<String, String> = mutableMapOf()
                map["11"] = "北京"
                map["12"] = "天津"
                map["13"] = "河北"
                map["14"] = "山西"
                map["15"] = "内蒙古"
                map["21"] = "辽宁"
                map["22"] = "吉林"
                map["23"] = "黑龙江"
                map["31"] = "上海"
                map["32"] = "江苏"
                map["33"] = "浙江"
                map["34"] = "安徽"
                map["35"] = "福建"
                map["36"] = "江西"
                map["37"] = "山东"
                map["41"] = "河南"
                map["42"] = "湖北"
                map["43"] = "湖南"
                map["44"] = "广东"
                map["45"] = "广西"
                map["46"] = "海南"
                map["50"] = "重庆"
                map["51"] = "四川"
                map["52"] = "贵州"
                map["53"] = "云南"
                map["54"] = "西藏"
                map["61"] = "陕西"
                map["62"] = "甘肃"
                map["63"] = "青海"
                map["64"] = "宁夏"
                map["65"] = "新疆"
                map["71"] = "台湾"
                map["81"] = "香港"
                map["82"] = "澳门"
                map["91"] = "国外"
                return map
            }
    }
}