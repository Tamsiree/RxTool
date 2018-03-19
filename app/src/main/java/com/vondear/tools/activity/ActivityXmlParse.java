package com.vondear.tools.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.tools.R;
import com.vondear.tools.model.CityModel;
import com.vondear.tools.tools.RxPullXml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author vondear
 * @date 2018/3/19 17:36:44
 * PULL解析是Android官方推荐的XML解析方式，以事件驱动来解析XML
 * 注：Android中自带了Pull解析的jar包，故不需额外导入第三方jar包
 */
public class ActivityXmlParse extends ActivityBase {


    @BindView(R.id.btn_parse_xml)
    Button mBtnParseXml;
    @BindView(R.id.ed_xml_data)
    EditText mEdXmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parse);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_parse_xml)
    public void onViewClicked() {

        getCities(mContext);

    }

    public List<CityModel> getCities(Context mContext) {
        AssetManager assetManager = mContext.getAssets();
        List<CityModel> cityModels = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open("city_data.xml");
            cityModels = RxPullXml.newInstance().parseXMLWithPull(inputStream, mEdXmlData);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityModels;
    }
}
