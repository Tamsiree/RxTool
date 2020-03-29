package com.tamsiree.rxui.view.popupwindows

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import com.tamsiree.rxkit.RxDeviceTool.getScreenHeight
import com.tamsiree.rxkit.RxDeviceTool.getScreenWidth
import com.tamsiree.rxkit.model.ActionItem
import com.tamsiree.rxui.R
import java.util.*

/**
 *
 * @author tamsiree
 * @date 2016/8/4
 */
class RxPopupImply @JvmOverloads constructor(private val mContext: Context?, width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT, str: String? = "一小时后点这里\n有惊喜哦~") : PopupWindow() {
    // 列表弹窗的间隔
    protected val LIST_PADDING = 10

    // 坐标的位置（x、y）
    private val mLocation = IntArray(2)

    // 实例化一个矩形
    private val mRect = Rect()

    // 屏幕的宽度和高度
    private val mScreenWidth: Int
    private val mScreenHeight: Int

    // 判断是否需要添加或更新列表子类项
    private val mIsDirty = false

    // 位置不在中心
    private val popupGravity = Gravity.NO_GRAVITY

    // 定义列表对象
    private val mListView: ListView? = null

    // 定义弹窗子类项列表
    private val mActionItems = ArrayList<ActionItem>()
    private var tv_imply: TextView? = null

    constructor(context: Context?, str: String?) : this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, str) {
        // 设置布局的参数
    }

    /**
     * 初始化弹窗列表
     */
    private fun initUI(str: String?) {
        tv_imply = contentView.findViewById(R.id.tv_imply)
        tv_imply?.text = str
        /*  mListView = (ListView) getContentView().findViewById(R.id.title_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                // 点击子类项后，弹窗消失
                dismiss();
            }
        });*/
    }

    /**
     * 显示弹窗列表界面
     */
    fun show(view: View) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation)
        // 设置矩形的大小
        mRect[mLocation[0], mLocation[1], mLocation[0] + view.width] = mLocation[1] + view.height
        // 显示弹窗的位置
        // showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth() / 2), mRect.bottom + VonUtils.dip2px(mContext, 7.5f));
        showAsDropDown(view)
    }

    init {

        // 设置可以获得焦点
        isFocusable = true
        // 设置弹窗内可点击
        isTouchable = true
        // 设置弹窗外可点击
        isOutsideTouchable = true

        // 获得屏幕的宽度和高度
        mScreenWidth = getScreenWidth(mContext!!)
        mScreenHeight = getScreenHeight(mContext)

        // 设置弹窗的宽度和高度
        setWidth(width)
        setHeight(height)
        setBackgroundDrawable(BitmapDrawable())

        // 设置弹窗的布局界面
        contentView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_imply, null)
        initUI(str)
    }
}