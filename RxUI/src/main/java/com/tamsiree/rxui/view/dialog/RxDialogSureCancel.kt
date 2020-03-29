package com.tamsiree.rxui.view.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2016/7/19
 * 确认 取消 Dialog
 */
open class RxDialogSureCancel : RxDialog {
    lateinit var logoView: ImageView
        private set
    lateinit var contentView: TextView
        private set
    lateinit var sureView: TextView
        private set
    lateinit var cancelView: TextView
        private set
    lateinit var titleView: TextView
        private set
    private var title: String = ""
    private var logoIcon = -1

    constructor(context: Context?, themeResId: Int) : super(context!!, themeResId) {
        initView()
    }

    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context!!, cancelable, cancelListener) {
        initView()
    }

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Activity?) : super(context!!) {
        initView()
    }

    constructor(context: Context?, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView()
    }

    fun setContent(content: String?) {
        contentView.text = content
    }

    fun setSure(strSure: String?) {
        sureView.text = strSure
    }

    fun setCancel(strCancel: String?) {
        cancelView.text = strCancel
    }

    fun setSureListener(sureListener: View.OnClickListener?) {
        sureView.setOnClickListener(sureListener)
    }

    fun setCancelListener(cancelListener: View.OnClickListener?) {
        cancelView.setOnClickListener(cancelListener)
    }

    private fun initView() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sure_false, null)
        logoView = dialogView.findViewById(R.id.iv_logo)
        sureView = dialogView.findViewById(R.id.tv_sure)
        cancelView = dialogView.findViewById(R.id.tv_cancel)
        contentView = dialogView.findViewById(R.id.tv_content)
        contentView.setTextIsSelectable(true)
        titleView = dialogView.findViewById(R.id.tv_title)
        if (isNullString(title)) {
            titleView.visibility = View.GONE
        }
        if (logoIcon == -1) {
            logoView.visibility = View.GONE
        }
        setContentView(dialogView)
    }

    fun setLogo(LOGOIcon: Int) {
        logoIcon = LOGOIcon
        if (logoIcon == -1) {
            logoView.visibility = View.GONE
            return
        }
        logoView.visibility = View.VISIBLE
        logoView.setImageResource(logoIcon)
    }

    fun setTitle(titleStr: String) {
        title = titleStr
        if (isNullString(title)) {
            titleView.visibility = View.GONE
            return
        }
        titleView.visibility = View.VISIBLE
        titleView.text = title
    }
}