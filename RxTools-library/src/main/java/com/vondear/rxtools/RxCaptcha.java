package com.vondear.rxtools;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * 随机生成验证码，使用方法：
 *
 * 拿到验证码图片ImageView
 * code_image.setImageBitmap(code.getInstance().getBitmap());
 *
 * 拿到验证码值 String
 * code=code.getInstance().getCode();
 * <p/>
 * RxCaptcha
 * 2015年2月10日 上午11:32:34
 *
 * @version 1.0.0
 */
public class RxCaptcha {

    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};

    private static RxCaptcha bpUtil;

    private RxCaptcha() {
    }

    public static RxCaptcha getInstance() {
        if (bpUtil == null)
            bpUtil = new RxCaptcha();
        return bpUtil;
    }

    // default settings
    private static final int DEFAULT_CODE_LENGTH = 4;// 验证码的长度 这里是4位
    private static final int DEFAULT_FONT_SIZE = 60;// 字体大小
    private static final int DEFAULT_LINE_NUMBER = 0;// 多少条干扰线
    private static final int BASE_PADDING_LEFT = 20; // 左边距
    private static final int RANGE_PADDING_LEFT = 20;// 左边距范围值
    private static final int BASE_PADDING_TOP = 42;// 上边距
    private static final int RANGE_PADDING_TOP = 15;// 上边距范围值
    private static final int DEFAULT_WIDTH = 200;// 默认宽度.图片的总宽
    private static final int DEFAULT_HEIGHT = 70;// 默认高度.图片的总高
    private final int DEFAULT_COLOR = 0xdf;// 默认背景颜色值

    // settings decided by the layout xml
    // canvas width and height
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    // random word space and pading_top
    private int base_padding_left = BASE_PADDING_LEFT;
    private int range_padding_left = RANGE_PADDING_LEFT;
    private int base_padding_top = BASE_PADDING_TOP;
    private int range_padding_top = RANGE_PADDING_TOP;

    // number of chars, lines; font size
    private int codeLength = DEFAULT_CODE_LENGTH;
    private int line_number = DEFAULT_LINE_NUMBER;
    private int font_size = DEFAULT_FONT_SIZE;

    // variables
    private String code;// 保存生成的验证码
    private int padding_left, padding_top;
    private Random random = new Random();

    private Bitmap createBitmap() {
        padding_left = 0;

        Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();

        c.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));
        Paint paint = new Paint();
        paint.setTextSize(font_size);

        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }

        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }

        c.save(Canvas.ALL_SAVE_FLAG);// 保存
        c.restore();//
        return bp;
    }

    public String getCode() {
        return code.toLowerCase();
    }

    public Bitmap getBitmap() {
        return createBitmap();
    }

    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean()); // true为粗体，false为非粗体
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        // paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
        // paint.setUnderlineText(true); //true为下划线，false为非下划线
        // paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }
}
