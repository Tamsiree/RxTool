package com.vondear.rxtools.view.ticker;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vondear
 * In ticker, each character in the rendered text is represented by a {@link RxTickerColumn}. The
 * column can be seen as a column of text in which we can animate from one character to the next
 * by scrolling the column vertically. The {@link RxTickerColumnManager} is then a
 * manager/convenience class for handling a list of {@link RxTickerColumn} which then combines into
 * the entire string we are rendering.
 *
 */
class RxTickerColumnManager {
    final ArrayList<RxTickerColumn> mRxTickerColumns = new ArrayList<>();
    private final RxTickerDrawMetrics metrics;

    // The character list that dictates how to transition from one character to another.
    private char[] characterList;
    // A minor optimization so that we can cache the indices of each character.
    private Map<Character, Integer> characterIndicesMap;

    RxTickerColumnManager(RxTickerDrawMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * @see {@link RxTickerView#setCharacterList(char[])}.
     */
    void setCharacterList(char[] characterList) {
        this.characterList = characterList;
        this.characterIndicesMap = new HashMap<>(characterList.length);

        for (int i = 0; i < characterList.length; i++) {
            characterIndicesMap.put(characterList[i], i);
        }
    }

    /**
     * @return whether or not {@param text} should be debounced because it's the same as the
     *         current target text of this column manager.
     */
    boolean shouldDebounceText(char[] text) {
        final int newTextSize = text.length;
        if (newTextSize == mRxTickerColumns.size()) {
            for (int i = 0; i < newTextSize; i++) {
                if (text[i] != mRxTickerColumns.get(i).getTargetChar()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Tell the column manager the new target text that it should display.
     */
    void setText(char[] text) {
        if (characterList == null) {
            throw new IllegalStateException("Need to call setCharacterList(char[]) first.");
        }

        // First remove any zero-width columns
        for (int i = 0; i < mRxTickerColumns.size(); ) {
            final RxTickerColumn rxTickerColumn = mRxTickerColumns.get(i);
            if (rxTickerColumn.getCurrentWidth() > 0) {
                i++;
            } else {
                mRxTickerColumns.remove(i);
            }
        }

        // Use Levenshtein distance algorithm to figure out how to manipulate the columns
        final int[] actions = RxLevenshteinUtils.computeColumnActions(getCurrentText(), text);
        int columnIndex = 0;
        int textIndex = 0;
        for (int i = 0; i < actions.length; i++) {
            switch (actions[i]) {
                case RxLevenshteinUtils.ACTION_INSERT:
                    mRxTickerColumns.add(columnIndex,
                            new RxTickerColumn(characterList, characterIndicesMap, metrics));
                case RxLevenshteinUtils.ACTION_SAME:
                    mRxTickerColumns.get(columnIndex).setTargetChar(text[textIndex]);
                    columnIndex++;
                    textIndex++;
                    break;
                case RxLevenshteinUtils.ACTION_DELETE:
                    mRxTickerColumns.get(columnIndex).setTargetChar(RxTickerUtils.EMPTY_CHAR);
                    columnIndex++;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown action: " + actions[i]);
            }
        }
    }

    void onAnimationEnd() {
        for (int i = 0, size = mRxTickerColumns.size(); i < size; i++) {
            final RxTickerColumn column = mRxTickerColumns.get(i);
            column.onAnimationEnd();
        }
    }

    void setAnimationProgress(float animationProgress) {
        for (int i = 0, size = mRxTickerColumns.size(); i < size; i++) {
            final RxTickerColumn column = mRxTickerColumns.get(i);
            column.setAnimationProgress(animationProgress);
        }
    }

    float getMinimumRequiredWidth() {
        float width = 0f;
        for (int i = 0, size = mRxTickerColumns.size(); i < size; i++) {
            width += mRxTickerColumns.get(i).getMinimumRequiredWidth();
        }
        return width;
    }

    float getCurrentWidth() {
        float width = 0f;
        for (int i = 0, size = mRxTickerColumns.size(); i < size; i++) {
            width += mRxTickerColumns.get(i).getCurrentWidth();
        }
        return width;
    }

    char[] getCurrentText() {
        final int size = mRxTickerColumns.size();
        final char[] currentText = new char[size];
        for (int i = 0; i < size; i++) {
            currentText[i] = mRxTickerColumns.get(i).getCurrentChar();
        }
        return currentText;
    }

    /**
     * This method will draw onto the canvas the appropriate UI state of each column dictated
     * by {@param animationProgress}. As a side effect, this method will also translate the canvas
     * accordingly for the draw procedures.
     */
    void draw(Canvas canvas, Paint textPaint) {
        for (int i = 0, size = mRxTickerColumns.size(); i < size; i++) {
            final RxTickerColumn column = mRxTickerColumns.get(i);
            column.draw(canvas, textPaint);
            canvas.translate(column.getCurrentWidth(), 0f);
        }
    }
}
