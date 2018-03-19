package com.vondear.tools.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxRecyclerViewDividerTool;
import com.vondear.rxtools.activity.ActivityCodeTool;
import com.vondear.tools.R;
import com.vondear.tools.adapter.AdapterRecyclerViewMain;
import com.vondear.tools.model.ModelMainItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author vondear
 */
public class ActivityMain extends AppCompatActivity {

    //双击返回键 退出
    //----------------------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<ModelMainItem> mData;
    private int mColumnCount = 3;
    private ActivityMain mContext;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initData();
        initView();

        RxPermissionsTool.
                with(mContext).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new ModelMainItem("RxPhotoTool操作UZrop裁剪图片", R.drawable.circle_elves_ball, ActivityRxPhoto.class));
        mData.add(new ModelMainItem("二维码与条形码的扫描与生成", R.drawable.circle_dynamic_generation_code, ActivityCodeTool.class));
        mData.add(new ModelMainItem("动态生成码", R.drawable.circle_qr_code, ActivityCreateQRCode.class));

        mData.add(new ModelMainItem("WebView的封装可播放视频", R.drawable.circle_webpage, com.vondear.rxtools.activity.ActivityWebView.class));
        mData.add(new ModelMainItem("常用的Dialog展示", R.drawable.circle_dialog, ActivityDialog.class));
        mData.add(new ModelMainItem("图片的缩放艺术", R.drawable.circle_scale_icon, ActivityRxScaleImageView.class));

        mData.add(new ModelMainItem("RxDataTool操作Demo", R.drawable.circle_data, ActivityRxDataTool.class));
        mData.add(new ModelMainItem("设备信息", R.drawable.circle_device_info, ActivityDeviceInfo.class));
        mData.add(new ModelMainItem("RxTextTool操作Demo", R.drawable.circle_text, ActivityTextTool.class));

        mData.add(new ModelMainItem("进度条的艺术", R.drawable.circle_bar, ActivityProgressBar.class));
        mData.add(new ModelMainItem("加载的艺术", R.drawable.circle_loading_icon, ActivityLoading.class));
        mData.add(new ModelMainItem("点赞控件", R.drawable.circle_heart_circle, ActivityLike.class));

        mData.add(new ModelMainItem("旋转引擎View", R.drawable.circle_rotate, ActivityRxRotateBar.class));
        mData.add(new ModelMainItem("蛛网等级View", R.drawable.circle_cobweb, ActivityCobweb.class));
        mData.add(new ModelMainItem("添加购物车控件", R.drawable.circle_shop_cart, ActivityShoppingView.class));

        mData.add(new ModelMainItem("网速控件", R.drawable.circle_net_speed, ActivityNetSpeed.class));
        mData.add(new ModelMainItem("验证码", R.drawable.circle_captcha, ActivityRxCaptcha.class));
        mData.add(new ModelMainItem("横向滑动选择控件", R.drawable.circle_bookshelf, ActivityWheelHorizontal.class));

        mData.add(new ModelMainItem("横向左右自动滚动的ImageView", R.drawable.circle_two_way, ActivityAutoImageView.class));
        mData.add(new ModelMainItem("SlidingDrawerSingle使用", R.drawable.circle_up_down, ActivitySlidingDrawerSingle.class));
        mData.add(new ModelMainItem("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar.class));

        mData.add(new ModelMainItem("登录界面", R.drawable.circle_clound, ActivityLoginAct.class));
        mData.add(new ModelMainItem("PopupView的使用", R.drawable.circle_bullet, ActivityPopupView.class));
        mData.add(new ModelMainItem("RxToast的使用", R.drawable.circle_toast, ActivityRxToast.class));

        mData.add(new ModelMainItem("RunTextView的使用", R.drawable.circle_wrap_text, ActivityRunTextView.class));
        mData.add(new ModelMainItem("选座控件", R.drawable.circle_seat, ActivitySeat.class));
        mData.add(new ModelMainItem("银行卡组堆叠控件", R.drawable.circle_credit_card, ActivityCardStack.class));

        mData.add(new ModelMainItem("联系人侧边快速导航", R.drawable.circle_phone, ActivityContact.class));
        mData.add(new ModelMainItem("GPS原生定位", R.drawable.circle_gps_icon, ActivityLocation.class));
        mData.add(new ModelMainItem("震动的艺术", R.drawable.circle_vibrate, ActivityVibrate.class));

        mData.add(new ModelMainItem("压缩与加密的艺术", R.drawable.circle_zip, ActivityZipEncrypt.class));
        mData.add(new ModelMainItem("图片添加经纬度信息", R.drawable.circle_picture_location, ActivityRxExifTool.class));
        mData.add(new ModelMainItem("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView.class));
        mData.add(new ModelMainItem("app检测更新与安装", R.mipmap.ic_launcher, ActivitySplash.class));
        mData.add(new ModelMainItem("PULL解析XML", R.mipmap.ic_launcher, ActivityXmlParse.class));
    }

    private void initView() {
        if (mColumnCount <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }

        recyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        recyclerview.setAdapter(recyclerViewMain);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
    //==============================================================================================
}
