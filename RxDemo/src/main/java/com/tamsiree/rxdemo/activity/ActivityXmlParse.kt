package com.tamsiree.rxdemo.activity

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.model.CityModel
import com.tamsiree.rxdemo.tools.RxPullXml.Companion.newInstance
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_xml_parse.*
import java.io.IOException
import java.util.*

/**
 * @author tamsiree
 * @date 2018/3/19 17:36:44
 * PULL解析是Android官方推荐的XML解析方式，以事件驱动来解析XML
 * 注：Android中自带了Pull解析的jar包，故不需额外导入第三方jar包
 */
class ActivityXmlParse : ActivityBase() {
    @JvmField
    @BindView(R.id.btn_parse_xml)
    var mBtnParseXml: Button? = null

    @JvmField
    @BindView(R.id.ed_xml_data)
    var mEdXmlData: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml_parse)
        ButterKnife.bind(this)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
    }

    override fun initData() {

    }

    @OnClick(R.id.btn_parse_xml)
    fun onViewClicked() {
        getCities(mContext)
    }

    fun getCities(mContext: Context): List<CityModel?> {
        val assetManager = mContext.assets
        var cityModels: List<CityModel?> = ArrayList()
        try {
            val inputStream = assetManager.open("city_data.xml")
            cityModels = newInstance().parseXMLWithPull(inputStream, mEdXmlData!!)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cityModels
    }
}