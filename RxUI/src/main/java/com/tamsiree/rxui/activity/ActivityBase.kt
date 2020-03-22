package com.tamsiree.rxui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.tamsiree.rxkit.RxActivityTool

/**
 * @author tamsiree
 */
abstract class ActivityBase : FragmentActivity() {

    lateinit var mContext: ActivityBase

    //获取TAG的activity名称
    protected val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        RxActivityTool.addActivity(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initView()
        initData()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initView()
        initData()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        initView()
        initData()
    }

    protected abstract fun initView()
    protected abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        RxActivityTool.removeActivity(this)
    }

}