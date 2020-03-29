package com.tamsiree.rxui.view.dialog

import android.app.Activity
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tamsiree.rxkit.RxPhotoTool.openCameraImage
import com.tamsiree.rxkit.RxPhotoTool.openLocalImage
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2017/3/20
 * 封装了从相册/相机 获取 图片 的Dialog.
 */
class RxDialogChooseImage : RxDialog {
    var layoutType = LayoutType.TITLE
        private set
    lateinit var fromCameraView: TextView
        private set
    lateinit var fromFileView: TextView
        private set
    private lateinit var btnCancel: Button
    lateinit var tvOriginalImage: TextView
        private set

    var uriOriginalImage: Uri? = null

    constructor(context: Activity) : super(context) {
        initView(context)
    }

    constructor(fragment: Fragment) : super(fragment.context!!) {
        initView(fragment)
    }

    constructor(context: Activity, uri: Uri?) : super(context) {
        uriOriginalImage = uri
        initView(context)
    }

    constructor(fragment: Fragment, uri: Uri?) : super(fragment.context!!) {
        uriOriginalImage = uri
        initView(fragment)
    }

    constructor(context: Activity, themeResId: Int) : super(context, themeResId) {
        initView(context)
    }

    constructor(fragment: Fragment, themeResId: Int) : super(fragment.context!!, themeResId) {
        initView(fragment)
    }

    constructor(context: Activity, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView(context)
    }

    constructor(fragment: Fragment, alpha: Float, gravity: Int) : super(fragment.context, alpha, gravity) {
        initView(fragment)
    }

    constructor(fragment: Fragment, layoutType: LayoutType) : super(fragment.context!!) {
        this.layoutType = layoutType
        initView(fragment)
    }

    constructor(context: Activity, layoutType: LayoutType) : super(context) {
        this.layoutType = layoutType
        initView(context)
    }

    constructor(context: Activity, themeResId: Int, layoutType: LayoutType) : super(context, themeResId) {
        this.layoutType = layoutType
        initView(context)
    }

    constructor(fragment: Fragment, themeResId: Int, layoutType: LayoutType) : super(fragment.context!!, themeResId) {
        this.layoutType = layoutType
        initView(fragment)
    }

    constructor(context: Activity, alpha: Float, gravity: Int, layoutType: LayoutType) : super(context, alpha, gravity) {
        this.layoutType = layoutType
        initView(context)
    }

    constructor(fragment: Fragment, alpha: Float, gravity: Int, layoutType: LayoutType) : super(fragment.context, alpha, gravity) {
        this.layoutType = layoutType
        initView(fragment)
    }

    private fun initView(activity: Activity) {
        init()
        setClickEvent(activity)
    }

    private fun initView(fragment: Fragment) {
        init()
        setClickEvent(fragment)
    }

    private fun init() {
        var dialogView: View? = null
        when (layoutType) {
            LayoutType.TITLE -> dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_picker_pictrue, null)
            LayoutType.NO_TITLE -> dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_camero_show, null)
        }
        tvOriginalImage = dialogView.findViewById(R.id.tv_original_image)
        fromCameraView = dialogView.findViewById(R.id.tv_camera)
        fromFileView = dialogView.findViewById(R.id.tv_file)
        btnCancel = dialogView.findViewById(R.id.btnCancel)
        tvOriginalImage.setOnClickListener(View.OnClickListener {
            if (uriOriginalImage != null) {
                val rxDialogScaleView = RxDialogScaleView(mContext, uriOriginalImage)
                rxDialogScaleView.show()
            }
        })
        btnCancel.setOnClickListener(View.OnClickListener { cancel() })
        setContentView(dialogView!!)
        layoutParams!!.gravity = Gravity.BOTTOM
        layoutParams!!.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private fun setClickEvent(activity: Activity) {
        fromCameraView.setOnClickListener {
            openCameraImage(activity)
            cancel()
        }
        fromFileView.setOnClickListener {
            openLocalImage(activity)
            cancel()
        }
    }

    private fun setClickEvent(fragment: Fragment) {
        fromCameraView.setOnClickListener { //请求Camera权限
            openCameraImage(fragment)
            cancel()
        }
        fromFileView.setOnClickListener {
            openLocalImage(fragment)
            cancel()
        }
    }

    val cancelView: TextView?
        get() = btnCancel

    enum class LayoutType {
        TITLE, NO_TITLE
    }
}