package com.tamsiree.rxui.view.ticker

/**
 * @author tamsiree
 * Static utility class for the ticker package. This class contains helper methods such as those
 * that generate default character lists to use for animation.
 */
object RxTickerUtils {
    const val EMPTY_CHAR = 0.toChar()// Special case EMPTY_CHAR and '.' <-> '/'

    /**
     * @return a default ordering for all viewable ASCII characters. This list also special cases
     * space to be in front of the 0 and swap '.' with '/'. These special cases make
     * typical US currency animations intuitive.
     */
    val defaultListForUSCurrency: CharArray
        get() {
            val indexOf0 = '0'.toInt()
            val indexOfPeriod = '.'.toInt()
            val indexOfSlash = '/'.toInt()
            val start = 33
            val end = 256
            val charList = CharArray(end - start + 1)
            for (i in start until indexOf0) {
                charList[i - start] = i.toChar()
            }

            // Special case EMPTY_CHAR and '.' <-> '/'
            charList[indexOf0 - start] = EMPTY_CHAR
            charList[indexOfPeriod - start] = '/'
            charList[indexOfSlash - start] = '.'
            for (i in indexOf0 + 1 until end + 1) {
                charList[i - start] = (i - 1).toChar()
            }
            return charList
        }

    /**
     * @return a default ordering for numbers (including periods).
     */
    val defaultNumberList: CharArray
        get() {
            val charList = CharArray(11)
            charList[0] = EMPTY_CHAR
            for (i in 0..9) {
                charList[i + 1] = (i + 48).toChar()
            }
            return charList
        }
}