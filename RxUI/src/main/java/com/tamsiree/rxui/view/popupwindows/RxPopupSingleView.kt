package com.tamsiree.rxui.view.popupwindows

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tamsiree.rxkit.RxDeviceTool.getScreenHeight
import com.tamsiree.rxkit.RxDeviceTool.getScreenWidth
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.model.ActionItem
import com.tamsiree.rxui.R
import java.util.*

/***
 * @author tamsiree
 * 功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
class RxPopupSingleView : PopupWindow {
    // 列表弹窗的间隔
    protected val LIST_PADDING = 10

    // 坐标的位置（x、y）
    private val mLocation = IntArray(2)
    private var mContext: Context

    // 实例化一个矩形
    private val mRect = Rect()

    // 屏幕的宽度和高度
    private var mScreenWidth: Int
    private var mScreenHeight: Int

    // 判断是否需要添加或更新列表子类项
    private var mIsDirty = false

    // 位置不在中心
    private val popupGravity = Gravity.NO_GRAVITY

    // 弹窗子类项选中时的监听
    private var mItemOnClickListener: OnItemOnClickListener? = null

    // 定义列表对象
    private var mListView: ListView? = null

    // 定义弹窗子类项列表
    private val mActionItems = ArrayList<ActionItem>()
    private var colorItemText = 0

    @JvmOverloads
    constructor(context: Context, width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) {
        mContext = context

        // 设置可以获得焦点
        isFocusable = true
        // 设置弹窗内可点击
        isTouchable = true
        // 设置弹窗外可点击
        isOutsideTouchable = true

        // 获得屏幕的宽度和高度
        mScreenWidth = getScreenWidth(mContext)
        mScreenHeight = getScreenHeight(mContext)

        // 设置弹窗的宽度和高度
        setWidth(width)
        setHeight(height)
        setBackgroundDrawable(BitmapDrawable())

        // 设置弹窗的布局界面
        contentView = LayoutInflater.from(mContext).inflate(
                R.layout.popupwindow_layout, null)
        initUI()
    }

    constructor(context: Context, width: Int, height: Int, layout: Int) {
        mContext = context

        // 设置可以获得焦点
        isFocusable = true
        // 设置弹窗内可点击
        isTouchable = true
        // 设置弹窗外可点击
        isOutsideTouchable = true

        // 获得屏幕的宽度和高度
        mScreenWidth = getScreenWidth(mContext)
        mScreenHeight = getScreenHeight(mContext)

        // 设置弹窗的宽度和高度
        setWidth(width)
        setHeight(height)
        setBackgroundDrawable(BitmapDrawable())

        // 设置弹窗的布局界面
        contentView = LayoutInflater.from(mContext).inflate(
                layout, null)
        initUI()
    }

    /**
     * 初始化弹窗列表
     */
    private fun initUI() {
        mListView = contentView.findViewById(R.id.title_list)
        mListView?.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, index, arg3 -> // 点击子类项后，弹窗消失
            dismiss()
            if (mItemOnClickListener != null) {
                mItemOnClickListener!!.onItemClick(mActionItems[index],
                        index)
            }
        }
    }

    /**
     * 显示弹窗列表界面
     */
    fun show(view: View) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation)

        // 设置矩形的大小
        mRect[mLocation[0], mLocation[1], mLocation[0] + view.width] = mLocation[1] + view.height

        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions()
        }

        // 显示弹窗的位置
        showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - width / 2, mRect.bottom + dp2px(7.5f))
    }

    /**
     * 显示弹窗列表界面
     */
    fun show(view: View, dex: Int) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation)

        // 设置矩形的大小
        mRect[mLocation[0], mLocation[1], mLocation[0] + view.width] = mLocation[1] + view.height

        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions()
        }

        // 显示弹窗的位置
        showAtLocation(view, popupGravity, mLocation[0], mRect.bottom + dex)
    }

    fun setColorItemText(colorItemText: Int) {
        this.colorItemText = colorItemText
    }

    /**
     * 设置弹窗列表子项
     */
    private fun populateActions() {
        mIsDirty = false

        // 设置列表的适配器
        mListView!!.adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
                var convertView = convertView
                var tv_itpop: TextView? = null
                var iv_itpop: ImageView? = null
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_popup, null)
                }
                tv_itpop = convertView.findViewById(R.id.tv_itpop)
                iv_itpop = convertView.findViewById(R.id.iv_itpop)
                if (colorItemText == 0) {
                    colorItemText = mContext.resources.getColor(android.R.color.white)
                }
                tv_itpop.setTextColor(colorItemText)
                tv_itpop.textSize = 14f
                // 设置文本居中
                tv_itpop.gravity = Gravity.CENTER
                // 设置文本域的范围
                tv_itpop.setPadding(0, 10, 0, 10)
                // 设置文本在一行内显示（不换行）
                tv_itpop.isSingleLine = true
                val item = mActionItems[position]

                // 设置文本文字
                tv_itpop.text = item.mTitle
                if (item.mResourcesId == 0) {
                    iv_itpop.visibility = View.GONE
                } else {
                    iv_itpop.visibility = View.VISIBLE
                    iv_itpop.setImageResource(item.mResourcesId)
                }
                return convertView
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getItem(position: Int): Any {
                return mActionItems[position]
            }

            override fun getCount(): Int {
                return mActionItems.size
            }
        }
    }

    /**
     * 添加子类项
     */
    fun addAction(action: ActionItem?) {
        if (action != null) {
            mActionItems.add(action)
            mIsDirty = true
        }
    }

    /**
     * 清除子类项
     */
    fun cleanAction() {
        if (mActionItems.isEmpty()) {
            mActionItems.clear()
            mIsDirty = true
        }
    }

    /**
     * 根据位置得到子类项
     */
    fun getAction(position: Int): ActionItem? {
        return if (position < 0 || position > mActionItems.size) {
            null
        } else mActionItems[position]
    }

    /**
     * 设置监听事件
     */
    fun setItemOnClickListener(
            onItemOnClickListener: OnItemOnClickListener?) {
        mItemOnClickListener = onItemOnClickListener
    }

    /**
     * @author yangyu 功能描述：弹窗子类项按钮监听事件
     */
    interface OnItemOnClickListener {
        fun onItemClick(item: ActionItem?, position: Int)
    }
}