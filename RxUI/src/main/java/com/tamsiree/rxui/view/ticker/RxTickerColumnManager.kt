package com.tamsiree.rxui.view.ticker

import android.graphics.Canvas
import android.graphics.Paint
import com.tamsiree.rxui.view.ticker.RxLevenshteinUtils.computeColumnActions
import java.util.*

/**
 * @author tamsiree
 * In ticker, each character in the rendered text is represented by a [RxTickerColumn]. The
 * column can be seen as a column of text in which we can animate from one character to the next
 * by scrolling the column vertically. The [RxTickerColumnManager] is then a
 * manager/convenience class for handling a list of [RxTickerColumn] which then combines into
 * the entire string we are rendering.
 */
internal class RxTickerColumnManager(private val metrics: RxTickerDrawMetrics) {
    val mRxTickerColumns = ArrayList<RxTickerColumn>()

    // The character list that dictates how to transition from one character to another.
    private var characterList: CharArray? = null

    // A minor optimization so that we can cache the indices of each character.
    private var characterIndicesMap: MutableMap<Char, Int>? = null

    /**
     * @see {@link RxTickerView.setCharacterList
     */
    fun setCharacterList(characterList: CharArray) {
        this.characterList = characterList
        characterIndicesMap = HashMap(characterList.size)
        for (i in characterList.indices) {
            characterIndicesMap!![characterList[i]] = i
        }
    }

    /**
     * @return whether or not {@param text} should be debounced because it's the same as the
     * current target text of this column manager.
     */
    fun shouldDebounceText(text: CharArray): Boolean {
        val newTextSize = text.size
        if (newTextSize == mRxTickerColumns.size) {
            for (i in 0 until newTextSize) {
                if (text[i] != mRxTickerColumns[i].getTargetChar()) {
                    return false
                }
            }
            return true
        }
        return false
    }

    /**
     * Tell the column manager the new target text that it should display.
     */
    fun setText(text: CharArray) {
        checkNotNull(characterList) { "Need to call setCharacterList(char[]) first." }

        // First remove any zero-width columns
        run {
            var i = 0
            while (i < mRxTickerColumns.size) {
                val rxTickerColumn = mRxTickerColumns[i]
                if (rxTickerColumn.currentWidth > 0) {
                    i++
                } else {
                    mRxTickerColumns.removeAt(i)
                }
            }
        }

        // Use Levenshtein distance algorithm to figure out how to manipulate the columns
        val actions = computeColumnActions(currentText, text)
        var columnIndex = 0
        var textIndex = 0
        for (i in actions.indices) {
            when (actions[i]) {
                RxLevenshteinUtils.ACTION_INSERT -> {
                    mRxTickerColumns.add(columnIndex,
                            RxTickerColumn(characterList!!, characterIndicesMap!!, metrics))
                    mRxTickerColumns[columnIndex].setTargetChar(text[textIndex])
                    columnIndex++
                    textIndex++
                }
                RxLevenshteinUtils.ACTION_SAME -> {
                    mRxTickerColumns[columnIndex].setTargetChar(text[textIndex])
                    columnIndex++
                    textIndex++
                }
                RxLevenshteinUtils.ACTION_DELETE -> {
                    mRxTickerColumns[columnIndex].setTargetChar(RxTickerUtils.EMPTY_CHAR)
                    columnIndex++
                }
                else -> throw IllegalArgumentException("Unknown action: " + actions[i])
            }
        }
    }

    fun onAnimationEnd() {
        var i = 0
        val size = mRxTickerColumns.size
        while (i < size) {
            val column = mRxTickerColumns[i]
            column.onAnimationEnd()
            i++
        }
    }

    fun setAnimationProgress(animationProgress: Float) {
        var i = 0
        val size = mRxTickerColumns.size
        while (i < size) {
            val column = mRxTickerColumns[i]
            column.setAnimationProgress(animationProgress)
            i++
        }
    }

    val minimumRequiredWidth: Float
        get() {
            var width = 0f
            var i = 0
            val size = mRxTickerColumns.size
            while (i < size) {
                width += mRxTickerColumns[i].minimumRequiredWidth
                i++
            }
            return width
        }

    val currentWidth: Float
        get() {
            var width = 0f
            var i = 0
            val size = mRxTickerColumns.size
            while (i < size) {
                width += mRxTickerColumns[i].currentWidth
                i++
            }
            return width
        }

    val currentText: CharArray
        get() {
            val size = mRxTickerColumns.size
            val currentText = CharArray(size)
            for (i in 0 until size) {
                currentText[i] = mRxTickerColumns[i].currentChar
            }
            return currentText
        }

    /**
     * This method will draw onto the canvas the appropriate UI state of each column dictated
     * by {@param animationProgress}. As a side effect, this method will also translate the canvas
     * accordingly for the draw procedures.
     */
    fun draw(canvas: Canvas, textPaint: Paint?) {
        var i = 0
        val size = mRxTickerColumns.size
        while (i < size) {
            val column = mRxTickerColumns[i]
            column.draw(canvas, textPaint!!)
            canvas.translate(column.currentWidth, 0f)
            i++
        }
    }

}