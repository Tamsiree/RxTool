package com.tamsiree.rxui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tamsiree.rxkit.RxActivityTool

/**
 * @author tamsiree
 */
open class ActivityBase : FragmentActivity() {
    @JvmField
    var mContext: ActivityBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        RxActivityTool.addActivity(this)
    }

}