package com.tamsiree.rxkit

import com.tamsiree.rxkit.RxConstTool.REGEX_CHZ
import com.tamsiree.rxkit.RxConstTool.REGEX_DATE
import com.tamsiree.rxkit.RxConstTool.REGEX_EMAIL
import com.tamsiree.rxkit.RxConstTool.REGEX_IDCARD
import com.tamsiree.rxkit.RxConstTool.REGEX_IDCARD15
import com.tamsiree.rxkit.RxConstTool.REGEX_IDCARD18
import com.tamsiree.rxkit.RxConstTool.REGEX_IP
import com.tamsiree.rxkit.RxConstTool.REGEX_MOBILE_EXACT
import com.tamsiree.rxkit.RxConstTool.REGEX_MOBILE_SIMPLE
import com.tamsiree.rxkit.RxConstTool.REGEX_TEL
import com.tamsiree.rxkit.RxConstTool.REGEX_URL
import com.tamsiree.rxkit.RxConstTool.REGEX_USERNAME
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * @author Tamsiree
 * @date 2017/3/15
 */
object RxRegTool {
    //--------------------------------------------正则表达式-----------------------------------------
    /**
     * 原文链接：http://caibaojian.com/regexp-example.html
     * 提取信息中的网络链接:(h|H)(r|R)(e|E)(f|F) *= *('|")?(\w|\\|\/|\.)+('|"| *|>)?
     * 提取信息中的邮件地址:\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*
     * 提取信息中的图片链接:(s|S)(r|R)(c|C) *= *('|")?(\w|\\|\/|\.)+('|"| *|>)?
     * 提取信息中的IP地址:(\d+)\.(\d+)\.(\d+)\.(\d+)
     * 提取信息中的中国电话号码（包括移动和固定电话）:(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}
     * 提取信息中的中国邮政编码:[1-9]{1}(\d+){5}
     * 提取信息中的中国身份证号码:\d{18}|\d{15}
     * 提取信息中的整数：\d+
     * 提取信息中的浮点数（即小数）：(-?\d*)\.?\d+
     * 提取信息中的任何数字 ：(-?\d*)(\.\d+)?
     * 提取信息中的中文字符串：[\u4e00-\u9fa5]*
     * 提取信息中的双字节字符串 (汉字)：[^\x00-\xff]*
     */
    /**
     * 判断是否为真实手机号
     *
     * @param mobiles
     * @return
     */
    @JvmStatic
    fun isMobile(mobiles: String?): Boolean {
        val p = Pattern.compile("^(13[0-9]|15[012356789]|17[03678]|18[0-9]|14[57])[0-9]{8}$")
        val m = p.matcher(mobiles)
        return m.matches()
    }

    /**
     * 验证银卡卡号
     *
     * @param cardNo
     * @return
     */
    @JvmStatic
    fun isBankCard(cardNo: String?): Boolean {
        val p = Pattern.compile("^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$")
        val m = p.matcher(cardNo)
        return m.matches()
    }

    /**
     * 15位和18位身份证号码的正则表达式 身份证验证
     *
     * @param idCard
     * @return
     */
    @JvmStatic
    fun validateIdCard(idCard: String?): Boolean {
        // 15位和18位身份证号码的正则表达式
        val regIdCard = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$"
        val p = Pattern.compile(regIdCard)
        return p.matcher(idCard).matches()
    }
    //=========================================正则表达式=============================================
    /**
     * 验证手机号（简单）
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isMobileSimple(string: String?): Boolean {
        return isMatch(REGEX_MOBILE_SIMPLE, string)
    }

    /**
     * 验证手机号（精确）
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isMobileExact(string: String?): Boolean {
        return isMatch(REGEX_MOBILE_EXACT, string)
    }

    /**
     * 验证电话号码
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isTel(string: String?): Boolean {
        return isMatch(REGEX_TEL, string)
    }

    /**
     * 验证身份证号码15位
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isIDCard15(string: String?): Boolean {
        return isMatch(REGEX_IDCARD15, string)
    }

    /**
     * 验证身份证号码18位
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isIDCard18(string: String?): Boolean {
        return isMatch(REGEX_IDCARD18, string)
    }

    /**
     * 验证身份证号码15或18位 包含以x结尾
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isIDCard(string: String?): Boolean {
        return isMatch(REGEX_IDCARD, string)
    }
    /*********************************** 身份证验证开始  */
    /**
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
     * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
     * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
     * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */
    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"有效" 无效：返回String信息
     * @throws ParseException
     */
    @JvmStatic
    fun IDCardValidate(IDStr: String): String {
        var errorInfo = "" // 记录错误信息
        val ValCodeArr = arrayOf("1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2")
        val Wi = arrayOf("7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2")
        var Ai = ""
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length != 15 && IDStr.length != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。"
            return errorInfo
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length == 18) {
            Ai = IDStr.substring(0, 17)
        } else if (IDStr.length == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15)
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。"
            return errorInfo
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        val strYear = Ai.substring(6, 10) // 年份
        val strMonth = Ai.substring(10, 12) // 月份
        val strDay = Ai.substring(12, 14) // 月份
        if (isDate("$strYear-$strMonth-$strDay") == false) {
            errorInfo = "身份证生日无效。"
            return errorInfo
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd")
        try {
            if (gc[Calendar.YEAR] - strYear.toInt() > 150
                    || gc.time.time - s.parse(
                            "$strYear-$strMonth-$strDay").time < 0) {
                errorInfo = "身份证生日不在有效范围。"
                return errorInfo
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (strMonth.toInt() > 12 || strMonth.toInt() == 0) {
            errorInfo = "身份证月份无效"
            return errorInfo
        }
        if (strDay.toInt() > 31 || strDay.toInt() == 0) {
            errorInfo = "身份证日期无效"
            return errorInfo
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        val h = GetAreaCode()
        if (h[Ai.substring(0, 2)] == null) {
            errorInfo = "身份证地区编码错误。"
            return errorInfo
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        var TotalmulAiWi = 0
        for (i in 0..16) {
            TotalmulAiWi = (TotalmulAiWi
                    + Ai[i].toString().toInt() * Wi[i].toInt())
        }
        val modValue = TotalmulAiWi % 11
        val strVerifyCode = ValCodeArr[modValue]
        Ai = Ai + strVerifyCode
        if (IDStr.length == 18) {
            if (Ai == IDStr == false) {
                errorInfo = "身份证无效，不是合法的身份证号码"
                return errorInfo
            }
        } else {
            return "有效"
        }
        // =====================(end)=====================
        return "有效"
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private fun GetAreaCode(): Hashtable<String, String> {
        val hashtable = Hashtable<String, String>()
        hashtable["11"] = "北京"
        hashtable["12"] = "天津"
        hashtable["13"] = "河北"
        hashtable["14"] = "山西"
        hashtable["15"] = "内蒙古"
        hashtable["21"] = "辽宁"
        hashtable["22"] = "吉林"
        hashtable["23"] = "黑龙江"
        hashtable["31"] = "上海"
        hashtable["32"] = "江苏"
        hashtable["33"] = "浙江"
        hashtable["34"] = "安徽"
        hashtable["35"] = "福建"
        hashtable["36"] = "江西"
        hashtable["37"] = "山东"
        hashtable["41"] = "河南"
        hashtable["42"] = "湖北"
        hashtable["43"] = "湖南"
        hashtable["44"] = "广东"
        hashtable["45"] = "广西"
        hashtable["46"] = "海南"
        hashtable["50"] = "重庆"
        hashtable["51"] = "四川"
        hashtable["52"] = "贵州"
        hashtable["53"] = "云南"
        hashtable["54"] = "西藏"
        hashtable["61"] = "陕西"
        hashtable["62"] = "甘肃"
        hashtable["63"] = "青海"
        hashtable["64"] = "宁夏"
        hashtable["65"] = "新疆"
        hashtable["71"] = "台湾"
        hashtable["81"] = "香港"
        hashtable["82"] = "澳门"
        hashtable["91"] = "国外"
        return hashtable
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 验证邮箱
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isEmail(string: String?): Boolean {
        return isMatch(REGEX_EMAIL, string)
    }

    /**
     * 验证URL
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isURL(string: String?): Boolean {
        return isMatch(REGEX_URL, string)
    }

    /**
     * 验证汉字
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isChz(string: String?): Boolean {
        return isMatch(REGEX_CHZ, string)
    }

    /**
     * 验证用户名
     *
     * 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isUsername(string: String?): Boolean {
        return isMatch(REGEX_USERNAME, string)
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isDate(string: String?): Boolean {
        return isMatch(REGEX_DATE, string)
    }

    /**
     * 验证IP地址
     *
     * @param string 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isIP(string: String?): Boolean {
        return isMatch(REGEX_IP, string)
    }

    /**
     * string是否匹配regex正则表达式字符串
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    @JvmStatic
    fun isMatch(regex: String?, string: String?): Boolean {
        return !isNullString(string) && Pattern.matches(regex, string)
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *
     * **国家（地区） 代码 ：**标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     * 数字之后是空格分隔的国家（地区）代码。
     *
     * **区号（城市代码）：**这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。
     *
     * **电话号码：**这包含从 0 到 9 的一个或多个数字
     * @return 验证成功返回true，验证失败返回false
     */
    @JvmStatic
    fun checkPhone(phone: String?): Boolean {
        val regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"
        return Pattern.matches(regex, phone)
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    @JvmStatic
    fun checkDigit(digit: String?): Boolean {
        val regex = "\\-?[1-9]\\d+"
        return Pattern.matches(regex, digit)
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    @JvmStatic
    fun checkDecimals(decimals: String?): Boolean {
        val regex = "\\-?[1-9]\\d+(\\.\\d+)?"
        return Pattern.matches(regex, decimals)
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    @JvmStatic
    fun checkBlankSpace(blankSpace: String?): Boolean {
        val regex = "\\s+"
        return Pattern.matches(regex, blankSpace)
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    @JvmStatic
    fun checkBirthday(birthday: String?): Boolean {
        val regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}"
        return Pattern.matches(regex, birthday)
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    @JvmStatic
    fun checkPostcode(postcode: String?): Boolean {
        val regex = "[1-9]\\d{5}"
        return Pattern.matches(regex, postcode)
    }
}