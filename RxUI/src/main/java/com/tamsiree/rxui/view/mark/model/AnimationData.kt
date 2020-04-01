package com.tamsiree.rxui.view.mark.model

/**
 * Specialised animation metric wrapper for holding the start proportion trigger
 * and duration for swipe-based canvas animations
 */
internal data class AnimationData(
        val startProportion: Float,
        val duration: Long
)