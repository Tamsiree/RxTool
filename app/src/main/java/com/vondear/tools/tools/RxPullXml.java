package com.vondear.tools.tools;

import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.vondear.tools.model.CityModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vondear
 * @date 2018/3/19
 * <p>
 * PULL 框架 解析 XML 的 DEMO
 */

public class RxPullXml {

    public static RxPullXml newInstance() {
        return new RxPullXml();
    }

    public enum unitType {

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

    public List<CityModel> parseXMLWithPull(InputStream inputStream,EditText mEdXmlData) {

        List<CityModel> provinceList = new ArrayList<>();
        CityModel provinceModel = new CityModel();
        CityModel cityModel = new CityModel();
        CityModel countyModel = new CityModel();

        try {
            StringBuilder sb = new StringBuilder("");
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser = pullParserFactory.newPullParser();
            pullParser.setInput(new InputStreamReader(inputStream));

            /**
             * Pull解析是基于事件的解析，因此专门定了几个常量表示状态
             * START_DOCUMENT 0（开始解析文档）
             * END_DOCUMENT 1（结束解析文档）
             * START_TAG 2(开始解析标签)
             * END_TAG 3(结束解析标签)
             * TEXT 4 (解析文本时用的)
             */
            int eventType = pullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (nodeName.endsWith("province")) {
                            //可以调用XmlPullParser的getAttributte()方法来获取属性的值
                            sb.append("\n省份：" + pullParser.getAttributeValue(0) + "\t" + pullParser.getAttributeValue(1) + "\n");

                            provinceModel = new CityModel();
                            provinceModel.setCityName(pullParser.getAttributeValue(0));
                            provinceModel.setCityCode(pullParser.getAttributeValue(1));
                            provinceModel.setCityType(unitType.TYPE_PROVINCE);
                            provinceModel.setCountyList(new ArrayList<CityModel>());

                        } else if (nodeName.equals("city")) {
                            //可以调用XmlPullParser的nextText()方法来获取节点的值
                            sb.append("\t市州：" + pullParser.getAttributeValue(0) + "\t" + pullParser.getAttributeValue(1) + "\n");

                            cityModel = new CityModel();
                            cityModel.setCityName(pullParser.getAttributeValue(0));
                            cityModel.setCityCode(pullParser.getAttributeValue(1));
                            cityModel.setCityType(unitType.TYPE_CITY);
                            cityModel.setCountyList(new ArrayList<CityModel>());
                        } else if (nodeName.equals("county")) {
                            sb.append("\t\t区县：" + pullParser.getAttributeValue(0) + "\t" + pullParser.getAttributeValue(1) + "\n");

                            countyModel = new CityModel();
                            countyModel.setCityName(pullParser.getAttributeValue(0));
                            countyModel.setCityCode(pullParser.getAttributeValue(1));
                            countyModel.setCityType(unitType.TYPE_COUNTY);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("province".equals(nodeName)) {
                            provinceList.add(provinceModel);
                        } else if ("city".equals(nodeName)) {
                            provinceModel.getCountyList().add(cityModel);
                        } else if ("county".equals(nodeName)) {
                            cityModel.getCountyList().add(countyModel);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                //不断读取下一条，直到结束
                eventType = pullParser.next();
            }
            Logger.d(sb);
            mEdXmlData.setText(sb);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provinceList;
    }

}
