package com.tamsiree.rxui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tamsiree.rxui.R

/**
 * Created by chenupt@gmail.com on 11/20/14.
 * Description : custom layout to draw bezier
 */
class TUnReadView : FrameLayout {
    // 手势坐标
    var handX = 300f
    var handY = 300f

    // 锚点坐标
    var anchorX = 200f
    var anchorY = 300f

    // 起点坐标
    var startX = 100f
    var startY = 100f

    // 定点圆半径
    var radius = DEFAULT_RADIUS

    // 判断动画是否开始
    var isAnimStart = false

    // 判断是否开始拖动
    var isTouch = false
    var exploredImageView: ImageView? = null

    //  ImageView tipImageView;
    var tipTextView: TextView? = null
    private var paint: Paint? = null
    private var path: Path? = null

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> tipTextView!!.text = msg.obj.toString()
                2 -> {
                    tipTextView!!.text = ""
                    tipTextView!!.setBackgroundResource(R.drawable.skin_tips_new)
                }
                else -> {
                }
            }
        }
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        path = Path()
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL_AND_STROKE
        paint!!.strokeWidth = 2f
        paint!!.color = Color.RED
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        exploredImageView = ImageView(context)
        exploredImageView!!.layoutParams = params
        exploredImageView!!.setImageResource(R.drawable.tip_anim)
        exploredImageView!!.visibility = View.INVISIBLE
        tipTextView = TextView(context)
        tipTextView!!.layoutParams = params
        tipTextView!!.setTextColor(Color.parseColor("#FFFFFF"))
        tipTextView!!.setPadding(30, 5, 0, 0)
        //        tipTextView.setGravity(Gravity.CENTER);
        tipTextView!!.setBackgroundResource(R.drawable.skin_tips_newmessage_ninetynine)
        /*   tipImageView = new ImageView(getContext());
        tipImageView.setLayoutParams(params);
        tipImageView.setImageResource(R.drawable.skin_tips_newmessage_ninetynine);*/
        //addView(tipImageView);
        addView(tipTextView)
        addView(exploredImageView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        exploredImageView!!.x = startX - exploredImageView!!.width / 2
        exploredImageView!!.y = startY - exploredImageView!!.height / 2
        /*  tipImageView.setX(startX - tipImageView.getWidth()/2);
        tipImageView.setY(startY - tipImageView.getHeight()/2);*/tipTextView!!.x = startX - tipTextView!!.width / 2
        tipTextView!!.y = startY - tipTextView!!.height / 2
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun calculate() {
        val distance = Math.sqrt(Math.pow(handY - startY.toDouble(), 2.0) + Math.pow(handX - startX.toDouble(), 2.0)).toFloat()
        radius = -distance / 15 + DEFAULT_RADIUS
        if (radius < 9) {
            isAnimStart = true
            exploredImageView!!.visibility = View.VISIBLE
            exploredImageView!!.setImageResource(R.drawable.tip_anim)
            (exploredImageView!!.drawable as AnimationDrawable).stop()
            (exploredImageView!!.drawable as AnimationDrawable).start()

            //tipImageView.setVisibility(View.GONE);
            tipTextView!!.visibility = View.GONE
        }

        // 根据角度算出四边形的四个点
        val offsetX = (radius * Math.sin(Math.atan((handY - startY) / (handX - startX).toDouble()))).toFloat()
        val offsetY = (radius * Math.cos(Math.atan((handY - startY) / (handX - startX).toDouble()))).toFloat()
        val x1 = startX - offsetX
        val y1 = startY + offsetY
        val x2 = handX - offsetX
        val y2 = handY + offsetY
        val x3 = handX + offsetX
        val y3 = handY - offsetY
        val x4 = startX + offsetX
        val y4 = startY - offsetY
        path!!.reset()
        path!!.moveTo(x1, y1)
        path!!.quadTo(anchorX, anchorY, x2, y2)
        path!!.lineTo(x3, y3)
        path!!.quadTo(anchorX, anchorY, x4, y4)
        path!!.lineTo(x1, y1)

        // 更改图标的位置
        /* tipImageView.setX(x - tipImageView.getWidth()/2);
        tipImageView.setY(y - tipImageView.getHeight()/2);*/tipTextView!!.x = handX - tipTextView!!.width / 2
        tipTextView!!.y = handY - tipTextView!!.height / 2
    }

    override fun onDraw(canvas: Canvas) {
        if (isAnimStart || !isTouch) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY)
        } else {
            calculate()
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY)
            canvas.drawPath(path!!, paint!!)
            canvas.drawCircle(startX, startY, radius, paint!!)
            canvas.drawCircle(handX, handY, radius, paint!!)
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // 判断触摸点是否在tipImageView中
            val rect = Rect()
            val location = IntArray(2)
            /* tipImageView.getDrawingRect(rect);
            tipImageView.getLocationOnScreen(location);*/tipTextView!!.getDrawingRect(rect)
            tipTextView!!.getLocationOnScreen(location)
            rect.left = location[0]
            rect.top = location[1]
            rect.right = rect.right + location[0]
            rect.bottom = rect.bottom + location[1]
            if (rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                isTouch = true
            }
        } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            isTouch = false
            /*   tipImageView.setX(startX - tipImageView.getWidth()/2);
            tipImageView.setY(startY - tipImageView.getHeight()/2);
*/tipTextView!!.x = startX - tipTextView!!.width / 2
            tipTextView!!.y = startY - tipTextView!!.height / 2
        }
        invalidate()
        if (isAnimStart) {
            return super.onTouchEvent(event)
        }
        anchorX = (event.x + startX) / 2
        anchorY = (event.y + startY) / 2
        handX = event.x
        handY = event.y
        return true
    }

    /**
     * 设置信息的数量
     *
     * @param text
     */
    fun setText(text: CharSequence?) {
        val msg = Message()
        msg.obj = text
        msg.what = 1
        mHandler.sendMessage(msg)
    }

    /**
     * 设置有新的信息
     */
    fun setNewText() {
        mHandler.sendEmptyMessage(2)
    }

    companion object {
        // 默认定点圆半径
        const val DEFAULT_RADIUS = 20f
    }
}