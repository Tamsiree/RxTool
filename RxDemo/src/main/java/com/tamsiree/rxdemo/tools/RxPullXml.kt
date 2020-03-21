package com.tamsiree.rxdemo.tools

import android.widget.EditText
import com.orhanobut.logger.Logger
import com.tamsiree.rxdemo.model.CityModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * @author tamsiree
 * @date 2018/3/19
 *
 *
 * PULL 框架 解析 XML 的 DEMO
 */
class RxPullXml {
    enum class unitType {
        /**
         * 省份
         */
        TYPE_PROVINCE,

        /**
         * 市州
         */
        TYPE_CITY,

        /**
         * 区县
         */
        TYPE_COUNTY
    }

    fun parseXMLWithPull(inputStream: InputStream?, mEdXmlData: EditText): MutableList<CityModel> {
        val provinceList: MutableList<CityModel> = ArrayList()
        var provinceModel = CityModel()
        var cityModel = CityModel()
        var countyModel = CityModel()
        try {
            val sb = StringBuilder("")
            val pullParserFactory = XmlPullParserFactory.newInstance()
            val pullParser = pullParserFactory.newPullParser()
            pullParser.setInput(InputStreamReader(inputStream))
            /**
             * Pull解析是基于事件的解析，因此专门定了几个常量表示状态
             * START_DOCUMENT 0（开始解析文档）
             * END_DOCUMENT 1（结束解析文档）
             * START_TAG 2(开始解析标签)
             * END_TAG 3(结束解析标签)
             * TEXT 4 (解析文本时用的)
             */
            var eventType = pullParser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val nodeName = pullParser.name
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                    }
                    XmlPullParser.START_TAG -> if (nodeName.endsWith("province")) {
                        //可以调用XmlPullParser的getAttributte()方法来获取属性的值
                        sb.append("""
省份：${pullParser.getAttributeValue(0)}	${pullParser.getAttributeValue(1)}
""")
                        provinceModel = CityModel()
                        provinceModel.cityName = pullParser.getAttributeValue(0)
                        provinceModel.cityCode = pullParser.getAttributeValue(1)
                        provinceModel.cityType = unitType.TYPE_PROVINCE
                        provinceModel.countyList = ArrayList()
                    } else if (nodeName == "city") {
                        //可以调用XmlPullParser的nextText()方法来获取节点的值
                        sb.append("""	市州：${pullParser.getAttributeValue(0)}	${pullParser.getAttributeValue(1)}
""")
                        cityModel = CityModel()
                        cityModel.cityName = pullParser.getAttributeValue(0)
                        cityModel.cityCode = pullParser.getAttributeValue(1)
                        cityModel.cityType = unitType.TYPE_CITY
                        cityModel.countyList = ArrayList()
                    } else if (nodeName == "county") {
                        sb.append("""		区县：${pullParser.getAttributeValue(0)}	${pullParser.getAttributeValue(1)}
""")
                        countyModel = CityModel()
                        countyModel.cityName = pullParser.getAttributeValue(0)
                        countyModel.cityCode = pullParser.getAttributeValue(1)
                        countyModel.cityType = unitType.TYPE_COUNTY
                    }
                    XmlPullParser.END_TAG -> if ("province" == nodeName) {
                        provinceList.add(provinceModel)
                    } else if ("city" == nodeName) {
                        val temp = provinceModel.countyList
                        temp?.add(cityModel)
                        provinceModel.countyList = temp
                    } else if ("county" == nodeName) {
                        val temp = provinceModel.countyList
                        temp?.add(countyModel)
                        cityModel.countyList = temp
                    }
                    XmlPullParser.TEXT -> {
                    }
                    else -> {
                    }
                }
                //不断读取下一条，直到结束
                eventType = pullParser.next()
            }
            Logger.d(sb)
            mEdXmlData.setText(sb)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return provinceList
    }

    companion object {
        @JvmStatic
        fun newInstance(): RxPullXml {
            return RxPullXml()
        }
    }
}