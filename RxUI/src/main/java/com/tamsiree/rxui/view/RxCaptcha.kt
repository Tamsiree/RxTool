package com.tamsiree.rxui.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import java.util.*

/**
 * 随机生成验证码，使用方法：
 *
 *
 * 拿到验证码图片ImageView
 * mIvCode.setImageBitmap(RxCaptcha.getInstance().createBitmap());
 * int code=RxCaptcha.getInstance().getCode();
 *
 *
 * 只需生成验证码值 String
 *
 *
 *
 *
 * RxCaptcha
 * 2015年2月10日 上午11:32:34
 *
 * @author tamsiree
 * @version 1.0.0
 */
class RxCaptcha {
    private var type = TYPE.CHARS

    enum class TYPE {
        //数字
        NUMBER,  //字母
        LETTER,  //数字 加 字母
        CHARS
    }

    private constructor()
    private constructor(types: TYPE) {
        type = types
    }

    //默认背景颜色值
    private var defaultColor = 0xdf

    // settings decided by the layout xml
    // canvas width and height
    private var width = DEFAULT_WIDTH
    private var height = DEFAULT_HEIGHT

    // random word space and pading_top
    private val basePaddingLeft = BASE_PADDING_LEFT
    private val rangePaddingLeft = RANGE_PADDING_LEFT
    private val basePaddingTop = BASE_PADDING_TOP
    private val rangePaddingTop = RANGE_PADDING_TOP

    // number of chars, lines; font size
    private var codeLength = DEFAULT_CODE_LENGTH
    private var lineNumber = DEFAULT_LINE_NUMBER
    private var fontSize = DEFAULT_FONT_SIZE

    // variables
    // 保存生成的验证码
    private var code: String? = null
    private var paddingLeft = 0
    private var paddingTop = 0
    private val random = Random()

    /**
     * @param length 验证码的长度
     * @return
     */
    fun codeLength(length: Int): RxCaptcha? {
        codeLength = length
        return rxCaptcha
    }

    /**
     * @param size 字体大小
     * @return
     */
    fun fontSize(size: Int): RxCaptcha? {
        fontSize = size
        return rxCaptcha
    }

    /**
     * @param number 干扰线 数量
     * @return
     */
    fun lineNumber(number: Int): RxCaptcha? {
        lineNumber = number
        return rxCaptcha
    }

    /**
     * @return 背景颜色值
     */
    fun backColor(colorInt: Int): RxCaptcha? {
        defaultColor = colorInt
        return rxCaptcha
    }

    fun type(type: TYPE): RxCaptcha? {
        this.type = type
        return rxCaptcha
    }

    fun size(width: Int, height: Int): RxCaptcha? {
        this.width = width
        this.height = height
        return rxCaptcha
    }

    private fun makeBitmap(): Bitmap {
        paddingLeft = 0
        val bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bp)
        code = makeCode()
        c.drawColor(Color.rgb(defaultColor, defaultColor, defaultColor))
        val paint = Paint()
        paint.textSize = fontSize.toFloat()
        for (i in 0 until code!!.length) {
            randomTextStyle(paint)
            randomPadding()
            c.drawText(code!![i].toString() + "", paddingLeft.toFloat(), paddingTop.toFloat(), paint)
        }
        for (i in 0 until lineNumber) {
            drawLine(c, paint)
        }

        // 保存
        c.save() //Canvas.ALL_SAVE_FLAG
        c.restore() //
        return bp
    }

    fun getCode(): String {
        return code!!.toLowerCase()
    }

    fun into(imageView: ImageView?): Bitmap? {
        val bitmap = createBitmap()
        imageView?.setImageBitmap(bitmap)
        return bitmap
    }

    fun createCode(): String {
        return makeCode()
    }

    private var mBitmapCode: Bitmap? = null
    fun createBitmap(): Bitmap? {
        mBitmapCode = makeBitmap()
        return mBitmapCode
    }

    private fun makeCode(): String {
        val buffer = StringBuilder()
        when (type) {
            TYPE.NUMBER -> {
                var i = 0
                while (i < codeLength) {
                    buffer.append(CHARS_NUMBER[random.nextInt(CHARS_NUMBER.size)])
                    i++
                }
            }
            TYPE.LETTER -> {
                var i = 0
                while (i < codeLength) {
                    buffer.append(CHARS_LETTER[random.nextInt(CHARS_LETTER.size)])
                    i++
                }
            }
            TYPE.CHARS -> {
                var i = 0
                while (i < codeLength) {
                    buffer.append(CHARS_ALL[random.nextInt(CHARS_ALL.size)])
                    i++
                }
            }
            else -> {
                var i = 0
                while (i < codeLength) {
                    buffer.append(CHARS_ALL[random.nextInt(CHARS_ALL.size)])
                    i++
                }
            }
        }
        return buffer.toString()
    }

    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = randomColor()
        val startX = random.nextInt(width)
        val startY = random.nextInt(height)
        val stopX = random.nextInt(width)
        val stopY = random.nextInt(height)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    private fun randomColor(rate: Int = 1): Int {
        val red = random.nextInt(256) / rate
        val green = random.nextInt(256) / rate
        val blue = random.nextInt(256) / rate
        return Color.rgb(red, green, blue)
    }

    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.color = color
        // true为粗体，false为非粗体
        paint.isFakeBoldText = random.nextBoolean()
        var skewX = random.nextInt(11) / 10.toFloat()
        skewX = if (random.nextBoolean()) skewX else -skewX
        // paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
        // paint.setUnderlineText(true); //true为下划线，false为非下划线
        // paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    private fun randomPadding() {
        paddingLeft += basePaddingLeft + random.nextInt(rangePaddingLeft)
        paddingTop = basePaddingTop + random.nextInt(rangePaddingTop)
    }

    companion object {
        fun build(): RxCaptcha? {
            if (rxCaptcha == null) {
                rxCaptcha = RxCaptcha()
            }
            return rxCaptcha
        }

        private val CHARS_NUMBER = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        private val CHARS_LETTER = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z')
        private val CHARS_ALL = charArrayOf('0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z')
        private var rxCaptcha: RxCaptcha? = null
        fun getInstance(types: TYPE): RxCaptcha? {
            if (rxCaptcha == null) {
                rxCaptcha = RxCaptcha(types)
            }
            return rxCaptcha
        }

        //default settings
        //验证码的长度 这里是4位
        private const val DEFAULT_CODE_LENGTH = 4

        //字体大小
        private const val DEFAULT_FONT_SIZE = 60

        //多少条干扰线
        private const val DEFAULT_LINE_NUMBER = 0

        //左边距
        private const val BASE_PADDING_LEFT = 20

        //左边距范围值
        private const val RANGE_PADDING_LEFT = 20

        //上边距
        private const val BASE_PADDING_TOP = 42

        //上边距范围值
        private const val RANGE_PADDING_TOP = 15

        //默认宽度.图片的总宽
        private const val DEFAULT_WIDTH = 200

        //默认高度.图片的总高
        private const val DEFAULT_HEIGHT = 70
    }
}