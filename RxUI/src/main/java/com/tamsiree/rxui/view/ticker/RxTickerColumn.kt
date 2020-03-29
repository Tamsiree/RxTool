package com.tamsiree.rxui.view.ticker

import android.graphics.Canvas
import android.graphics.Paint

/**
 * @author tamsiree
 * Represents a column of characters to be drawn on the screen. This class primarily handles
 * animating within the column from one character to the next and drawing all of the intermediate
 * states.
 */
internal class RxTickerColumn(private val characterList: CharArray, private val characterIndicesMap: Map<Char, Int>,
                              private val metrics: RxTickerDrawMetrics) {
    var currentChar = RxTickerUtils.EMPTY_CHAR
        private set
    private var targetChar = RxTickerUtils.EMPTY_CHAR

    // The indices characters simply signify what positions are for the current and target
    // characters in the assigned characterList. This tells us how to animate from the current
    // to the target characters.
    private var startIndex = 0
    private var endIndex = 0

    // Drawing state variables that get updated whenever animation progress gets updated.
    private var bottomCharIndex = 0
    private var bottomDelta = 0f
    private var charHeight = 0f

    // Drawing state variables for handling size transition
    private var sourceWidth = 0f
    var currentWidth = 0f
        private set
    private var targetWidth = 0f
    var minimumRequiredWidth = 0f
        private set

    // The bottom delta variables signifies the vertical offset that the bottom drawn character
    // is seeing. If the delta is 0, it means that the character is perfectly centered. If the
    // delta is negative, it means that the bottom character is poking out from the bottom and
    // part of the top character is visible. The delta should never be positive because it means
    // that the bottom character is not actually the bottom character.
    private var currentBottomDelta = 0f
    private var previousBottomDelta = 0f
    private var directionAdjustment = 0

    /**
     * Tells the column that the next character it should show is {@param targetChar}. This can
     * change can either be animated or instant depending on the animation progress set by
     * [.setAnimationProgress].
     */
    fun setTargetChar(targetChar: Char) {
        // Set the current and target characters for the animation
        this.targetChar = targetChar
        sourceWidth = currentWidth
        targetWidth = metrics.getCharWidth(targetChar)
        minimumRequiredWidth = Math.max(sourceWidth, targetWidth)

        // Calculate the current indices
        setCharacterIndices()
        val scrollDown = endIndex >= startIndex
        directionAdjustment = if (scrollDown) 1 else -1

        // Save the currentBottomDelta as previousBottomDelta in case this call to setTargetChar
        // interrupted a previously running animation. The deltas will then be used to compute
        // offset so that the interruption feels smooth on the UI.
        previousBottomDelta = currentBottomDelta
        currentBottomDelta = 0f
    }

    fun getTargetChar(): Char {
        return targetChar
    }

    /**
     * A helper method for populating [.startIndex] and [.endIndex] given the
     * current and target characters for the animation.
     */
    private fun setCharacterIndices() {
        startIndex = (if (characterIndicesMap.containsKey(currentChar)) characterIndicesMap[currentChar] else UNKNOWN_START_INDEX)!!
        endIndex = (if (characterIndicesMap.containsKey(targetChar)) characterIndicesMap[targetChar] else UNKNOWN_END_INDEX)!!
    }

    fun onAnimationEnd() {
        minimumRequiredWidth = currentWidth
    }

    fun setAnimationProgress(animationProgress: Float) {
        if (animationProgress == 1f) {
            // Animation finished (or never started), set to stable state.
            currentChar = targetChar
            currentBottomDelta = 0f
            previousBottomDelta = 0f
        }
        val charHeight = metrics.charHeight

        // First let's find the total height of this column between the start and end chars.
        val totalHeight = charHeight * Math.abs(endIndex - startIndex)

        // The current base is then the part of the total height that we have progressed to
        // from the animation. For example, there might be 5 characters, each character is
        // 2px tall, so the totalHeight is 10. If we are at 50% progress, then our baseline
        // in this column is at 5 out of 10 (which is the 3rd character with a -50% offset
        // to the baseline).
        val currentBase = animationProgress * totalHeight

        // Given the current base, we now can find which character should drawn on the bottom.
        // Note that this position is a float. For example, if the bottomCharPosition is
        // 4.5, it means that the bottom character is the 4th character, and it has a -50%
        // offset relative to the baseline.
        val bottomCharPosition = currentBase / charHeight

        // By subtracting away the integer part of bottomCharPosition, we now have the
        // percentage representation of the bottom char's offset.
        val bottomCharOffsetPercentage = bottomCharPosition - bottomCharPosition.toInt()

        // We might have interrupted a previous animation if previousBottomDelta is not 0f.
        // If that's the case, we need to take this delta into account so that the previous
        // character offset won't be wiped away when we start a new animation.
        // We multiply by the inverse percentage so that the offset contribution from the delta
        // progresses along with the rest of the animation (from full delta to 0).
        val additionalDelta = previousBottomDelta * (1f - animationProgress)

        // Now, using the bottom char's offset percentage and the delta we have from the
        // previous animation, we can now compute what's the actual offset of the bottom
        // character in the column relative to the baseline.
        bottomDelta = (bottomCharOffsetPercentage * charHeight * directionAdjustment
                + additionalDelta)

        // Figure out what the actual character index is in the characterList, and then
        // draw the character with the computed offset.
        bottomCharIndex = startIndex + bottomCharPosition.toInt() * directionAdjustment
        this.charHeight = charHeight
        currentWidth = sourceWidth + (targetWidth - sourceWidth) * animationProgress
    }

    /**
     * Draw the current state of the column as it's animating from one character in the list
     * to another. This method will take into account various factors such as animation
     * progress and the previously interrupted animation state to render the characters
     * in the correct position on the canvas.
     */
    fun draw(canvas: Canvas, textPaint: Paint) {
        if (drawText(canvas, textPaint, characterList, bottomCharIndex, bottomDelta)) {
            // Save the current drawing state in case our animation gets interrupted
            if (bottomCharIndex >= 0) {
                currentChar = characterList[bottomCharIndex]
            } else if (bottomCharIndex == UNKNOWN_END_INDEX) {
                currentChar = targetChar
            }
            currentBottomDelta = bottomDelta
        }

        // Draw the corresponding top and bottom characters if applicable
        drawText(canvas, textPaint, characterList, bottomCharIndex + 1,
                bottomDelta - charHeight)
        // Drawing the bottom character here might seem counter-intuitive because we've been
        // computing for the bottom character this entire time. But the bottom character
        // computed above might actually be above the baseline if we interrupted a previous
        // animation that gave us a positive additionalDelta.
        drawText(canvas, textPaint, characterList, bottomCharIndex - 1,
                bottomDelta + charHeight)
    }

    /**
     * @return whether the text was successfully drawn on the canvas
     */
    private fun drawText(canvas: Canvas, textPaint: Paint, characterList: CharArray, index: Int,
                         verticalOffset: Float): Boolean {
        if (index >= 0 && index < characterList.size) {
            canvas.drawText(characterList, index, 1, 0f, verticalOffset, textPaint)
            return true
        } else if (startIndex == UNKNOWN_START_INDEX && index == UNKNOWN_START_INDEX) {
            canvas.drawText(Character.toString(currentChar), 0, 1, 0f, verticalOffset, textPaint)
            return true
        } else if (endIndex == UNKNOWN_END_INDEX && index == UNKNOWN_END_INDEX) {
            canvas.drawText(Character.toString(targetChar), 0, 1, 0f, verticalOffset, textPaint)
            return true
        }
        return false
    }

    companion object {
        private const val UNKNOWN_START_INDEX = -1
        private const val UNKNOWN_END_INDEX = -2
    }

}