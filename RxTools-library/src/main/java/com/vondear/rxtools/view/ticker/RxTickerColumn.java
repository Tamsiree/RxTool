package com.vondear.rxtools.view.ticker;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Map;

/**
 * @author vondear
 * Represents a column of characters to be drawn on the screen. This class primarily handles
 * animating within the column from one character to the next and drawing all of the intermediate
 * states.
 */
class RxTickerColumn {
    private static final int UNKNOWN_START_INDEX = -1;
    private static final int UNKNOWN_END_INDEX = -2;

    private final char[] characterList;
    private final Map<Character, Integer> characterIndicesMap;
    private final RxTickerDrawMetrics metrics;

    private char currentChar = RxTickerUtils.EMPTY_CHAR;
    private char targetChar = RxTickerUtils.EMPTY_CHAR;

    // The indices characters simply signify what positions are for the current and target
    // characters in the assigned characterList. This tells us how to animate from the current
    // to the target characters.
    private int startIndex;
    private int endIndex;

    // Drawing state variables that get updated whenever animation progress gets updated.
    private int bottomCharIndex;
    private float bottomDelta;
    private float charHeight;

    // Drawing state variables for handling size transition
    private float sourceWidth, currentWidth, targetWidth, minimumRequiredWidth;

    // The bottom delta variables signifies the vertical offset that the bottom drawn character
    // is seeing. If the delta is 0, it means that the character is perfectly centered. If the
    // delta is negative, it means that the bottom character is poking out from the bottom and
    // part of the top character is visible. The delta should never be positive because it means
    // that the bottom character is not actually the bottom character.
    private float currentBottomDelta;
    private float previousBottomDelta;
    private int directionAdjustment;

    RxTickerColumn(char[] characterList, Map<Character, Integer> characterIndicesMap,
                   RxTickerDrawMetrics metrics) {
        this.characterList = characterList;
        this.characterIndicesMap = characterIndicesMap;
        this.metrics = metrics;
    }

    /**
     * Tells the column that the next character it should show is {@param targetChar}. This can
     * change can either be animated or instant depending on the animation progress set by
     * {@link #setAnimationProgress(float)}.
     */
    void setTargetChar(char targetChar) {
        // Set the current and target characters for the animation
        this.targetChar = targetChar;
        this.sourceWidth = this.currentWidth;
        this.targetWidth = metrics.getCharWidth(targetChar);
        this.minimumRequiredWidth = Math.max(this.sourceWidth, this.targetWidth);

        // Calculate the current indices
        setCharacterIndices();

        final boolean scrollDown = endIndex >= startIndex;
        directionAdjustment = scrollDown ? 1 : -1;

        // Save the currentBottomDelta as previousBottomDelta in case this call to setTargetChar
        // interrupted a previously running animation. The deltas will then be used to compute
        // offset so that the interruption feels smooth on the UI.
        previousBottomDelta = currentBottomDelta;
        currentBottomDelta = 0f;
    }

    char getCurrentChar() {
        return currentChar;
    }

    char getTargetChar() {
        return targetChar;
    }

    float getCurrentWidth() {
        return currentWidth;
    }

    float getMinimumRequiredWidth() {
        return minimumRequiredWidth;
    }

    /**
     * A helper method for populating {@link #startIndex} and {@link #endIndex} given the
     * current and target characters for the animation.
     */
    private void setCharacterIndices() {
        startIndex = characterIndicesMap.containsKey(currentChar)
                ? characterIndicesMap.get(currentChar) : UNKNOWN_START_INDEX;
        endIndex = characterIndicesMap.containsKey(targetChar)
                ? characterIndicesMap.get(targetChar) : UNKNOWN_END_INDEX;
    }

    void onAnimationEnd() {
        minimumRequiredWidth = currentWidth;
    }

    void setAnimationProgress(float animationProgress) {
        if (animationProgress == 1f) {
            // Animation finished (or never started), set to stable state.
            this.currentChar = this.targetChar;
            currentBottomDelta = 0f;
            previousBottomDelta = 0f;
        }

        final float charHeight = metrics.getCharHeight();

        // First let's find the total height of this column between the start and end chars.
        final float totalHeight = charHeight * Math.abs(endIndex - startIndex);

        // The current base is then the part of the total height that we have progressed to
        // from the animation. For example, there might be 5 characters, each character is
        // 2px tall, so the totalHeight is 10. If we are at 50% progress, then our baseline
        // in this column is at 5 out of 10 (which is the 3rd character with a -50% offset
        // to the baseline).
        final float currentBase = animationProgress * totalHeight;

        // Given the current base, we now can find which character should drawn on the bottom.
        // Note that this position is a float. For example, if the bottomCharPosition is
        // 4.5, it means that the bottom character is the 4th character, and it has a -50%
        // offset relative to the baseline.
        final float bottomCharPosition = currentBase / charHeight;

        // By subtracting away the integer part of bottomCharPosition, we now have the
        // percentage representation of the bottom char's offset.
        final float bottomCharOffsetPercentage = bottomCharPosition - (int) bottomCharPosition;

        // We might have interrupted a previous animation if previousBottomDelta is not 0f.
        // If that's the case, we need to take this delta into account so that the previous
        // character offset won't be wiped away when we start a new animation.
        // We multiply by the inverse percentage so that the offset contribution from the delta
        // progresses along with the rest of the animation (from full delta to 0).
        final float additionalDelta = previousBottomDelta * (1f - animationProgress);

        // Now, using the bottom char's offset percentage and the delta we have from the
        // previous animation, we can now compute what's the actual offset of the bottom
        // character in the column relative to the baseline.
        bottomDelta = bottomCharOffsetPercentage * charHeight * directionAdjustment
                + additionalDelta;

        // Figure out what the actual character index is in the characterList, and then
        // draw the character with the computed offset.
        bottomCharIndex = startIndex + ((int) bottomCharPosition * directionAdjustment);

        this.charHeight = charHeight;
        this.currentWidth = sourceWidth + (targetWidth - sourceWidth) * animationProgress;
    }

    /**
     * Draw the current state of the column as it's animating from one character in the list
     * to another. This method will take into account various factors such as animation
     * progress and the previously interrupted animation state to render the characters
     * in the correct position on the canvas.
     */
    void draw(Canvas canvas, Paint textPaint) {
        if (drawText(canvas, textPaint, characterList, bottomCharIndex, bottomDelta)) {
            // Save the current drawing state in case our animation gets interrupted
            if (bottomCharIndex >= 0) {
                currentChar = characterList[bottomCharIndex];
            } else if (bottomCharIndex == UNKNOWN_END_INDEX) {
                currentChar = targetChar;
            }
            currentBottomDelta = bottomDelta;
        }

        // Draw the corresponding top and bottom characters if applicable
        drawText(canvas, textPaint, characterList, bottomCharIndex + 1,
                bottomDelta - charHeight);
        // Drawing the bottom character here might seem counter-intuitive because we've been
        // computing for the bottom character this entire time. But the bottom character
        // computed above might actually be above the baseline if we interrupted a previous
        // animation that gave us a positive additionalDelta.
        drawText(canvas, textPaint, characterList, bottomCharIndex - 1,
                bottomDelta + charHeight);
    }

    /**
     * @return whether the text was successfully drawn on the canvas
     */
    private boolean drawText(Canvas canvas, Paint textPaint, char[] characterList, int index,
            float verticalOffset) {
        if (index >= 0 && index < characterList.length) {
            canvas.drawText(characterList, index, 1, 0f, verticalOffset, textPaint);
            return true;
        } else if (startIndex == UNKNOWN_START_INDEX && index == UNKNOWN_START_INDEX) {
            canvas.drawText(Character.toString(currentChar), 0, 1, 0f, verticalOffset, textPaint);
            return true;
        } else if (endIndex == UNKNOWN_END_INDEX && index == UNKNOWN_END_INDEX) {
            canvas.drawText(Character.toString(targetChar), 0, 1, 0f, verticalOffset, textPaint);
            return true;
        }
        return false;
    }
}
