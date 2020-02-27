package com.tamsiree.rxdemo.activity;

import android.Manifest;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain;
import com.tamsiree.rxdemo.model.ModelMainItem;
import com.tamsiree.rxfeature.activity.ActivityCodeTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxPermissionsTool;
import com.tamsiree.rxkit.RxRecyclerViewDividerTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.activity.ActivityWebView;
import com.tamsiree.rxui.view.RxTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tamsiree
 */
public class ActivityMain extends ActivityBase {

    //双击返回键 退出
    //----------------------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview_function)
    RecyclerView mRecyclerviewFunction;
    @BindView(R.id.recyclerview_ui)
    RecyclerView mRecyclerviewUi;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;
    private List<ModelMainItem> mDataFunction = new ArrayList<>();
    private List<ModelMainItem> mDataUI = new ArrayList<>();
    private ActivityMain mContext;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
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

    private int mColumnCount = 2;

    private void initView() {
        initRecyclerView(mRecyclerviewFunction, mDataFunction);
        initRecyclerView(mRecyclerviewUi, mDataUI);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<ModelMainItem> mData) {
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }

        recyclerView.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        recyclerView.setAdapter(recyclerViewMain);
    }

    private void initData() {
        mDataFunction.clear();
        mDataUI.clear();

        //----------------------------------------------------功能性 Demo---------------------------------------------------------------------------------

        mDataFunction.add(new ModelMainItem("RxPhotoTool操作UZrop裁剪图片", R.drawable.circle_elves_ball, ActivityRxPhoto.class));
        mDataFunction.add(new ModelMainItem("动态生成码", R.drawable.circle_qr_code, ActivityCreateQRCode.class));

        mDataFunction.add(new ModelMainItem("二维码与条形码的扫描与生成", R.drawable.circle_dynamic_generation_code, ActivityCodeTool.class));
        mDataFunction.add(new ModelMainItem("图片添加经纬度信息", R.drawable.circle_picture_location, ActivityRxExifTool.class));

        mDataFunction.add(new ModelMainItem("WebView的封装可播放视频", R.drawable.circle_webpage, ActivityWebView.class));
        mDataFunction.add(new ModelMainItem("常用的Dialog展示", R.drawable.circle_dialog, ActivityDialog.class));
        mDataFunction.add(new ModelMainItem("图片的缩放艺术", R.drawable.circle_scale_icon, ActivityRxScaleImageView.class));

        mDataFunction.add(new ModelMainItem("添加购物车控件", R.drawable.circle_shop_cart, ActivityShoppingView.class));
        mDataFunction.add(new ModelMainItem("网速控件", R.drawable.circle_net_speed, ActivityNetSpeed.class));
        mDataFunction.add(new ModelMainItem("验证码", R.drawable.circle_captcha, ActivityRxCaptcha.class));

        mDataFunction.add(new ModelMainItem("SlidingDrawerSingle使用", R.drawable.circle_up_down, ActivitySlidingDrawerSingle.class));
        mDataFunction.add(new ModelMainItem("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar.class));
        mDataFunction.add(new ModelMainItem("视频时间轴", R.drawable.circle_ebuddy, ActivityTVideoTimeline.class));
        mDataFunction.add(new ModelMainItem("PopupView的使用", R.drawable.circle_bullet, ActivityPopupView.class));

        mDataFunction.add(new ModelMainItem("选座控件", R.drawable.circle_seat, ActivitySeat.class));
        mDataFunction.add(new ModelMainItem("GPS原生定位", R.drawable.circle_gps_icon, ActivityLocation.class));
        mDataFunction.add(new ModelMainItem("震动的艺术", R.drawable.circle_vibrate, ActivityVibrate.class));

        mDataFunction.add(new ModelMainItem("压缩与加密的艺术", R.drawable.circle_zip, ActivityZipEncrypt.class));
        mDataFunction.add(new ModelMainItem("PULL解析XML", R.drawable.circle_swap_vert, ActivityXmlParse.class));
        mDataFunction.add(new ModelMainItem("支付宝支付Demo", R.drawable.circle_alipay, ActivityAliPay.class));
        mDataFunction.add(new ModelMainItem("Hold住崩溃界面", R.drawable.circle_alipay, ActivityOnCrash.class));

        mDataFunction.add(new ModelMainItem("app检测更新与安装", R.mipmap.ic_launcher, ActivitySplash.class));

        //----------------------------------------------------UI Demo---------------------------------------------------------------------------------

        mDataUI.add(new ModelMainItem("RxDataTool操作Demo", R.drawable.circle_data, ActivityRxDataTool.class));
        mDataUI.add(new ModelMainItem("设备信息", R.drawable.circle_device_info, ActivityDeviceInfo.class));
        mDataUI.add(new ModelMainItem("RxTextTool操作Demo", R.drawable.circle_text, ActivityTextTool.class));

        mDataUI.add(new ModelMainItem("进度条的艺术", R.drawable.circle_bar, ActivityProgressBar.class));
        mDataUI.add(new ModelMainItem("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView.class));
        mDataUI.add(new ModelMainItem("橙汁加载", R.drawable.circle_origin, ActivityOrangeJuice.class));
        mDataUI.add(new ModelMainItem("加载的艺术", R.drawable.circle_loading_icon, ActivityLoading.class));
        mDataUI.add(new ModelMainItem("旋转引擎View", R.drawable.circle_rotate, ActivityRxRotateBar.class));

        mDataUI.add(new ModelMainItem("点赞控件", R.drawable.circle_heart_circle, ActivityLike.class));

        mDataUI.add(new ModelMainItem("蛛网等级View", R.drawable.circle_cobweb, ActivityCobweb.class));
        mDataUI.add(new ModelMainItem("横向滑动选择控件", R.drawable.circle_bookshelf, ActivityWheelHorizontal.class));

        mDataUI.add(new ModelMainItem("横向左右自动滚动的ImageView", R.drawable.circle_two_way, ActivityAutoImageView.class));
        mDataUI.add(new ModelMainItem("登录界面", R.drawable.circle_clound, ActivityLoginAct.class));
        mDataUI.add(new ModelMainItem("RxToast的使用", R.drawable.circle_toast, ActivityRxToast.class));

        mDataUI.add(new ModelMainItem("RunTextView的使用", R.drawable.circle_wrap_text, ActivityRunTextView.class));
        mDataUI.add(new ModelMainItem("银行卡组堆叠控件", R.drawable.circle_credit_card, ActivityCardStack.class));

        mDataUI.add(new ModelMainItem("联系人侧边快速导航", R.drawable.circle_phone, ActivityContact.class));


        mDataUI.add(new ModelMainItem("TabLayout", R.drawable.circle_pocket, ActivityTabLayout.class));
        mDataUI.add(new ModelMainItem("其他界面效果", R.drawable.circle_icecandy, ActivityOtherEffect.class));


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
