package com.vondear.rxarcgiskit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.vondear.rxarcgiskit.R;


/**
 * Created by Administrator on 2017/11/5.
 */

public class RxMapScaleView extends View {

    private Context context;
    private int scaleWidth;
    private int scaleHeight;
    private int textColor;
    private MapView mapView;
    private String text;
    private int textSize;
    private int scaleSpaceText;
    private Paint mPaint;

    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public RxMapScaleView(Context context) {
        super(context);
        this.context = context;
        this.initVariables();
    }

    public RxMapScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.initVariables();
    }

    public RxMapScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.initVariables();
    }

    private void initVariables() {
        scaleWidth = 104;//
        scaleHeight = 8;//比比例尺宽度例尺高度
        textColor = Color.BLACK;//比例尺字体颜色
        text = "20公里";//比例尺文本
        textSize = 18;//比例尺宽度
        scaleSpaceText = 8;//比例尺文本与图形的间隔高度
        mPaint = new Paint();//画笔
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = getWidthSize(widthMeasureSpec);
        int heightSize = getHeightSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 测量ScaleView的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int getHeightSize(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                height = textSize + scaleSpaceText + scaleHeight;
                break;
            case MeasureSpec.EXACTLY: {
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            }
            case MeasureSpec.UNSPECIFIED: {
                height = Math.max(textSize + scaleSpaceText + scaleHeight, MeasureSpec.getSize(heightMeasureSpec));
                break;
            }
            default:
                break;
        }
        return height;
    }

    /**
     * 测量ScaleView的宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int getWidthSize(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }


    /**
     * 绘制上面的文字和下面的比例尺，因为比例尺是.9.png，
     * 我们需要利用drawNinepath方法绘制比例尺
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = scaleWidth;
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float textWidth = mPaint.measureText(text);
        canvas.drawText(text, (width - textWidth) / 2, textSize, mPaint);
        Rect scaleRect = new Rect(0, textSize + scaleSpaceText, width, textSize + scaleSpaceText + scaleHeight);
        drawNinepath(canvas, R.drawable.plotting_scale_new, scaleRect);
    }

    // 绘制.9.PNG图片：
    private void drawNinepath(Canvas canvas, int resId, Rect rect) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
    }

    /**
     * 根据缩放级别更新ScaleView的文字以及比例尺的长度
     */
    public void refreshScaleView() {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        //结果单位米，表示图上1厘米代表*米
        double scale = this.mapView.getMapScale() / 100 ;
        // ppi为每英尺像素数，
        // ppi/2.54为1厘米的像素数
        double ppi = getPPIOfDevice();
        if (scale > 0 && scale <= 5) {//换算5米
            String unit = "5米";
            int scaleWidth = (int) (5 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 5 && scale <= 10) {//换算10米
            String unit = "10米";
            int scaleWidth = (int) (10 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 10 && scale <= 20) {//换算20米
            String unit = "20米";
            int scaleWidth = (int) (20 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 20 && scale <= 50) {//换算50米
            String unit = "50米";
            int scaleWidth = (int) (50 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 50 && scale <= 100) {//换算100米
            String unit = "100米";
            int scaleWidth = (int) (100 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 100 && scale <= 200) {//换算200米
            String unit = "200米";
            int scaleWidth = (int) (200 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 200 && scale <= 500) {//换算500米
            String unit = "500米";
            int scaleWidth = (int) (500 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 500 && scale <= 1000) {//换算1公里
            String unit = "1公里";
            int scaleWidth = (int) (1000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 1000 && scale <= 2000) {//换算2公里
            String unit = "2公里";
            int scaleWidth = (int) (2000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 2000 && scale <= 5000) {//换算5公里
            String unit = "5公里";
            int scaleWidth = (int) (5000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 5000 && scale <= 10000) {//换算10公里
            String unit = "10公里";
            int scaleWidth = (int) (10000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 10000 && scale <= 20000) {//换算20公里
            String unit = "20公里";
            int scaleWidth = (int) (20000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 20000 && scale <= 25000) {//换算25公里
            String unit = "25公里";
            int scaleWidth = (int) (25000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 25000 && scale <= 50000) {//换算50公里
            String unit = "50公里";
            int scaleWidth = (int) (50000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 50000 && scale <= 100000) {//换算100公里
            String unit = "100公里";
            int scaleWidth = (int) (100000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 100000 && scale <= 200000) {//换算200公里
            String unit = "200公里";
            int scaleWidth = (int) (200000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 200000 && scale <= 250000) {//换算250公里
            String unit = "250公里";
            int scaleWidth = (int) (250000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 250000 && scale <= 500000) {//换算500公里
            String unit = "500公里";
            int scaleWidth = (int) (500000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        } else if (scale > 500000 && scale <= 1000000) {//换算1000公里
            String unit = "1000公里";
            int scaleWidth = (int) (1000000 * ppi / 2.54 / scale);
            reInitSaleView(unit, scaleWidth);
        }
        invalidate();
    }

    private void reInitSaleView(String unit, int scaleWidth) {
        //更新文字
        setText(unit);
        //更新比例尺长度
        setScaleWidth(scaleWidth);
    }


    private double getPPIOfDevice() {
        Point point = new Point();
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);//获取屏幕的真实分辨率
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);//
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Double ppi = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / screenInches;
        return ppi;
    }
}
