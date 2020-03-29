package com.tamsiree.rxui.view.ticker

/**
 * @author tamsiree
 * https://en.wikipedia.org/wiki/Levenshtein_distance
 */
object RxLevenshteinUtils {
    const val ACTION_SAME = 0
    const val ACTION_INSERT = 1
    const val ACTION_DELETE = 2

    /**
     * Run a slightly modified version of Levenshtein distance algorithm to compute the minimum
     * edit distance between the current and the target text. Unlike the traditional algorithm,
     * we force return all [.ACTION_SAME] for inputs that are the same length (so optimize
     * update over insertion/deletion).
     *
     * @param source the source character array
     * @param target the target character array
     * @return an int array of size min(source.length, target.length) where each index
     * corresponds to one of [.ACTION_SAME], [.ACTION_INSERT],
     * [.ACTION_DELETE] to represent if we update, insert, or delete a character
     * at the particular index.
     */
    @JvmStatic
    fun computeColumnActions(source: CharArray, target: CharArray): IntArray {
        val sourceLength = source.size
        val targetLength = target.size
        val resultLength = Math.max(sourceLength, targetLength)
        val result = IntArray(resultLength)
        if (sourceLength == targetLength) {
            // No modifications needed if the length of the strings are the same
            return result
        }
        val numRows = sourceLength + 1
        val numCols = targetLength + 1

        // Compute the Levenshtein matrix
        val matrix = Array(numRows) { IntArray(numCols) }
        for (i in 0 until numRows) {
            matrix[i][0] = i
        }
        for (j in 0 until numCols) {
            matrix[0][j] = j
        }
        var cost: Int
        for (j in 1 until numCols) {
            for (i in 1 until numRows) {
                cost = if (source[i - 1] == target[j - 1]) 0 else 1
                matrix[i][j] = min(
                        matrix[i - 1][j] + 1,
                        matrix[i][j - 1] + 1,
                        matrix[i - 1][j - 1] + cost)
            }
        }

        // Reverse trace the matrix to compute the necessary actions
        var i = numRows - 1
        var j = numCols - 1
        var resultIndex = resultLength - 1
        while (resultIndex >= 0) {
            if (i == 0) {
                // At the top row, can only move left, meaning insert column
                result[resultIndex] = ACTION_INSERT
                j--
            } else if (j == 0) {
                // At the left column, can only move up, meaning delete column
                result[resultIndex] = ACTION_DELETE
                i--
            } else {
                val top = matrix[i - 1][j]
                val left = matrix[i][j - 1]
                val topLeft = matrix[i - 1][j - 1]
                if (topLeft <= top && topLeft <= left) {
                    result[resultIndex] = ACTION_SAME
                    i--
                    j--
                } else if (top <= left) {
                    result[resultIndex] = ACTION_DELETE
                    i--
                } else {
                    result[resultIndex] = ACTION_INSERT
                    j--
                }
            }
            resultIndex--
        }
        return result
    }

    private fun min(first: Int, second: Int, third: Int): Int {
        return Math.min(first, Math.min(second, third))
    }
}