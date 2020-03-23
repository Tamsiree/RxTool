package com.tamsiree.rxui.model

import com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel

data class ModelTab(override var tabTitle: String, override var tabSelectedIcon: Int, override var tabUnselectedIcon: Int) : TabLayoutModel