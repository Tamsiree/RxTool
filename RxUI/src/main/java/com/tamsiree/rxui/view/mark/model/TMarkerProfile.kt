package com.tamsiree.rxui.view.mark.model

/**
 * @ClassName TMarkerProfile
 * @Description TODO
 * @Author tamsiree
 * @Date 20-4-1 上午10:20
 * @Version 1.0
 */
object TMarkerProfile {

    // Swipe threshold for triggering the swipe callback and
// animation on individual ViewHolders
    const val SWIPE_THRESHOLD_PROPORTION = 0.325F

    // Animation threshold whereby for proportions smaller than
// this no background animation will be shown, else when between
// [SWIPE_THRESHOLD_PROPORTION] and [ANIMATION_THRESHOLD_PROPORTION],
// an interactive circular reveal animation will be visible
    const val ANIMATION_THRESHOLD_PROPORTION = 0.25F

    // The offset to [ANIMATION_THRESHOLD_PROPORTION] where
// the icon starts its "lift" animation
    const val ICON_LIFT_ANIMATION_OFFSET_PROPORTION = 0.1F

    // How far into the interactive animation the color change
// should occur
    const val ICON_CHANGE_COLOR_OFFSET_PROPORTION = 0.02F

    // The maximum scale of the icon when lifting
// This value must be greater than or equal to 1
    const val MAX_LIFT_SCALE = 1.5F

    // Reveal animation duration
    const val REVEAL_DURATION = 350L
    const val FADE_DURATION = 200L
    const val BOUNCE_DURATION = 250L
    const val REVEAL_COLOR_CHANGE_DURATION = 100L

    // Icon specs
    const val ICON_MARGIN_DP = 32F
    const val ICON_DEFAULT_SIZE_DP = 24F

    const val TIME_PER_FRAME_MS = 1000L / 60L


}