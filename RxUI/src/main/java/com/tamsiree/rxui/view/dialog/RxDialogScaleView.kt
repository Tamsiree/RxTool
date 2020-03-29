package com.tamsiree.rxui.view.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.scaleimage.ImageSource
import com.tamsiree.rxui.view.scaleimage.RxScaleImageView

/**
 * @author tamsiree
 * @date 2016/7/19
 * 查看图片并支持手势缩放
 */
class RxDialogScaleView : RxDialog {
    lateinit var rxScaleImageView: RxScaleImageView
        private set
    var filePath: String? = null
        private set
    var fileUri: Uri? = null
        private set
    var fileAssetName: String? = null
        private set
    var fileBitmap: Bitmap? = null
        private set
    var resId = 0
        private set
    private var maxScale = 100

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Activity?) : super(context!!) {
        initView()
    }

    constructor(context: Context?, filePath: String?, isAssets: Boolean) : super(context!!) {
        initView()
        setImage(filePath, isAssets)
    }

    constructor(context: Context?, uri: Uri?) : super(context!!) {
        initView()
        setImage(uri)
    }

    constructor(context: Context?, resId: Int, isResId: Boolean) : super(context!!) {
        initView()
        if (isResId) {
            setImage(resId)
        }
    }

    constructor(context: Context?, bitmap: Bitmap?) : super(context!!) {
        initView()
        setImage(bitmap)
    }

    constructor(context: Context?, themeResId: Int) : super(context!!, themeResId) {
        initView()
    }

    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context!!, cancelable, cancelListener) {
        initView()
    }

    constructor(context: Context?, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView()
    }

    fun setImage(filePath: String?, isAssets: Boolean) {
        if (isAssets) {
            fileAssetName = fileAssetName
            rxScaleImageView.setImage(ImageSource.asset(filePath))
        } else {
            this.filePath = filePath
            rxScaleImageView.setImage(ImageSource.uri(filePath))
        }
    }

    fun setImage(uri: Uri?) {
        fileUri = uri
        rxScaleImageView.setImage(ImageSource.uri(uri))
    }

    fun setImage(resId: Int) {
        this.resId = resId
        rxScaleImageView.setImage(ImageSource.newImageResource(resId))
    }

    fun setImage(bitmap: Bitmap?) {
        fileBitmap = bitmap
        rxScaleImageView.setImage(ImageSource.bitmap(fileBitmap))
    }

    private fun initView() {
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_scaleview, null)
        rxScaleImageView = dialogView.findViewById(R.id.rx_scale_view)
        rxScaleImageView.maxScale = maxScale.toFloat()
        val ivClose = dialogView.findViewById<ImageView>(R.id.iv_close)
        ivClose.setOnClickListener { cancel() }
        setFullScreen()
        setContentView(dialogView)
    }

    fun getMaxScale(): Int {
        return maxScale
    }

    fun setMaxScale(maxScale: Int) {
        this.maxScale = maxScale
        initView()
    }

}