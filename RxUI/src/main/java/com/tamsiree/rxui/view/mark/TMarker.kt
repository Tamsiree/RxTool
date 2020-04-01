package com.tamsiree.rxui.view.mark

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.mark.canvas.*
import com.tamsiree.rxui.view.mark.model.*
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.ANIMATION_THRESHOLD_PROPORTION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.BOUNCE_DURATION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.FADE_DURATION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.ICON_DEFAULT_SIZE_DP
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.ICON_LIFT_ANIMATION_OFFSET_PROPORTION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.ICON_MARGIN_DP
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.MAX_LIFT_SCALE
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.REVEAL_COLOR_CHANGE_DURATION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.REVEAL_DURATION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.SWIPE_THRESHOLD_PROPORTION
import com.tamsiree.rxui.view.mark.model.TMarkerProfile.TIME_PER_FRAME_MS
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.math.abs


/**
 * Covert is a library for enabling Material Swipe actions in any old [RecyclerView]
 *
 * For Covert to operate, you must first define the configuration to be used. This tells Covert
 * which colors you're interested in drawing, which icons to use etc. For more on Coverts configuration,
 * see [TMarker.Config].
 *
 * The other crucial piece of Covert is the [isViewHolderActive] callback. This is called by Covert
 * to check if the [RecyclerView.ViewHolder] should display active or inactive animations when swiping
 * occurs. Active and inactive states can be thought of as checked and unchecked respectively.
 *
 * Covert must be attached to the recyclerView using the [TMarker.Builder.attachTo] function. The instance of
 * Covert returned by this function should be passed along to your [RecyclerView.Adapter]. This allows Covert
 * to draw the initial state of any defined [TMarker.CornerFlag] on the [RecyclerView.ViewHolder]s itemView.
 * For information around how to implement this, as well as additional optimisations, check the full documentation
 * in the Github repository.
 */
class TMarker private constructor(
        recyclerView: RecyclerView,
        covertConfig: Config,
        private val isViewHolderActive: ViewHolderCheckCallback,
        private val isViewHolderEnabled: ViewHolderCheckCallback,
        private val onSwipe: ViewHolderNotifyCallback,
        private val pullToRefreshView: SwipeRefreshLayout?
) : ItemTouchHelper.Callback() {

    // Configuration
    private val activeStateDrawables: List<CanvasDrawable>
    private val inactiveStateDrawables: List<CanvasDrawable>
    private val backgroundColor: Int
    private val cornerFlag: CornerFlag
    private val isHapticFeedbackEnabled: Boolean

    // Swipe state variables
    private var currentDx: Float = 0F
    private var listenerNotified: Boolean = false
    private var isReturning: Boolean = false
    private var flagRenderedOnReturn: Boolean = false

    // Ternary variable representing if the viewHolder started out swiping as active or inactive
    private var isViewHolderCurrentlyActive: Boolean? = null

    // Optimisation for not notifying the RecyclerView at more than 60fps
    private val updateSubject = PublishSubject.create<Int>()

    companion object {
        const val SKIP_FULL_BIND_PAYLOAD = "swipe_action_skip_full_bind_payload"

        @JvmStatic
        fun with(config: Config) = Builder(config)
    }

    init {
        ItemTouchHelper(this).attachToRecyclerView(recyclerView)

        recyclerView.itemAnimator = null

        isHapticFeedbackEnabled = covertConfig.isHapticFeedbackEnabled

        activeStateDrawables = buildActiveStateDrawables(recyclerView.context, covertConfig)
        inactiveStateDrawables = buildInactiveStateDrawables(recyclerView.context, covertConfig)

        backgroundColor = ContextCompat.getColor(recyclerView.context, covertConfig.activeBackdropColorRes)
        cornerFlag = covertConfig.cornerFlag

        // Optimization - The following subject ensures that we only ever notify the RecyclerView of item
        // changes at 60fps max
        updateSubject
                .toFlowable(BackpressureStrategy.DROP)
                .debounce(TIME_PER_FRAME_MS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    recyclerView.adapter?.notifyItemChanged(it, SKIP_FULL_BIND_PAYLOAD)
                }
    }

    /**
     * Constructs a list of [CanvasDrawable]s which represent the inactive state, i.e the state which
     * starts as inactive and proceeds to active via a circular reveal animation.
     *
     * @param context Context
     * @param covertConfig Config to construct [CanvasDrawable]s with
     */
    private fun buildInactiveStateDrawables(context: Context, covertConfig: Config): List<CanvasDrawable> {
        val iconInset = ((ICON_DEFAULT_SIZE_DP / 2) + ICON_MARGIN_DP).toPx(context)

        // Background icon lift drawable
        val backgroundIconDrawable = IconLiftCanvasDrawable(
                liftStartProportion = ANIMATION_THRESHOLD_PROPORTION - ICON_LIFT_ANIMATION_OFFSET_PROPORTION,
                bounceAnimationData = AnimationData(
                        startProportion = SWIPE_THRESHOLD_PROPORTION,
                        duration = BOUNCE_DURATION
                ),
                maxIconScaleProportion = MAX_LIFT_SCALE,
                iconInsetPx = iconInset.toInt(),
                iconSizePx = ICON_DEFAULT_SIZE_DP.toPx(context).toInt(),
                iconColor = ContextCompat.getColor(context, covertConfig.inactiveIcon.startColorRes),
                icon = (VectorDrawableCompat.create(context.resources, covertConfig.inactiveIcon.iconRes, context.theme) as Drawable).mutate(),
                clipStartProportion = null
        )

        // Circular reveal drawable
        val backgroundDrawable = CircularRevealCanvasDrawable(
                interactionStartProportion = ANIMATION_THRESHOLD_PROPORTION,
                revealData = AnimationData(
                        startProportion = SWIPE_THRESHOLD_PROPORTION,
                        duration = REVEAL_DURATION
                ),
                centerXInset = iconInset,
                backdropPaint = Paint().apply {
                    color = ContextCompat.getColor(context, covertConfig.inactiveBackdropColorRes)
                }
        )

        // Foreground icon lift drawable
        val foregroundIconDrawable = IconLiftCanvasDrawable(
                liftStartProportion = ANIMATION_THRESHOLD_PROPORTION - ICON_LIFT_ANIMATION_OFFSET_PROPORTION,
                bounceAnimationData = AnimationData(
                        startProportion = SWIPE_THRESHOLD_PROPORTION,
                        duration = BOUNCE_DURATION
                ),
                maxIconScaleProportion = MAX_LIFT_SCALE,
                iconInsetPx = iconInset.toInt(),
                iconSizePx = ICON_DEFAULT_SIZE_DP.toPx(context).toInt(),
                iconColor = ContextCompat.getColor(context, covertConfig.inactiveIcon.endColorRes),
                icon = (VectorDrawableCompat.create(context.resources, covertConfig.inactiveIcon.iconRes, context.theme) as Drawable).mutate(),
                clipStartProportion = ANIMATION_THRESHOLD_PROPORTION
        )

        return listOf(backgroundIconDrawable, backgroundDrawable, foregroundIconDrawable)
    }

    /**
     * Constructs a list of [CanvasDrawable]s which represent the active state, i.e the state which
     * starts as active and proceeds to inactive via a fading background animation.
     *
     * @param context Context
     * @param covertConfig Config to construct [CanvasDrawable]s with
     */
    private fun buildActiveStateDrawables(context: Context, covertConfig: Config): List<CanvasDrawable> {
        val iconInset = ((ICON_DEFAULT_SIZE_DP / 2) + ICON_MARGIN_DP).toPx(context)

        // Fading background
        val backgroundDrawable = FadingBackgroundCanvasDrawable(
                fadeAnimationData = AnimationData(
                        startProportion = SWIPE_THRESHOLD_PROPORTION,
                        duration = FADE_DURATION
                ),
                backdropPaint = Paint().apply {
                    color = ContextCompat.getColor(context, covertConfig.activeBackdropColorRes)
                }
        )

        // Color change
        val foregroundDrawable = IconChangeColorCanvasDrawable(
                colorChangeAnimationData = AnimationData(
                        startProportion = SWIPE_THRESHOLD_PROPORTION,
                        duration = REVEAL_COLOR_CHANGE_DURATION
                ),
                iconSizePx = ICON_DEFAULT_SIZE_DP.toPx(context).toInt(),
                iconInsetPx = iconInset.toInt(),
                iconColor = ColorChange(
                        start = ContextCompat.getColor(context, covertConfig.activeIcon.startColorRes),
                        end = ContextCompat.getColor(context, covertConfig.activeIcon.endColorRes)
                ),
                icon = VectorDrawableCompat.create(context.resources, covertConfig.activeIcon.iconRes, context.theme) as Drawable
        )

        return listOf(backgroundDrawable, foregroundDrawable)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int = when {
        isViewHolderEnabled(viewHolder) && !isReturning ->
            ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.LEFT)
        else ->
            0
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean =
            false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // no-op
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        pullToRefreshView?.isEnabled = actionState != ItemTouchHelper.ACTION_STATE_SWIPE
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (currentDx != 0F) return 0

        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        currentDx = dX

        if (currentDx == 0F) {
            (activeStateDrawables + inactiveStateDrawables).forEach {
                it.reset()
            }

            listenerNotified = false
            isReturning = false
            flagRenderedOnReturn = false
            isViewHolderCurrentlyActive = null
        } else {
            isReturning = !isCurrentlyActive

            // If the ViewHolder is not enabled, we can skip the following which renders the backdrop animation and corner flag
            if (isViewHolderEnabled(viewHolder)) {
                if (isViewHolderCurrentlyActive == null) {
                    isViewHolderCurrentlyActive = isViewHolderActive(viewHolder)
                }

                // If the listener has been notified and the ViewHolder is returning, render the active flag animation
                isViewHolderCurrentlyActive?.let {
                    if (isReturning && listenerNotified && !flagRenderedOnReturn && !it) {
                        renderCornerFlag(viewHolder, showFlag = true, animate = true)
                        flagRenderedOnReturn = true
                    }
                }

                val drawables = when (isViewHolderCurrentlyActive) {
                    true -> activeStateDrawables
                    false -> inactiveStateDrawables
                    else -> emptyList()
                }

                if (drawables.isNotEmpty()) {
                    // Find the total margin applied to the ViewHolder such that animations are always draw within itemView bounds
                    val viewHolderMarginY = (viewHolder.itemView.layoutParams as? RecyclerView.LayoutParams)?.let { it.leftMargin + it.rightMargin }
                            ?: 0

                    val parentMetrics = ParentMetrics(
                            width = viewHolder.itemView.measuredWidth.toFloat() - viewHolderMarginY.toFloat(),
                            height = viewHolder.itemView.measuredHeight.toFloat()
                    )

                    val swipeProportion = abs(dX) / parentMetrics.width

                    if (swipeProportion > SWIPE_THRESHOLD_PROPORTION && !listenerNotified) {
                        onSwiped(viewHolder, SwipeDirection.LEFT)

                        // If the ViewHolder was active, render the removal of the corner flag when swiped
                        if (isViewHolderCurrentlyActive == true) {
                            renderCornerFlag(viewHolder, showFlag = false, animate = true)
                        }
                    }

                    drawables.forEach {
                        it.invalidateCallback = {
                            triggerPartialRebind(viewHolder.adapterPosition)
                        }

                        it.onDraw(c, parentMetrics, viewHolder.itemView.y, swipeProportion)
                    }
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * Draws the [CornerFlag] on a card from somewhere outside of normal [TMarker] swiping operation.
     * This should be used in the [RecyclerView.Adapter.onBindViewHolder] callback to update the
     * user-visible check state of the ViewHolder
     *
     * @param viewHolder The viewholder to draw the corner flag on
     */
    @JvmOverloads
    fun drawCornerFlag(viewHolder: RecyclerView.ViewHolder, animate: Boolean = false) {
        renderCornerFlag(viewHolder, showFlag = isViewHolderActive(viewHolder), animate = animate)
    }

    /**
     * Triggers a partial rebind fo the [RecyclerView.ViewHolder] at adapter position [index]
     *
     * @param index Index to invalidate
     */
    private fun triggerPartialRebind(index: Int) {
        updateSubject.onNext(index)
    }

    /**
     * Callback invoked when the swipe threshold is reached
     *
     * @param viewHolder The [RecyclerView.ViewHolder] which was swiped
     * @param direction The [SwipeDirection] the [RecyclerView.ViewHolder] was swiped
     */
    private fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: SwipeDirection) {
        onSwipe(viewHolder, direction)
        listenerNotified = true

        if (isHapticFeedbackEnabled) {
            vibrate(viewHolder.itemView.context)
        }
    }

    /**
     * Performs a short vibrate
     *
     * @param context Context
     */
    @SuppressLint("MissingPermission")
    private fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT > 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50L, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50L)
        }
    }

    /**
     * Renders a [CornerFlag] on the given [RecyclerView.ViewHolder]. If [animate] is set,
     * the [CornerFlag] will animate in from the side which was swiped from when activating,
     * and will fade out when becoming inactive
     *
     * @param viewHolder The [RecyclerView.ViewHolder] to render the flag on
     * @param showFlag Whether or not to show the flag
     * @param animate Wheter or not the flag should animate in or out
     */
    private fun renderCornerFlag(viewHolder: RecyclerView.ViewHolder, showFlag: Boolean, animate: Boolean) {
        if (cornerFlag == CornerFlag.Disabled) {
            return
        }

        if (viewHolder.itemView.findViewById<View>(R.id.covertCornerFlag) == null) {
            LayoutInflater.from(viewHolder.itemView.context).inflate(R.layout.corner_flag_layout, viewHolder.itemView as ViewGroup).apply {
                findViewById<ImageView>(R.id.covertCornerFlag).apply {
                    setImageResource(cornerFlag.shapeRes)
                    setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN)
                }
            }
        }

        val cornerFlagView = viewHolder.itemView.findViewById<View>(R.id.covertCornerFlag)

        val hiddenTranslation = viewHolder.itemView.resources.getDimensionPixelSize(cornerFlag.widthRes)

        if (showFlag) {
            if (animate) {
                cornerFlagView.translationX = viewHolder.itemView.measuredWidth.toFloat()

                cornerFlagView
                        .animate()
                        .translationX(viewHolder.itemView.measuredWidth.toFloat() - hiddenTranslation)
                        .setInterpolator(FastOutLinearInInterpolator())
                        .setDuration(200L)
                        .withStartAction {
                            cornerFlagView.alpha = 1F
                        }
                        .start()
            } else {
                cornerFlagView.translationX = viewHolder.itemView.measuredWidth.toFloat() - hiddenTranslation
                cornerFlagView.alpha = 1F
            }
        } else {
            if (animate) {
                // Set translation and alpha
                cornerFlagView
                        .animate()
                        .translationX(viewHolder.itemView.measuredWidth.toFloat())
                        .alpha(0F)
                        .setDuration(50L)
                        .withEndAction {
                            cornerFlagView.alpha = 0F
                        }
                        .start()
            } else {
                cornerFlagView.translationX = viewHolder.itemView.measuredWidth.toFloat()
                cornerFlagView.alpha = 0F
            }
        }
    }

    /**
     * Utility function for converting a [Float] representing Density-Independent Pixels (dp) to Pixels (px)
     */
    private fun Float.toPx(context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return this * (metrics.densityDpi / 160f)
    }

    /**
     * [TMarker.Config] allows you to modify the look and feel of [TMarker]. The primary constructor
     * allows full configurability of which icons to draw in active and inactive states, which
     * backdrop colors are to be used, start and end animated colors for icons, and much more.
     *
     * For more information, see the Advanced Setup section of the README
     */
    data class Config(
            val activeIcon: Icon,
            val inactiveIcon: Icon,
            @ColorRes val activeBackdropColorRes: Int,
            @ColorRes val inactiveBackdropColorRes: Int,
            val isHapticFeedbackEnabled: Boolean,
            val cornerFlag: CornerFlag
    ) {

        /**
         * Simplified secondary constructor to allow for easy implementation of the stock behavior.
         *
         * @param iconRes Icon resource identifier
         * @param iconDefaultColorRes Icon color resource identifier
         * @param actionColorRes Backdrop color resource identifier
         * @param isHapticFeedbackEnabled Whether or not haptic feedback is enabled
         * @param changeIconColor Whether or not to change the icon color mid-animation
         * @param cornerFlag The cornerflag to render on the ItemView when active state is changed
         */
        @JvmOverloads
        constructor(
                @DrawableRes iconRes: Int,
                @ColorRes iconDefaultColorRes: Int,
                @ColorRes actionColorRes: Int,
                isHapticFeedbackEnabled: Boolean = true,
                changeIconColor: Boolean = false,
                cornerFlag: CornerFlag = CornerFlag.Round
        ) : this(
                activeIcon = Icon(
                        iconRes = iconRes,
                        startColorRes = iconDefaultColorRes,
                        endColorRes = if (changeIconColor) actionColorRes else iconDefaultColorRes
                ),
                inactiveIcon = Icon(
                        iconRes = iconRes,
                        startColorRes = if (changeIconColor) actionColorRes else iconDefaultColorRes,
                        endColorRes = iconDefaultColorRes
                ),
                activeBackdropColorRes = actionColorRes,
                inactiveBackdropColorRes = actionColorRes,
                cornerFlag = cornerFlag,
                isHapticFeedbackEnabled = isHapticFeedbackEnabled
        )
    }

    /**
     * Enumerated class representing a swipe direction
     */
    enum class SwipeDirection {
        LEFT,
        RIGHT
    }

    /**
     * Data class representing the icon to be rendered - can optionally use different start
     * and end colors
     */
    data class Icon(
            @DrawableRes val iconRes: Int,
            @ColorRes val startColorRes: Int,
            @ColorRes val endColorRes: Int
    )

    /**
     * Base class for a corner flag
     */
    sealed class CornerFlag(
            @DimenRes val widthRes: Int,
            @DrawableRes val shapeRes: Int
    ) {

        /**
         * Renders a round corner flag
         */
        object Round : CornerFlag(
                widthRes = R.dimen.circular_cornerflag_width,
                shapeRes = R.drawable.circular_background)

        /**
         * Renders a triangular corner flag
         */
        object Triangular : CornerFlag(
                widthRes = R.dimen.triangle_cornerflag_width,
                shapeRes = R.drawable.triangle_background)

        /**
         * Disables the corner flag
         */
        object Disabled : CornerFlag(0, 0)

        /**
         * Renders a custom corner flag with given width and shape
         */
        class Custom(widthRes: Int, shapeRes: Int) : CornerFlag(widthRes, shapeRes)
    }

    /**
     * Builder class for constructing an instance of Covert and binding it to a recyclerview
     */
    data class Builder(
            /**
             * The base Covert config describing colors and general configuration
             */
            private val config: Config,
            /**
             * The callback used by Covert to determine if it should render the
             * active or inactive graphics
             */
            private val viewHolderActiveCallback: ViewHolderCheckCallback? = null,
            /**
             * The callback used by Covert to enable or disable swiping of ViewHolders.
             * This can be done on either a per-ViewHolder basis, or globally.
             *
             * Defaults to enabling swiping on all items
             */
            private val isSwipingEnabledCallback: ViewHolderCheckCallback = { true },
            /**
             * The callback triggered by Covert when an item is swiped.
             */
            private val onSwipeCallback: ViewHolderNotifyCallback = { _, _ -> },
            /**
             * Specifies the pull-to-refresh view associated with the recycler view,
             * if any. Covert will disable the pull-to-refresh gesture while the user
             * is swiping.
             */
            private val pullToRefreshView: SwipeRefreshLayout? = null
    ) {

        /**
         * The callback used by Covert to determine if it should render the
         * active or inactive graphics.
         */
        fun setIsActiveCallback(callback: ViewHolderCheckCallback): Builder = copy(viewHolderActiveCallback = callback)

        /**
         * The callback used by Covert to enable or disable swiping of ViewHolders.
         * This can be done on either a per-ViewHolder basis, or globally.
         *
         * Defaults to enabling swiping on all items.
         */
        fun setSwipeEnabledCallback(callback: ViewHolderCheckCallback): Builder = copy(isSwipingEnabledCallback = callback)

        /**
         * The callback triggered by Covert when an item is swiped.
         */
        fun doOnSwipe(callback: ViewHolderNotifyCallback): Builder = copy(onSwipeCallback = callback)

        /**
         * Specifies the pull-to-refresh view associated with the recycler view.
         * Covert will use this to automatically disable the pull-to-refresh gesture
         * while the user is swiping.
         */
        fun disablePullToRefreshOnSwipe(view: SwipeRefreshLayout): Builder = copy(pullToRefreshView = view)

        /**
         * Instantiated Covert and attaches it to the given recyclerview
         */
        fun attachTo(recyclerView: RecyclerView) = TMarker(
                recyclerView,
                config,
                requireNotNull(viewHolderActiveCallback) { "An active callback must be set" },
                isSwipingEnabledCallback,
                onSwipeCallback,
                pullToRefreshView
        )
    }
}