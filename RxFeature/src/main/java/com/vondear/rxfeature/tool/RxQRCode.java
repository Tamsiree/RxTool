package com.vondear.rxfeature.tool;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vondear.rxtool.RxImageTool;

import java.util.Hashtable;

/**
 * @author vondear
 * @date 2017/2/17.
 */

public class RxQRCode {

    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     * @return {@link RxQRCode.Builder}
     */
    public static RxQRCode.Builder builder(@NonNull CharSequence text) {
        return new RxQRCode.Builder(text);
    }

    public static class Builder {

        private int backgroundColor = 0xffffffff;

        private int codeColor = 0xff000000;

        private int codeSide = 800;

        private int codeBorder = 1;

        private CharSequence content;

        private Bitmap bitmapLogo;

        public Builder backColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder codeColor(int codeColor) {
            this.codeColor = codeColor;
            return this;
        }

        public Builder codeSide(int codeSide) {
            this.codeSide = codeSide;
            return this;
        }

        public Builder codeLogo(Bitmap bitmap) {
            this.bitmapLogo = bitmap;
            return this;
        }

        public Builder codeLogo(Drawable drawable) {
            this.bitmapLogo = RxImageTool.drawable2Bitmap(drawable);
            return this;
        }

        public Builder codeBorder(int codeBorder){
            this.codeBorder = codeBorder;
            return this;
        }

        public Builder(@NonNull CharSequence text) {
            this.content = text;
        }

        public Bitmap into(ImageView imageView) {
            Bitmap bitmap = RxQRCode.creatQRCode(content, codeSide, codeSide, codeBorder,backgroundColor, codeColor,bitmapLogo);
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            return bitmap;
        }
    }

    //----------------------------------------------------------------------------------------------以下为生成二维码算法

    public static Bitmap creatQRCode(CharSequence content, int QR_WIDTH, int QR_HEIGHT, int border,int backgroundColor, int codeColor) {
        Bitmap bitmap = null;
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, border+"");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content + "", BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = codeColor;
                    } else {
                        pixels[y * QR_WIDTH + x] = backgroundColor;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap creatQRCode(CharSequence content, int QR_WIDTH, int QR_HEIGHT) {
        return creatQRCode(content, QR_WIDTH, QR_HEIGHT, 1,0xffffffff, 0xff000000);
    }

    public static Bitmap creatQRCode(CharSequence content) {
        return creatQRCode(content, 800, 800);
    }

    public static Bitmap creatQRCode(CharSequence content, Bitmap logo) {
        return creatQRCode(content, 800, 800,1,0xffffffff, 0xff000000, logo);
    }

    //==============================================================================================二维码算法结束


    public static Bitmap creatQRCode(CharSequence content, int QR_WIDTH, int QR_HEIGHT, int border,int backgroundColor, int codeColor, Bitmap logo) {
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }

            Bitmap scaleLogo = getScaleLogo(logo, QR_WIDTH, QR_HEIGHT);

            int offsetX = QR_WIDTH / 2;
            int offsetY = QR_HEIGHT / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (QR_WIDTH - scaleWidth) / 2;
                offsetY = (QR_HEIGHT - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, border);
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content.toString(), BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (x >= offsetX && x < offsetX + scaleWidth && y >= offsetY && y < offsetY + scaleHeight) {
                        int pixel = scaleLogo.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (bitMatrix.get(x, y)) {
                                pixel = codeColor;
                            } else {
                                pixel = backgroundColor;
                            }
                        }
                        pixels[y * QR_WIDTH + x] = pixel;
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * QR_WIDTH + x] = codeColor;
                        } else {
                            pixels[y * QR_WIDTH + x] = backgroundColor;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param content   需要转换的字符串
     * @param QR_WIDTH  二维码的宽度
     * @param QR_HEIGHT 二维码的高度
     * @param iv_code   图片空间
     */
    public static void createQRCode(String content, int QR_WIDTH, int QR_HEIGHT, ImageView iv_code) {
        iv_code.setImageBitmap(creatQRCode(content, QR_WIDTH, QR_HEIGHT));
    }

    /**
     * QR_WIDTH  二维码的宽度
     * QR_HEIGHT 二维码的高度
     *
     * @param content 需要转换的字符串
     * @param iv_code 图片空间
     */
    public static void createQRCode(String content, ImageView iv_code) {
        iv_code.setImageBitmap(creatQRCode(content));
    }

    private static Bitmap getScaleLogo(Bitmap logo, int w, int h) {
        if (logo == null) return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
        matrix.postScale(scaleFactor, scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }
}
