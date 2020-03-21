package com.tamsiree.rxdemo.view

import android.animation.TypeEvaluator
import android.graphics.PointF

/**
 * Created by tamsiree on 16-11-13.
 */
/**
 * 自定义估值器
 * @author tamsiree
 */
class RxPointFTypeEvaluator(
        /**
         * 每个估值器对应一个属性动画，每个属性动画仅对应唯一一个控制点
         */
        var control: PointF) : TypeEvaluator<PointF> {

    /**
     * 估值器返回值
     */
    var mPointF = PointF()
    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
        return getBezierPoint(startValue, endValue, control, fraction)
    }

    /**
     * 二次贝塞尔曲线公式
     *
     * @param start   开始的数据点
     * @param end     结束的数据点
     * @param control 控制点
     * @param t       float 0-1
     * @return 不同t对应的PointF
     */
    private fun getBezierPoint(start: PointF, end: PointF, control: PointF, t: Float): PointF {
        mPointF.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x
        mPointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y
        return mPointF
    }

}