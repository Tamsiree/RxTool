package com.vondear.rxtools.view.likeview.tools.ei;

import android.support.annotation.NonNull;

/**
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
 */
class RxEasingProvider {
    /**
     * @param rxEase            Easing type
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    public static float get(@NonNull RxEase rxEase, float elapsedTimeRate) {
        switch (rxEase) {
            case LINEAR:
                return elapsedTimeRate;
            case QUAD_IN:
                return getPowIn(elapsedTimeRate, 2);
            case QUAD_OUT:
                return getPowOut(elapsedTimeRate, 2);
            case QUAD_IN_OUT:
                return getPowInOut(elapsedTimeRate, 2);
            case CUBIC_IN:
                return getPowIn(elapsedTimeRate, 3);
            case CUBIC_OUT:
                return getPowOut(elapsedTimeRate, 3);
            case CUBIC_IN_OUT:
                return getPowInOut(elapsedTimeRate, 3);
            case QUART_IN:
                return getPowIn(elapsedTimeRate, 4);
            case QUART_OUT:
                return getPowOut(elapsedTimeRate, 4);
            case QUART_IN_OUT:
                return getPowInOut(elapsedTimeRate, 4);
            case QUINT_IN:
                return getPowIn(elapsedTimeRate, 5);
            case QUINT_OUT:
                return getPowOut(elapsedTimeRate, 5);
            case QUINT_IN_OUT:
                return getPowInOut(elapsedTimeRate, 5);
            case SINE_IN:
                return (float) (1f - Math.cos(elapsedTimeRate * Math.PI / 2f));
            case SINE_OUT:
                return (float) Math.sin(elapsedTimeRate * Math.PI / 2f);
            case SINE_IN_OUT:
                return (float) (-0.5f * (Math.cos(Math.PI * elapsedTimeRate) - 1f));
            case BACK_IN:
                return (float) (elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate - 1.7));
            case BACK_OUT:
                return (float) (--elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate + 1.7) + 1f);
            case BACK_IN_OUT:
                return getBackInOut(elapsedTimeRate, 1.7f);
            case CIRC_IN:
                return (float) -(Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate) - 1);
            case CIRC_OUT:
                return (float) Math.sqrt(1f - (--elapsedTimeRate) * elapsedTimeRate);
            case CIRC_IN_OUT:
                if ((elapsedTimeRate *= 2f) < 1f) {
                    return (float) (-0.5f * (Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate) - 1f));
                }
                return (float) (0.5f * (Math.sqrt(1f - (elapsedTimeRate -= 2f) * elapsedTimeRate) + 1f));
            case BOUNCE_IN:
                return getBounceIn(elapsedTimeRate);
            case BOUNCE_OUT:
                return getBounceOut(elapsedTimeRate);
            case BOUNCE_IN_OUT:
                if (elapsedTimeRate < 0.5f) {
                    return getBounceIn(elapsedTimeRate * 2f) * 0.5f;
                }
                return getBounceOut(elapsedTimeRate * 2f - 1f) * 0.5f + 0.5f;
            case ELASTIC_IN:
                return getElasticIn(elapsedTimeRate, 1, 0.3);

            case ELASTIC_OUT:
                return getElasticOut(elapsedTimeRate, 1, 0.3);

            case ELASTIC_IN_OUT:
                return getElasticInOut(elapsedTimeRate, 1, 0.45);

            default:
                return elapsedTimeRate;

        }

    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private static float getPowIn(float elapsedTimeRate, double pow) {
        return (float) Math.pow(elapsedTimeRate, pow);
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private static float getPowOut(float elapsedTimeRate, double pow) {
        return (float) ((float) 1 - Math.pow(1 - elapsedTimeRate, pow));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private static float getPowInOut(float elapsedTimeRate, double pow) {
        if ((elapsedTimeRate *= 2) < 1) {
            return (float) (0.5 * Math.pow(elapsedTimeRate, pow));
        }

        return (float) (1 - 0.5 * Math.abs(Math.pow(2 - elapsedTimeRate, pow)));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amount          amount The strength of the ease.
     * @return easedValue
     */
    private static float getBackInOut(float elapsedTimeRate, float amount) {
        amount *= 1.525;
        if ((elapsedTimeRate *= 2) < 1) {
            return (float) (0.5 * (elapsedTimeRate * elapsedTimeRate * ((amount + 1) * elapsedTimeRate - amount)));
        }
        return (float) (0.5 * ((elapsedTimeRate -= 2) * elapsedTimeRate * ((amount + 1) * elapsedTimeRate + amount) + 2));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private static float getBounceIn(float elapsedTimeRate) {
        return 1f - getBounceOut(1f - elapsedTimeRate);
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private static float getBounceOut(float elapsedTimeRate) {
        if (elapsedTimeRate < 1 / 2.75) {
            return (float) (7.5625 * elapsedTimeRate * elapsedTimeRate);
        } else if (elapsedTimeRate < 2 / 2.75) {
            return (float) (7.5625 * (elapsedTimeRate -= 1.5 / 2.75) * elapsedTimeRate + 0.75);
        } else if (elapsedTimeRate < 2.5 / 2.75) {
            return (float) (7.5625 * (elapsedTimeRate -= 2.25 / 2.75) * elapsedTimeRate + 0.9375);
        } else {
            return (float) (7.5625 * (elapsedTimeRate -= 2.625 / 2.75) * elapsedTimeRate + 0.984375);
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private static float getElasticIn(float elapsedTimeRate, double amplitude, double period) {
        if (elapsedTimeRate == 0 || elapsedTimeRate == 1) return elapsedTimeRate;
        double pi2 = Math.PI * 2;
        double s = period / pi2 * Math.asin(1 / amplitude);
        return (float) -(amplitude * Math.pow(2f, 10f * (elapsedTimeRate -= 1f)) * Math.sin((elapsedTimeRate - s) * pi2 / period));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private static float getElasticOut(float elapsedTimeRate, double amplitude, double period) {
        if (elapsedTimeRate == 0 || elapsedTimeRate == 1) return elapsedTimeRate;

        double pi2 = Math.PI * 2;
        double s = period / pi2 * Math.asin(1 / amplitude);
        return (float) (amplitude * Math.pow(2, -10 * elapsedTimeRate) * Math.sin((elapsedTimeRate - s) * pi2 / period) + 1);
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private static float getElasticInOut(float elapsedTimeRate, double amplitude, double period) {
        double pi2 = Math.PI * 2;

        double s = period / pi2 * Math.asin(1 / amplitude);
        if ((elapsedTimeRate *= 2) < 1) {
            return (float) (-0.5f * (amplitude * Math.pow(2, 10 * (elapsedTimeRate -= 1f)) * Math.sin((elapsedTimeRate - s) * pi2 / period)));
        }
        return (float) (amplitude * Math.pow(2, -10 * (elapsedTimeRate -= 1)) * Math.sin((elapsedTimeRate - s) * pi2 / period) * 0.5 + 1);

    }
}
