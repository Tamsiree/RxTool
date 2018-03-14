package com.vondear.rxtools.view.progressing.animation.interpolator;

import android.graphics.Path;
import android.os.Build;
import android.view.animation.Interpolator;

/**
 * @author vondear
 * Helper for creating path-based {@link Interpolator} instances. On API 21 or newer, the
 * platform implementation will be used and on older platforms a compatible alternative
 * implementation will be used.
 */
public class PathInterpolatorCompat {

    private PathInterpolatorCompat() {
        // prevent instantiation
    }

    /**
     * Create an {@link Interpolator} for an arbitrary {@link Path}. The {@link Path}
     * must begin at {@code (0, 0)} and end at {@code (1, 1)}. The x-coordinate along the
     * {@link Path} is the input value and the output is the y coordinate of the line at that
     * point. This means that the Path must conform to a function {@code y = f(x)}.
     * <p>
     * The {@link Path} must not have gaps in the x direction and must not
     * loop back on itself such that there can be two points sharing the same x coordinate.
     *
     * @param path the {@link Path} to use to make the line representing the {@link Interpolator}
     * @return the {@link Interpolator} representing the {@link Path}
     */
    @SuppressWarnings("unused")
    public static Interpolator create(Path path) {
        if (Build.VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(path);
        }
        return PathInterpolatorCompatBase.create(path);
    }

    /**
     * Create an {@link Interpolator} for a quadratic Bezier curve. The end points
     * {@code (0, 0)} and {@code (1, 1)} are assumed.
     *
     * @param controlX the x coordinate of the quadratic Bezier control point
     * @param controlY the y coordinate of the quadratic Bezier control point
     * @return the {@link Interpolator} representing the quadratic Bezier curve
     */
    @SuppressWarnings("unused")
    public static Interpolator create(float controlX, float controlY) {
        if (Build.VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(controlX, controlY);
        }
        return PathInterpolatorCompatBase.create(controlX, controlY);
    }

    /**
     * Create an {@link Interpolator} for a cubic Bezier curve.  The end points
     * {@code (0, 0)} and {@code (1, 1)} are assumed.
     *
     * @param controlX1 the x coordinate of the first control point of the cubic Bezier
     * @param controlY1 the y coordinate of the first control point of the cubic Bezier
     * @param controlX2 the x coordinate of the second control point of the cubic Bezier
     * @param controlY2 the y coordinate of the second control point of the cubic Bezier
     * @return the {@link Interpolator} representing the cubic Bezier curve
     */
    public static Interpolator create(float controlX1, float controlY1,
                                      float controlX2, float controlY2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(controlX1, controlY1, controlX2, controlY2);
        }
        return PathInterpolatorCompatBase.create(controlX1, controlY1, controlX2, controlY2);
    }
}
