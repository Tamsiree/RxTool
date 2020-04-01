package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.activity.*
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain.ContentListener
import com.tamsiree.rxdemo.model.ModelDemo
import com.tamsiree.rxfeature.activity.ActivityCodeTool
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxkit.RxRecyclerViewDividerTool
import com.tamsiree.rxui.activity.ActivityWebView
import com.tamsiree.rxui.fragment.FragmentLazy
import kotlinx.android.synthetic.main.fragment_demo.*
import java.util.*

class FragmentDemo : FragmentLazy, OnRefreshListener {
    var mDemoList: MutableList<ModelDemo>? = ArrayList()

    private var mAdapter: AdapterRecyclerViewMain? = null
    private var demo_type = 0
    private val mColumnCount = 2

    constructor()
    private constructor(demo_type: Int) {
        this.demo_type = demo_type
    }

    override fun initViews(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_demo, viewGroup, false)
        return view
    }

    override fun initData() {
        init()
        loadData()
    }

    private fun init() {
        initRecyclerView()
        tvHint.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        //加载列表
        swipeLayoutDemo.setOnRefreshListener(this)
        swipeLayoutDemo.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.blue_baby))
        if (mColumnCount <= 1) {
            recyclerViewDemo.layoutManager = LinearLayoutManager(mContext)
        } else {
            recyclerViewDemo.layoutManager = GridLayoutManager(mContext, mColumnCount)
        }
        recyclerViewDemo.addItemDecoration(RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)))
        mAdapter = AdapterRecyclerViewMain(mDemoList, object : ContentListener {
            override fun setListener(position: Int) {
                RxActivityTool.skipActivity(mContext, mDemoList!![position].activity, null, true)
            }
        })
        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        mAdapter!!.isFirstOnly(true)
        mAdapter!!.bindToRecyclerView(recyclerViewDemo)
    }

    override fun onRefresh() {
        loadData()
    }

    private fun loadData() {
        swipeLayoutDemo.isRefreshing = true
        if (demo_type == 0) {
            functionData
        } else if (demo_type == 1) {
            uIData
        }
        if (mDemoList == null || mDemoList!!.size <= 0) {
            mAdapter!!.setEmptyView(R.layout.load_data_empty)
        }
        swipeLayoutDemo.isRefreshing = false
    }

    /**
     * 获得 功能性 Demo 数据
     */
    private val functionData: Unit
        get() {
            mDemoList!!.clear()
            mDemoList!!.add(ModelDemo("RxPhotoTool操作UZrop裁剪图片", R.drawable.circle_elves_ball, ActivityRxPhoto::class.java))
            mDemoList!!.add(ModelDemo("动态生成码", R.drawable.circle_qr_code, ActivityCreateQRCode::class.java))
            mDemoList!!.add(ModelDemo("二维码与条形码的扫描与生成", R.drawable.circle_dynamic_generation_code, ActivityCodeTool::class.java))
            mDemoList!!.add(ModelDemo("图片添加经纬度信息", R.drawable.circle_picture_location, ActivityRxExifTool::class.java))
            mDemoList!!.add(ModelDemo("WebView的封装可播放视频", R.drawable.circle_webpage, ActivityWebView::class.java))
            mDemoList!!.add(ModelDemo("常用的Dialog展示", R.drawable.circle_dialog, ActivityDialog::class.java))
            mDemoList!!.add(ModelDemo("图片的缩放艺术", R.drawable.circle_scale_icon, ActivityRxScaleImageView::class.java))
            mDemoList!!.add(ModelDemo("添加购物车控件", R.drawable.circle_shop_cart, ActivityShoppingView::class.java))
            mDemoList!!.add(ModelDemo("网速控件", R.drawable.circle_net_speed, ActivityNetSpeed::class.java))
            mDemoList!!.add(ModelDemo("验证码", R.drawable.circle_captcha, ActivityRxCaptcha::class.java))
            mDemoList!!.add(ModelDemo("SlidingDrawerSingle使用", R.drawable.circle_up_down, ActivitySlidingDrawerSingle::class.java))
            mDemoList!!.add(ModelDemo("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar::class.java))
            mDemoList!!.add(ModelDemo("视频时间轴", R.drawable.circle_ebuddy, ActivityTVideoTimeline::class.java))
            mDemoList!!.add(ModelDemo("PopupView的使用", R.drawable.circle_bullet, ActivityPopupView::class.java))
            mDemoList!!.add(ModelDemo("选座控件", R.drawable.circle_seat, ActivitySeat::class.java))
            mDemoList!!.add(ModelDemo("GPS原生定位", R.drawable.circle_gps_icon, ActivityLocation::class.java))
            mDemoList!!.add(ModelDemo("震动的艺术", R.drawable.circle_vibrate, ActivityVibrate::class.java))
            mDemoList!!.add(ModelDemo("压缩与加密的艺术", R.drawable.circle_zip, ActivityZipEncrypt::class.java))
            mDemoList!!.add(ModelDemo("PULL解析XML", R.drawable.circle_swap_vert, ActivityXmlParse::class.java))
            mDemoList!!.add(ModelDemo("支付宝支付Demo", R.drawable.circle_alipay, ActivityAliPay::class.java))
            mDemoList!!.add(ModelDemo("Hold住崩溃界面", R.drawable.crash_logo, ActivityOnCrash::class.java))
            mDemoList!!.add(ModelDemo("app检测更新与安装", R.mipmap.ic_launcher, ActivitySplash::class.java))
        }

    /**
     * 获得 UI Demo 数据
     */
    private val uIData: Unit
        get() {
            mDemoList!!.clear()
            mDemoList!!.add(ModelDemo("RxDataTool操作Demo", R.drawable.circle_data, ActivityRxDataTool::class.java))
            mDemoList!!.add(ModelDemo("设备信息", R.drawable.circle_device_info, ActivityDeviceInfo::class.java))
            mDemoList!!.add(ModelDemo("RxTextTool操作Demo", R.drawable.circle_text, ActivityTextTool::class.java))
            mDemoList!!.add(ModelDemo("进度条的艺术", R.drawable.circle_bar, ActivityProgressBar::class.java))
            mDemoList!!.add(ModelDemo("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView::class.java))
            mDemoList!!.add(ModelDemo("橙汁加载", R.drawable.circle_origin, ActivityOrangeJuice::class.java))
            mDemoList!!.add(ModelDemo("加载的艺术", R.drawable.circle_loading_icon, ActivityLoading::class.java))
            mDemoList!!.add(ModelDemo("旋转引擎View", R.drawable.circle_rotate, ActivityRxRotateBar::class.java))
            mDemoList!!.add(ModelDemo("点赞控件", R.drawable.circle_heart_circle, ActivityLike::class.java))
            mDemoList!!.add(ModelDemo("蛛网等级View", R.drawable.circle_cobweb, ActivityCobweb::class.java))
            mDemoList!!.add(ModelDemo("横向滑动选择控件", R.drawable.circle_bookshelf, ActivityWheelHorizontal::class.java))
            mDemoList!!.add(ModelDemo("横向左右自动滚动的ImageView", R.drawable.circle_two_way, ActivityAutoImageView::class.java))
            mDemoList!!.add(ModelDemo("登录界面", R.drawable.circle_clound, ActivityLoginAct::class.java))
            mDemoList!!.add(ModelDemo("RxToast的使用", R.drawable.circle_toast, ActivityRxToast::class.java))
            mDemoList!!.add(ModelDemo("RunTextView的使用", R.drawable.circle_wrap_text, ActivityRunTextView::class.java))
            mDemoList!!.add(ModelDemo("银行卡组堆叠控件", R.drawable.circle_credit_card, ActivityCardStack::class.java))
            mDemoList!!.add(ModelDemo("联系人侧边快速导航", R.drawable.circle_phone, ActivityContact::class.java))
            mDemoList!!.add(ModelDemo("TabLayout", R.drawable.circle_pocket, ActivityTabLayout::class.java))
            mDemoList!!.add(ModelDemo("卡片画廊效果", R.drawable.circle_outlook, ActivityTCardGallery::class.java))
            mDemoList!!.add(ModelDemo("步骤指示器", R.drawable.circle_indicator, ActivityTStepperIndicator::class.java))
            mDemoList!!.add(ModelDemo("加载中视图", R.drawable.circle_indicator, ActivityTLoadingView::class.java))
            mDemoList!!.add(ModelDemo("左滑标记", R.drawable.circle_indicator, ActivityTMarker::class.java))
            mDemoList!!.add(ModelDemo("其他界面效果", R.drawable.circle_icecandy, ActivityOtherEffect::class.java))
        }

    companion object {
        fun newInstance(demo_type: Int): FragmentDemo {
            return FragmentDemo(demo_type)
        }
    }
}