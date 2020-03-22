package com.tamsiree.rxdemo.activity

import android.graphics.PointF
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.scaleimage.ImageSource
import com.tamsiree.rxui.view.scaleimage.RxScaleImageView
import kotlinx.android.synthetic.main.activity_rx_scale_image_view.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityRxScaleImageView : ActivityBase(), View.OnClickListener {

    private var position = 0
    private var notes: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_scale_image_view)
        setPortrait(this)
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_POSITION)) {
            position = savedInstanceState.getInt(BUNDLE_POSITION)
        }

    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        next.setOnClickListener(this)
        previous.setOnClickListener(this)
        play.setOnClickListener(this)

    }

    override fun initData() {
        notes = Arrays.asList(
                Note("A demo", "点击播放按钮,将在图像上生成随机点缩放并变焦,显示标记。"),
                Note("Limited pan", "如果目标点附近的边缘图像,它将尽可能靠近中心。"),
                Note("Unlimited pan", "无限制的目标点总是可以动画中心。"),
                Note("Customisation", "持续时间是可配置的,你也可以做动画不可中断。")
        )
        initialiseImage()
        updateNotes()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_POSITION, position)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.next) {
            position++
            updateNotes()
        } else if (view.id == R.id.previous) {
            position--
            updateNotes()
        } else if (view.id == R.id.play) {
            val random = Random()
            if (imageView.isReady) {
                val maxScale = imageView.maxScale
                val minScale = imageView.minScale
                val scale = random.nextFloat() * (maxScale - minScale) + minScale
                val center = PointF(random.nextInt(imageView.sWidth).toFloat(), random.nextInt(imageView.sHeight).toFloat())
                imageView.setPin(center)
                val animationBuilder = imageView.animateScaleAndCenter(scale, center)
                if (position == 3) {
                    animationBuilder.withDuration(2000).withEasing(RxScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start()
                } else {
                    animationBuilder.withDuration(750).start()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun initialiseImage() {
        imageView.setImage(ImageSource.asset("squirrel.jpg"))
    }

    private fun updateNotes() {
        if (position > notes!!.size - 1) {
            return
        }
        note.text = notes!![position].text
        next.visibility = if (position >= notes!!.size - 1) View.INVISIBLE else View.VISIBLE
        previous.visibility = if (position <= 0) View.INVISIBLE else View.VISIBLE
        if (position == 2) {
            imageView.setPanLimit(RxScaleImageView.PAN_LIMIT_CENTER)
        } else {
            imageView.setPanLimit(RxScaleImageView.PAN_LIMIT_INSIDE)
        }
    }

    private class Note internal constructor(private val subtitle: String, val text: String)

    companion object {
        private const val BUNDLE_POSITION = "position"
    }
}