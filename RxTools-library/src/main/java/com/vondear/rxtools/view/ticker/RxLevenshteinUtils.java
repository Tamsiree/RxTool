package com.vondear.rxtools.view.ticker;

/**
 * @author vondear
 * https://en.wikipedia.org/wiki/Levenshtein_distance
 */
public class RxLevenshteinUtils {
    static final int ACTION_SAME = 0;
    static final int ACTION_INSERT = 1;
    static final int ACTION_DELETE = 2;

    /**
     * Run a slightly modified version of Levenshtein distance algorithm to compute the minimum
     * edit distance between the current and the target text. Unlike the traditional algorithm,
     * we force return all {@link #ACTION_SAME} for inputs that are the same length (so optimize
     * update over insertion/deletion).
     *
     * @param source the source character array
     * @param target the target character array
     * @return an int array of size min(source.length, target.length) where each index
     *         corresponds to one of {@link #ACTION_SAME}, {@link #ACTION_INSERT},
     *         {@link #ACTION_DELETE} to represent if we update, insert, or delete a character
     *         at the particular index.
     */
    public static int[] computeColumnActions(char[] source, char[] target) {
        final int sourceLength = source.length;
        final int targetLength = target.length;
        final int resultLength = Math.max(sourceLength, targetLength);

        final int[] result = new int[resultLength];

        if (sourceLength == targetLength) {
            // No modifications needed if the length of the strings are the same
            return result;
        }

        final int numRows = sourceLength + 1;
        final int numCols = targetLength + 1;

        // Compute the Levenshtein matrix
        final int[][] matrix = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j < numCols; j++) {
            matrix[0][j] = j;
        }

        int cost;
        for (int j = 1; j < numCols; j++) {
            for (int i = 1; i < numRows; i++) {
                cost = source[i-1] == target[j-1] ? 0 : 1;

                matrix[i][j] = min(
                        matrix[i-1][j] + 1,
                        matrix[i][j-1] + 1,
                        matrix[i-1][j-1] + cost);
            }
        }

        // Reverse trace the matrix to compute the necessary actions
        int i = numRows - 1;
        int j = numCols - 1;
        int resultIndex = resultLength - 1;
        while (resultIndex >= 0) {
            if (i == 0) {
                // At the top row, can only move left, meaning insert column
                result[resultIndex] = ACTION_INSERT;
                j--;
            } else if (j == 0) {
                // At the left column, can only move up, meaning delete column
                result[resultIndex] = ACTION_DELETE;
                i--;
            } else {
                final int top = matrix[i-1][j];
                final int left = matrix[i][j-1];
                final int topLeft = matrix[i-1][j-1];

                if (topLeft <= top && topLeft <= left) {
                    result[resultIndex] = ACTION_SAME;
                    i--;
                    j--;
                } else if (top <= left) {
                    result[resultIndex] = ACTION_DELETE;
                    i--;
                } else {
                    result[resultIndex] = ACTION_INSERT;
                    j--;
                }
            }
            resultIndex--;
        }

        return result;
    }

    private static int min(int first, int second, int third) {
        return Math.min(first, Math.min(second, third));
    }
}
