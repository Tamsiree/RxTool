package com.vondear.rxtools.view.progress;

/**
 * Created by ybq.
 */
public enum Style {

    ROTATING_PLANE(0),
    DOUBLE_BOUNCE(1),
    WAVE(2),
    WANDERING_CUBES(3),
    PULSE(4),
    CHASING_DOTS(5),
    THREE_BOUNCE(6),
    CIRCLE(7),
    CUBE_GRID(8),
    FADING_CIRCLE(9),
    FOLDING_CUBE(10),
    ROTATING_CIRCLE(11),
    MULTIPLE_PULSE(12),
    PULSE_RING(13),
    MULTIPLE_PULSE_RING(14);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private int value;

    Style(int value) {
        this.value = value;
    }
}
