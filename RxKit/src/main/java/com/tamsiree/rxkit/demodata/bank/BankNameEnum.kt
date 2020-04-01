package com.tamsiree.rxkit.demodata.bank

import org.apache.commons.lang3.ArrayUtils

/**
 * <pre>
 * 常见银行名称枚举类
 * Created by Binary Wang on 2017-3-31.
 *
 * @author [binarywang(Binary Wang)](https://github.com/binarywang)
</pre> *
 */
enum class BankNameEnum {
    /**
     * <pre>
     * 中国工商银行
     * 中国工商银行VISA学生国际信用卡：427020
     * 中国工商银行VISA国际信用卡金卡：427030
     * 中国工商银行MC国际信用卡普通卡：530990
     * 中国工商银行新版人民币贷记卡普卡：622230
     * 中国工商银行新版人民币贷记卡金卡：622235
     * 中国工商银行新版信用卡(准贷)普卡：622210
     * 中国工商银行新版信用卡(准贷)金卡：622215
     * 中国工商银行牡丹灵通卡借记卡：622200
     * 中国工商银行原牡丹灵通卡借记卡：955880
    </pre> *
     */
    ICBC("102", "中国工商银行", "工行", arrayOf<Int>(622200, 955880), arrayOf<Int>(427020, 427030, 530990, 622230, 622235, 622210, 622215)),

    /**
     * <pre>
     * 中国农业银行
     * 中国农业银行人民币贷记卡 香港旅游卡贷记卡金卡：622836
     * 中国农业银行人民币贷记卡 香港旅游卡贷记卡普卡：622837
     * 中国农业银行世纪通宝借记卡：622848
     * 农业银行：552599、404119、404121、519412、403361、558730、520083、520082、519413、49102、404120、404118、53591、404117
    </pre> *
     */
    ABC("103", "中国农业银行", "农行"),

    /**
     * <pre>
     * 中国银行
     * 中国银行中银都市卡：622760
     * 中国银行BOC系列VISA标准卡普通卡/VISA高校认同卡：409666
     * 中国银行国航知音信用卡：438088
     * 中国银行上海市分行长城人民币贷记卡普通卡：622752
    </pre> *
     */
    BOC("104", "中国银行", "中行"),

    /**
     * <pre>
     * 中国建设银行
     * 中国建设银行VISA龙卡借记卡：436742
     * 中国建设银行VISA龙卡贷记卡：436745
     * 中国建设银行支付宝龙卡借记卡：622280
    </pre> *
     */
    CCB("105", "中国建设银行", "建行"),

    /**
     * <pre>
     * 交通银行
     * 交通银行VISA普通卡：458123
     * 交通银行MC信用卡普通卡：521899
     * 交通银行太平洋卡借记卡：622260
    </pre> *
     */
    BCOM("301", "交通银行", "交行"),

    /**
     * <pre>
     * 中信银行
     * 中信银行国航知音信用卡/万事达卡普通卡：518212
     * 中信银行理财宝卡借记卡：622690
     * 中信银行万事达卡金卡：520108
     * 中信银行蓝卡/I卡信用卡：622680
     * 中信银行：376968、376966、622918、622916、376969、622919、556617、403391、558916、514906、400360、433669、433667、433666、404173、404172、404159、404158、403393、403392、622689、622688、433668、404157、404171、404174、628209、628208、628206
    </pre> *
     */
    CITIC("302", "中信银行"),

    /**
     * <pre>
     * 中国光大银行
     * 光大银行卡号开头：406254、622655、622650、622658、356839、486497、481699、543159、425862、406252、356837、356838、356840、622161、628201、628202
    </pre> *
     */
    CEB("303", "中国光大银行"),

    /**
     * <pre>
     * 华夏银行
     * 华夏银行：539867,528709
     * 华夏银行MC钛金卡：523959
     * 华夏银行人民币卡金卡：622637
     * 华夏银行人民币卡普卡：622636
     * 华夏银行MC金卡：528708
     * 华夏银行MC普卡：539868
    </pre> *
     */
    HXB("304", "华夏银行"),

    /**
     * <pre>
     * 中国民生银行
     * 民生银行：407405,517636
     * 中国民生银行MC金卡：512466
     * 中国民生银行星座卡借记卡：415599
     * 中国民生银行VISA信用卡金卡：421870
     * 中国民生银行蝶卡银卡借记卡：622622
     * 民生银行：528948,552288,556610,622600,622601,622602,622603,421869,421871,628258
    </pre> *
     */
    CMBC("305", "中国民生银行"),

    /**
     * <pre>
     * 广东发展银行
     * 广东发展银行新理财通借记卡：622568
     * 广东发展银行南航明珠卡MC金卡：520152
     * 广东发展银行南航明珠卡MC普卡：520382
     * 广东发展银行理财通借记卡：911121
     * 广发真情卡：548844
    </pre> *
     */
    CGB("306", "广东发展银行"),

    /**
     * <pre>
     * 平安银行
     * 深圳平安银行：622155,622156
     * 深圳平安银行万事达卡普卡：528020
     * 深圳平安银行万事达卡金卡：526855
     * 深发展联名普卡：435744
     * 深发展卡普通卡：622526
     * 深发展联名金卡：435745
     * 深圳发展银行：998801,998802
     * 深发展卡金卡：622525
     * 深圳发展银行发展卡借记卡：622538
    </pre> *
     */
    PAB("307", "平安银行"),

    /**
     * <pre>
     * 招商银行
     * 招商银行哆啦A梦粉丝信用卡：518710
     * 招商银行哆啦A梦粉丝信用卡珍藏版卡面/MC贝塔斯曼金卡/MC车主卡：518718
     * 招商银行QQ一卡通借记卡：622588
     * 招商银行HELLO KITTY单币卡：622575
     * 招商银行：545947、521302、439229、552534、622577、622579、439227、479229、356890、356885、545948、545623、552580、552581、552582、552583、552584、552585、552586、552588、552589、645621、545619、356886、622578、622576、622581、439228、628262、628362、628362、628262
     * 招商银行JCB信用卡普通卡：356889
     * 招商银行VISA白金卡：439188
     * 招商银行VISA信用卡普通卡：439225招商银行VISA信用卡金卡：439226
    </pre> *
     */
    CMB("308", "招商银行", "招行"),

    /**
     * <pre>
     * 兴业银行
     * 兴业银行：451289、622902、622901、527414、524070、486493、486494、451290、523036、486861、622922
    </pre> *
     */
    CIB("309", "兴业银行"),

    /**
     * <pre>
     * 上海浦东发展银行
     * 上海浦东发展银行奥运WOW卡美元单币：418152
     * 上海浦东发展银行WOW卡/奥运WOW卡：456418
     * 上海浦东发展银行东方卡借记卡：622521
     * 上海浦东发展银行VISA普通卡：404738
     * 上海浦东发展银行VISA金卡：404739
     * 浦东发展银行：498451,622517,622518,515672,517650,525998,356850,356851,356852
    </pre> *
     */
    SPDB("310", "上海浦东发展银行"),

    /**
     * <pre>
     * 华润银行
    </pre> *
     */
    CR("999999", "华润银行", arrayOf<Int>(622363)),

    /**
     * <pre>
     * 渤海银行
    </pre> *
     */
    BHB("318", "渤海银行"),

    /**
     * <pre>
     * 徽商银行
    </pre> *
     */
    HSB("319", "徽商银行"),

    /**
     * <pre>
     * 江苏银行
    </pre> *
     */
    JSB_1("03133010", "江苏银行"),

    /**
     * <pre>
     * 江苏银行
    </pre> *
     */
    JSB("03133120", "江苏银行"),

    /**
     * <pre>
     * 上海银行
     * 上海银行VISA金卡：402674
     * 上海银行借记卡：622892
    </pre> *
     */
    SHB("04012900", "上海银行"),

    /**
     * <pre>
     * 中国邮政储蓄银行
     * 中国邮政储蓄绿卡借记卡：622188
    </pre> *
     */
    POST("403", "中国邮政储蓄银行"),

    /**
     * <pre>
     * 北京银行
     * 北京银行京卡借记卡：602969
    </pre> *
     */
    BOB("", "北京银行"),

    /**
     * <pre>
     * 宁波银行
     * 宁波银行：512431,520194,622318,622778
     * 宁波银行汇通卡人民币金卡/钻石联名卡：622282
    </pre> *
     */
    BON("", "宁波银行");

    /**
     * 银行代码
     */
    val code: String

    /**
     * 银行名称
     */
    val cardName: String

    /**
     * 银行简称
     */
    var abbrName: String? = null
        private set

    /**
     * 信用卡卡号前缀数组
     */
    lateinit var creditCardPrefixes: Array<Int>
        private set

    /**
     * 借记卡卡号前缀数组
     */
    lateinit var debitCardPrefixes: Array<Int>
        private set

    /**
     * 所有卡号前缀数组
     */
    lateinit var allCardPrefixes: Array<Int>
        private set

    constructor(code: String, name: String) {
        this.code = code
        this.cardName = name
    }

    constructor(code: String, name: String, abbrName: String?) {
        this.code = code
        this.cardName = name
        this.abbrName = abbrName
    }

    constructor(code: String, name: String, abbrName: String?, debitCardPrefixes: Array<Int>, creditCardPrefixes: Array<Int>) {
        this.code = code
        this.cardName = name
        this.abbrName = abbrName
        this.creditCardPrefixes = creditCardPrefixes
        this.debitCardPrefixes = debitCardPrefixes

        val temp = arrayOfNulls<Int>(creditCardPrefixes.size + debitCardPrefixes.size) as Array<Int>
        if (creditCardPrefixes.isNotEmpty()) {
            for (index in creditCardPrefixes.indices) {
                temp[index] = creditCardPrefixes[index]
            }
            if (debitCardPrefixes.isNotEmpty()) {
                for (index in debitCardPrefixes.indices) {
                    temp[index + this.creditCardPrefixes.size] = debitCardPrefixes[index]
                }
            }
        }

        allCardPrefixes = temp
    }

    constructor(code: String, name: String, debitCardPrefixes: Array<Int>) {
        this.code = code
        this.cardName = name
        this.debitCardPrefixes = debitCardPrefixes
        allCardPrefixes = debitCardPrefixes
    }

    constructor(code: String, name: String, debitCardPrefixes: Array<Int>, creditCardPrefixes: Array<Int>) {
        this.code = code
        this.cardName = name
        this.creditCardPrefixes = creditCardPrefixes
        this.debitCardPrefixes = debitCardPrefixes
        allCardPrefixes = ArrayUtils.addAll(this.creditCardPrefixes, *this.debitCardPrefixes)
    }

}