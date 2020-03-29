package com.tamsiree.rxui.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxRegTool.isURL
import com.tamsiree.rxkit.RxTextTool.getBuilder
import com.tamsiree.rxui.R

/**
 * @author tamsiree
 * @date 2016/7/19
 * 确认 弹出框
 */
class RxDialogSure : RxDialog {
    lateinit var logoView: ImageView
        private set
    lateinit var titleView: TextView
        private set
    lateinit var contentView: TextView
        private set
    lateinit var sureView: TextView
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

    constructor(context: Context?, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView()
    }

    fun setSureListener(listener: View.OnClickListener?) {
        sureView.setOnClickListener(listener)
    }

    fun setSure(content: String?) {
        sureView.text = content
    }

    fun setContent(str: String?) {
        if (isURL(str)) {
            // 响应点击事件的话必须设置以下属性
            contentView.movementMethod = LinkMovementMethod.getInstance()
            contentView.text = getBuilder("").setBold().append(str!!).setUrl(str).create() //当内容为网址的时候，内容变为可点击
        } else {
            contentView.text = str
        }
    }

    private fun initView() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sure, null)
        sureView = dialogView.findViewById(R.id.tv_sure)
        titleView = dialogView.findViewById(R.id.tv_title)
        titleView.setTextIsSelectable(true)
        contentView = dialogView.findViewById(R.id.tv_content)
        contentView.movementMethod = ScrollingMovementMethod.getInstance()
        contentView.setTextIsSelectable(true)
        logoView = dialogView.findViewById(R.id.iv_logo)
        if (isNullString(title)) {
            titleView.visibility = View.GONE
        }
        if (logoIcon == -1) {
            logoView.visibility = View.GONE
        }
        setContentView(dialogView)
    }

    fun setLogo(resId: Int) {
        logoIcon = resId
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