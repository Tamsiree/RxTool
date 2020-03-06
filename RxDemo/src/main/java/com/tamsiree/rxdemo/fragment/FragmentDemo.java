package com.tamsiree.rxdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.activity.ActivityAliPay;
import com.tamsiree.rxdemo.activity.ActivityAutoImageView;
import com.tamsiree.rxdemo.activity.ActivityCardStack;
import com.tamsiree.rxdemo.activity.ActivityCobweb;
import com.tamsiree.rxdemo.activity.ActivityContact;
import com.tamsiree.rxdemo.activity.ActivityCreateQRCode;
import com.tamsiree.rxdemo.activity.ActivityDeviceInfo;
import com.tamsiree.rxdemo.activity.ActivityDialog;
import com.tamsiree.rxdemo.activity.ActivityLike;
import com.tamsiree.rxdemo.activity.ActivityLoading;
import com.tamsiree.rxdemo.activity.ActivityLocation;
import com.tamsiree.rxdemo.activity.ActivityLoginAct;
import com.tamsiree.rxdemo.activity.ActivityNetSpeed;
import com.tamsiree.rxdemo.activity.ActivityOnCrash;
import com.tamsiree.rxdemo.activity.ActivityOrangeJuice;
import com.tamsiree.rxdemo.activity.ActivityOtherEffect;
import com.tamsiree.rxdemo.activity.ActivityPopupView;
import com.tamsiree.rxdemo.activity.ActivityProgressBar;
import com.tamsiree.rxdemo.activity.ActivityRunTextView;
import com.tamsiree.rxdemo.activity.ActivityRxCaptcha;
import com.tamsiree.rxdemo.activity.ActivityRxDataTool;
import com.tamsiree.rxdemo.activity.ActivityRxExifTool;
import com.tamsiree.rxdemo.activity.ActivityRxPhoto;
import com.tamsiree.rxdemo.activity.ActivityRxRotateBar;
import com.tamsiree.rxdemo.activity.ActivityRxScaleImageView;
import com.tamsiree.rxdemo.activity.ActivityRxToast;
import com.tamsiree.rxdemo.activity.ActivityRxWaveView;
import com.tamsiree.rxdemo.activity.ActivitySeat;
import com.tamsiree.rxdemo.activity.ActivitySeekBar;
import com.tamsiree.rxdemo.activity.ActivityShoppingView;
import com.tamsiree.rxdemo.activity.ActivitySlidingDrawerSingle;
import com.tamsiree.rxdemo.activity.ActivitySplash;
import com.tamsiree.rxdemo.activity.ActivityTVideoTimeline;
import com.tamsiree.rxdemo.activity.ActivityTabLayout;
import com.tamsiree.rxdemo.activity.ActivityTextTool;
import com.tamsiree.rxdemo.activity.ActivityVibrate;
import com.tamsiree.rxdemo.activity.ActivityWheelHorizontal;
import com.tamsiree.rxdemo.activity.ActivityXmlParse;
import com.tamsiree.rxdemo.activity.ActivityZipEncrypt;
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain;
import com.tamsiree.rxdemo.model.ModelDemo;
import com.tamsiree.rxfeature.activity.ActivityCodeTool;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxkit.RxRecyclerViewDividerTool;
import com.tamsiree.rxui.activity.ActivityWebView;
import com.tamsiree.rxui.fragment.FragmentLazy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDemo extends FragmentLazy implements SwipeRefreshLayout.OnRefreshListener {

    List<ModelDemo> mDemoList = new ArrayList<>();
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private AdapterRecyclerViewMain mAdapter;

    private int demo_type;

    private int mColumnCount = 2;

    private FragmentDemo(int demo_type) {
        this.demo_type = demo_type;
    }

    public static FragmentDemo newInstance(int friend_type) {
        return new FragmentDemo(friend_type);
    }

    @Override
    protected View initViews(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_demo, viewGroup, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        init();
        loadData();
    }

    private void init() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        //加载列表
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.blue_baby));

        if (mColumnCount <= 1) {
            mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }

        mRecyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));

        mAdapter = new AdapterRecyclerViewMain(mDemoList, new AdapterRecyclerViewMain.ContentListener() {
            @Override
            public void setListerer(int position) {
                RxActivityTool.skipActivity(mContext, mDemoList.get(position).getActivity());
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.isFirstOnly(true);
        mAdapter.bindToRecyclerView(mRecyclerview);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void loadData() {

        mSwipeLayout.setRefreshing(true);

        if (demo_type == 0) {
            getFunctionData();
        } else if (demo_type == 1) {
            getUIData();
        }


        if (mDemoList == null || mDemoList.size() <= 0) {
            mAdapter.setEmptyView(R.layout.load_data_empty);
        }


        mSwipeLayout.setRefreshing(false);
    }

    /**
     * 获得 功能性 Demo 数据
     */
    private void getFunctionData() {
        mDemoList.clear();
        mDemoList.add(new ModelDemo("RxPhotoTool操作UZrop裁剪图片", R.drawable.circle_elves_ball, ActivityRxPhoto.class));
        mDemoList.add(new ModelDemo("动态生成码", R.drawable.circle_qr_code, ActivityCreateQRCode.class));

        mDemoList.add(new ModelDemo("二维码与条形码的扫描与生成", R.drawable.circle_dynamic_generation_code, ActivityCodeTool.class));
        mDemoList.add(new ModelDemo("图片添加经纬度信息", R.drawable.circle_picture_location, ActivityRxExifTool.class));

        mDemoList.add(new ModelDemo("WebView的封装可播放视频", R.drawable.circle_webpage, ActivityWebView.class));
        mDemoList.add(new ModelDemo("常用的Dialog展示", R.drawable.circle_dialog, ActivityDialog.class));
        mDemoList.add(new ModelDemo("图片的缩放艺术", R.drawable.circle_scale_icon, ActivityRxScaleImageView.class));

        mDemoList.add(new ModelDemo("添加购物车控件", R.drawable.circle_shop_cart, ActivityShoppingView.class));
        mDemoList.add(new ModelDemo("网速控件", R.drawable.circle_net_speed, ActivityNetSpeed.class));
        mDemoList.add(new ModelDemo("验证码", R.drawable.circle_captcha, ActivityRxCaptcha.class));

        mDemoList.add(new ModelDemo("SlidingDrawerSingle使用", R.drawable.circle_up_down, ActivitySlidingDrawerSingle.class));
        mDemoList.add(new ModelDemo("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar.class));
        mDemoList.add(new ModelDemo("视频时间轴", R.drawable.circle_ebuddy, ActivityTVideoTimeline.class));
        mDemoList.add(new ModelDemo("PopupView的使用", R.drawable.circle_bullet, ActivityPopupView.class));

        mDemoList.add(new ModelDemo("选座控件", R.drawable.circle_seat, ActivitySeat.class));
        mDemoList.add(new ModelDemo("GPS原生定位", R.drawable.circle_gps_icon, ActivityLocation.class));
        mDemoList.add(new ModelDemo("震动的艺术", R.drawable.circle_vibrate, ActivityVibrate.class));

        mDemoList.add(new ModelDemo("压缩与加密的艺术", R.drawable.circle_zip, ActivityZipEncrypt.class));
        mDemoList.add(new ModelDemo("PULL解析XML", R.drawable.circle_swap_vert, ActivityXmlParse.class));
        mDemoList.add(new ModelDemo("支付宝支付Demo", R.drawable.circle_alipay, ActivityAliPay.class));
        mDemoList.add(new ModelDemo("Hold住崩溃界面", R.drawable.crash_logo, ActivityOnCrash.class));

        mDemoList.add(new ModelDemo("app检测更新与安装", R.mipmap.ic_launcher, ActivitySplash.class));
    }

    /**
     * 获得 UI Demo 数据
     */
    private void getUIData() {
        mDemoList.clear();
        mDemoList.add(new ModelDemo("RxDataTool操作Demo", R.drawable.circle_data, ActivityRxDataTool.class));
        mDemoList.add(new ModelDemo("设备信息", R.drawable.circle_device_info, ActivityDeviceInfo.class));
        mDemoList.add(new ModelDemo("RxTextTool操作Demo", R.drawable.circle_text, ActivityTextTool.class));

        mDemoList.add(new ModelDemo("进度条的艺术", R.drawable.circle_bar, ActivityProgressBar.class));
        mDemoList.add(new ModelDemo("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView.class));
        mDemoList.add(new ModelDemo("橙汁加载", R.drawable.circle_origin, ActivityOrangeJuice.class));
        mDemoList.add(new ModelDemo("加载的艺术", R.drawable.circle_loading_icon, ActivityLoading.class));
        mDemoList.add(new ModelDemo("旋转引擎View", R.drawable.circle_rotate, ActivityRxRotateBar.class));

        mDemoList.add(new ModelDemo("点赞控件", R.drawable.circle_heart_circle, ActivityLike.class));

        mDemoList.add(new ModelDemo("蛛网等级View", R.drawable.circle_cobweb, ActivityCobweb.class));
        mDemoList.add(new ModelDemo("横向滑动选择控件", R.drawable.circle_bookshelf, ActivityWheelHorizontal.class));

        mDemoList.add(new ModelDemo("横向左右自动滚动的ImageView", R.drawable.circle_two_way, ActivityAutoImageView.class));
        mDemoList.add(new ModelDemo("登录界面", R.drawable.circle_clound, ActivityLoginAct.class));
        mDemoList.add(new ModelDemo("RxToast的使用", R.drawable.circle_toast, ActivityRxToast.class));

        mDemoList.add(new ModelDemo("RunTextView的使用", R.drawable.circle_wrap_text, ActivityRunTextView.class));
        mDemoList.add(new ModelDemo("银行卡组堆叠控件", R.drawable.circle_credit_card, ActivityCardStack.class));

        mDemoList.add(new ModelDemo("联系人侧边快速导航", R.drawable.circle_phone, ActivityContact.class));


        mDemoList.add(new ModelDemo("TabLayout", R.drawable.circle_pocket, ActivityTabLayout.class));
        mDemoList.add(new ModelDemo("其他界面效果", R.drawable.circle_icecandy, ActivityOtherEffect.class));
    }

}
