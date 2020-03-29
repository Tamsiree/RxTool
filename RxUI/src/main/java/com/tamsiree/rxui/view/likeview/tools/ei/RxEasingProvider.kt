package com.tamsiree.rxui.view.likeview.tools.ei

/**
 * @author tamsiree
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
 */
internal object RxEasingProvider {
    /**
     * @param rxEase          Easing type
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    operator fun get(rxEase: RxEase, elapsedTimeRate: Float): Float {
        var elapsedTimeRate = elapsedTimeRate
        return when (rxEase) {
            RxEase.LINEAR -> elapsedTimeRate
            RxEase.QUAD_IN -> getPowIn(elapsedTimeRate, 2.0)
            RxEase.QUAD_OUT -> getPowOut(elapsedTimeRate, 2.0)
            RxEase.QUAD_IN_OUT -> getPowInOut(elapsedTimeRate, 2.0)
            RxEase.CUBIC_IN -> getPowIn(elapsedTimeRate, 3.0)
            RxEase.CUBIC_OUT -> getPowOut(elapsedTimeRate, 3.0)
            RxEase.CUBIC_IN_OUT -> getPowInOut(elapsedTimeRate, 3.0)
            RxEase.QUART_IN -> getPowIn(elapsedTimeRate, 4.0)
            RxEase.QUART_OUT -> getPowOut(elapsedTimeRate, 4.0)
            RxEase.QUART_IN_OUT -> getPowInOut(elapsedTimeRate, 4.0)
            RxEase.QUINT_IN -> getPowIn(elapsedTimeRate, 5.0)
            RxEase.QUINT_OUT -> getPowOut(elapsedTimeRate, 5.0)
            RxEase.QUINT_IN_OUT -> getPowInOut(elapsedTimeRate, 5.0)
            RxEase.SINE_IN -> (1f - Math.cos(elapsedTimeRate * Math.PI / 2f)).toFloat()
            RxEase.SINE_OUT -> Math.sin(elapsedTimeRate * Math.PI / 2f).toFloat()
            RxEase.SINE_IN_OUT -> (-0.5f * (Math.cos(Math.PI * elapsedTimeRate) - 1f)).toFloat()
            RxEase.BACK_IN -> (elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate - 1.7)).toFloat()
            RxEase.BACK_OUT -> (--elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate + 1.7) + 1f).toFloat()
            RxEase.BACK_IN_OUT -> getBackInOut(elapsedTimeRate, 1.7f)
            RxEase.CIRC_IN -> (-(Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate.toDouble()) - 1)).toFloat()
            RxEase.CIRC_OUT -> Math.sqrt(1f - --elapsedTimeRate * elapsedTimeRate.toDouble()).toFloat()
            RxEase.CIRC_IN_OUT -> {
                if (2f.let { elapsedTimeRate *= it; elapsedTimeRate } < 1f) {
                    (-0.5f * (Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate.toDouble()) - 1f)).toFloat()
                } else (0.5f * (Math.sqrt(1f - 2f.let { elapsedTimeRate -= it; elapsedTimeRate } * elapsedTimeRate.toDouble()) + 1f)).toFloat()
            }
            RxEase.BOUNCE_IN -> getBounceIn(elapsedTimeRate)
            RxEase.BOUNCE_OUT -> getBounceOut(elapsedTimeRate)
            RxEase.BOUNCE_IN_OUT -> {
                if (elapsedTimeRate < 0.5f) {
                    getBounceIn(elapsedTimeRate * 2f) * 0.5f
                } else getBounceOut(elapsedTimeRate * 2f - 1f) * 0.5f + 0.5f
            }
            RxEase.ELASTIC_IN -> getElasticIn(elapsedTimeRate, 1.0, 0.3)
            RxEase.ELASTIC_OUT -> getElasticOut(elapsedTimeRate, 1.0, 0.3)
            RxEase.ELASTIC_IN_OUT -> getElasticInOut(elapsedTimeRate, 1.0, 0.45)
            else -> elapsedTimeRate
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private fun getPowIn(elapsedTimeRate: Float, pow: Double): Float {
        return Math.pow(elapsedTimeRate.toDouble(), pow).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private fun getPowOut(elapsedTimeRate: Float, pow: Double): Float {
        return (1.toFloat() - Math.pow(1 - elapsedTimeRate.toDouble(), pow)).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private fun getPowInOut(elapsedTimeRate: Float, pow: Double): Float {
        var elapsedTimeRate = elapsedTimeRate
        return if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
            (0.5 * Math.pow(elapsedTimeRate.toDouble(), pow)).toFloat()
        } else (1 - 0.5 * Math.abs(Math.pow(2 - elapsedTimeRate.toDouble(), pow))).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amount          amount The strength of the ease.
     * @return easedValue
     */
    private fun getBackInOut(elapsedTimeRate: Float, amount: Float): Float {
        var elapsedTimeRate = elapsedTimeRate
        var amount = amount
        amount *= 1.525f
        return if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
            (0.5 * (elapsedTimeRate * elapsedTimeRate * ((amount + 1) * elapsedTimeRate - amount))).toFloat()
        } else (0.5 * (2.let { elapsedTimeRate -= it; elapsedTimeRate } * elapsedTimeRate * ((amount + 1) * elapsedTimeRate + amount) + 2)).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private fun getBounceIn(elapsedTimeRate: Float): Float {
        return 1f - getBounceOut(1f - elapsedTimeRate)
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private fun getBounceOut(elapsedTimeRate: Float): Float {
        var elapsedTimeRate = elapsedTimeRate
        return if (elapsedTimeRate < 1 / 2.75) {
            7.5625f * elapsedTimeRate * elapsedTimeRate
        } else if (elapsedTimeRate < 2 / 2.75) {
            7.5625f * (1.5f / 2.75f.let { elapsedTimeRate -= it; elapsedTimeRate }) * elapsedTimeRate + 0.75f
        } else if (elapsedTimeRate < 2.5 / 2.75) {
            7.5625f * (2.25f / 2.75f.let { elapsedTimeRate -= it; elapsedTimeRate }) * elapsedTimeRate + 0.9375f
        } else {
            7.5625f * (2.625f / 2.75f.let { elapsedTimeRate -= it; elapsedTimeRate }) * elapsedTimeRate + 0.984375f
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private fun getElasticIn(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        var elapsedTimeRate = elapsedTimeRate
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f) return elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return (-(amplitude * Math.pow(2.0, 10f * 1f.let { elapsedTimeRate -= it; elapsedTimeRate }.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period))).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private fun getElasticOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f) return elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return (amplitude * Math.pow(2.0, -10 * elapsedTimeRate.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period) + 1).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private fun getElasticInOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        var elapsedTimeRate = elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
            (-0.5f * (amplitude * Math.pow(2.0, 10 * 1f.let { elapsedTimeRate -= it; elapsedTimeRate }.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period))).toFloat()
        } else (amplitude * Math.pow(2.0, -10 * 1.let { elapsedTimeRate -= it; elapsedTimeRate }.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period) * 0.5 + 1).toFloat()
    }
}