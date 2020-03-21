package com.tamsiree.rxdemo.tools

import android.util.SparseArray
import android.view.View

object ViewFindTool {
    /**
     * ViewHolder简洁写法,避免适配器中重复定义ViewHolder,减少代码量 用法:
     *
     * <pre>
     * if (convertView == null)
     * {
     * convertView = View.inflate(context, R.layout.ad_demo, null);
     * }
     * TextView tv_demo = ViewHolderUtils.get(convertView, R.id.tv_demo);
     * ImageView iv_demo = ViewHolderUtils.get(convertView, R.id.iv_demo);
    </pre> *
     */
    fun <T : View?> hold(view: View, id: Int): T? {
        val viewHolder = view.tag as SparseArray<View?>
        var childView = viewHolder[id]
        if (childView == null) {
            childView = view.findViewById(id)
            viewHolder.put(id, childView)
        }
        return childView as T?
    }

    /**
     * 替代findviewById方法
     */
    @JvmStatic
    fun <T : View?> find(view: View, id: Int): T {
        return view.findViewById<View>(id) as T
    }
}