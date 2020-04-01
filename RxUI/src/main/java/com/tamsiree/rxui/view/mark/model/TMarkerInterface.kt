package com.tamsiree.rxui.view.mark.model

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tamsiree.rxui.view.mark.TMarker

/**
 * @ClassName TMarkerInterface
 * @Description TODO
 * @Author tamsiree
 * @Date 20-4-1 上午10:21
 * @Version 1.0
 */


typealias ViewHolderCheckCallback = (viewHolder: ViewHolder) -> Boolean
typealias ViewHolderNotifyCallback = (viewHolder: ViewHolder, direction: TMarker.SwipeDirection) -> Unit
