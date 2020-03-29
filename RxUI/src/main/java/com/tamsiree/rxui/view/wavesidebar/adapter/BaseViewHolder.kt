package com.tamsiree.rxui.view.wavesidebar.adapter

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
    /**
     * Views indexed with their IDs
     */
    private val mViews: SparseArray<View?> = SparseArray()
    fun <V : View?> findViewById(viewId: Int): V? {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as V?
    }

}