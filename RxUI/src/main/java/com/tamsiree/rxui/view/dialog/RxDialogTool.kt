package com.tamsiree.rxui.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tamsiree.rxkit.RxIntentTool.getAppDetailsSettingsIntent
import com.tamsiree.rxkit.RxVibrateTool.vibrateOnce
import com.tamsiree.rxkit.view.RxToast.info
import com.tamsiree.rxui.R
import com.tamsiree.rxui.adapter.AdapterCardViewModelPicture
import com.tamsiree.rxui.model.ModelPicture
import com.tamsiree.rxui.view.cardstack.RxCardStackView
import com.tamsiree.rxui.view.cardstack.RxCardStackView.ItemExpendListener

/**
 * @author tamsiree
 * @date 2018/4/11 10:40:00
 */
object RxDialogTool {
    /**
     * 加载进度 提示弹窗
     */
    @SuppressLint("StaticFieldLeak")
    private var mDialogLoading: RxDialogLoading? = null

    /**
     * 耗时操作 提示弹窗
     */
    @SuppressLint("StaticFieldLeak")
    private var mDialogLoad: RxDialogLoading? = null

    /**
     * 网络请求加载框
     */
    fun loadingHttp(context: Context?) {
        if (mDialogLoading != null) {
            mDialogLoading!!.cancel()
        }
        mDialogLoading = RxDialogLoading(context!!)
        mDialogLoading!!.setCanceledOnTouchOutside(false)
        mDialogLoading!!.setCancelable(false)
        mDialogLoading!!.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen))
        if (!mDialogLoading!!.isShowing) {
            mDialogLoading!!.show()
        }
    }

    /**
     * 网络请求加载框
     * @param context
     * @param hint 提示语
     */
    fun loadingHttp(context: Context?, hint: String?) {
        if (mDialogLoading != null) {
            mDialogLoading!!.cancel()
        }
        mDialogLoading = RxDialogLoading(context!!)
        mDialogLoading!!.setCanceledOnTouchOutside(false)
        mDialogLoading!!.setCancelable(false)
        mDialogLoading!!.setLoadingText(hint)
        mDialogLoading!!.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen))
        if (!mDialogLoading!!.isShowing) {
            mDialogLoading!!.show()
        }
    }

    /**
     * 网络请求加载框 取消
     */
    fun loadingHttpCancel() {
        if (mDialogLoading != null) {
            mDialogLoading!!.cancel()
        }
    }

    /**
     * 网络请求加载框 取消
     */
    fun loadingHttpCancel(reminder: String?) {
        if (mDialogLoading != null) {
            mDialogLoading!!.cancel(reminder)
        }
    }

    /**
     * 耗时操作加载框
     */
    fun loading(context: Context?) {
        if (mDialogLoad != null) {
            mDialogLoad!!.cancel()
        }
        mDialogLoad = RxDialogLoading(context!!)
        mDialogLoad!!.setCanceledOnTouchOutside(false)
        mDialogLoad!!.setCancelable(false)
        mDialogLoad!!.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen))
        mDialogLoad!!.setLoadingText("正在进行操作..")
        if (!mDialogLoad!!.isShowing) {
            mDialogLoad!!.show()
        }
    }

    /**
     * 耗时操作加载框
     * @param context
     * @param hint 提示语
     */
    fun loading(context: Context?, hint: String?) {
        if (mDialogLoad != null) {
            mDialogLoad!!.cancel()
        }
        mDialogLoad = RxDialogLoading(context!!)
        mDialogLoad!!.setCanceledOnTouchOutside(false)
        mDialogLoad!!.setCancelable(false)
        mDialogLoad!!.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen))
        mDialogLoad!!.setLoadingText(hint)
        if (!mDialogLoad!!.isShowing) {
            mDialogLoad!!.show()
        }
    }

    /**
     * 耗时操作加载框 取消
     */
    fun loadingCancel() {
        if (mDialogLoad == null) {
        } else {
            mDialogLoad!!.cancel()
        }
    }

    /**
     * 跳转系统设置APP权限界面
     *
     * @param mContext
     * @param str
     */
    fun initDialogSurePermission(mContext: Context, str: String?) {
        val rxDialogSure = RxDialogSure(mContext)
        rxDialogSure.logoView.visibility = View.GONE
        rxDialogSure.titleView.visibility = View.GONE
        rxDialogSure.setContent(str)
        rxDialogSure.contentView.textSize = 20f
        rxDialogSure.contentView.setTextColor(ContextCompat.getColor(mContext, R.color.green_388e3c))
        rxDialogSure.contentView.gravity = Gravity.CENTER
        rxDialogSure.setCanceledOnTouchOutside(false)
        rxDialogSure.setSureListener(View.OnClickListener {
            rxDialogSure.cancel()
            mContext.startActivity(getAppDetailsSettingsIntent(mContext))
        })
        rxDialogSure.show()
    }

    /**
     * 显示大图
     *
     * @param context
     * @param uri     图片的Uri
     */
    fun showBigImageView(context: Context?, uri: Uri?) {
        val rxDialog = RxDialog(context!!)
        val view = LayoutInflater.from(context).inflate(R.layout.image, null)
        view.setOnClickListener { rxDialog.cancel() }
        val imageView = view.findViewById<ImageView>(R.id.page_item)
        imageView.setImageURI(uri)
        rxDialog.setContentView(view)
        rxDialog.show()
        rxDialog.setFullScreen()
    }

    /**
     * 提示框
     *
     * @param hint 提示的内容
     */
    fun initDialogExport(mContext: Context?, hint: String?) {
        vibrateOnce(mContext!!, 150)
        val mDialogExport = RxDialogSureCancel(mContext, R.style.PushUpInDialogThem)
        mDialogExport.titleView.background = null
        mDialogExport.titleView.text = "数据导出目录"
        mDialogExport.setContent(hint)
        mDialogExport.contentView.textSize = 13f
        mDialogExport.sureView.visibility = View.GONE
        mDialogExport.setCancel("确定")
        mDialogExport.setCancelListener(View.OnClickListener {
            vibrateOnce(mContext, 150)
            mDialogExport.cancel()
        })
        mDialogExport.setCancelable(false)
        mDialogExport.show()
    }

    fun initDialogShowPicture(mContext: Context?, modelList: List<ModelPicture>) {
        val mDialogShowPicture = RxDialog(mContext!!)
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_picture, null)
        val mStackView: RxCardStackView = dialogView.findViewById(R.id.stackview_main)
        val mButtonContainer = dialogView.findViewById<LinearLayout>(R.id.button_container)
        val btnPre = dialogView.findViewById<Button>(R.id.btn_Pre)
        val btnNext = dialogView.findViewById<Button>(R.id.btn_next)
        mStackView.itemExpendListener = object : ItemExpendListener {
            override fun onItemExpend(expend: Boolean) {
                mButtonContainer.visibility = if (expend) View.VISIBLE else View.INVISIBLE
            }
        }
        val testStackAdapter = AdapterCardViewModelPicture(mContext)
        mStackView.setAdapter(testStackAdapter)
        testStackAdapter.updateData(modelList)
        mDialogShowPicture.setContentView(dialogView)
        mDialogShowPicture.setFullScreen()
        if (modelList.size > 1) {
            btnPre.setOnClickListener {
                if (mStackView.selectPosition == 0) {
                    info("当前为第一张")
                } else {
                    mStackView.pre()
                }
            }
            btnNext.setOnClickListener {
                if (mStackView.selectPosition == modelList.size - 1) {
                    info("当前为最后一张")
                } else {
                    mStackView.next()
                }
            }
            btnPre.text = "上一张"
            btnNext.visibility = View.VISIBLE
            btnPre.visibility = View.VISIBLE
        } else {
            btnPre.text = "确定"
            btnPre.visibility = View.VISIBLE
            btnPre.setOnClickListener { mDialogShowPicture.cancel() }
            btnNext.visibility = View.GONE
        }
        testStackAdapter.updateData(modelList)
        mDialogShowPicture.show()
    }
}