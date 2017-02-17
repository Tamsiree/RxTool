/**
 * Copyright (C) 2016 Robinhood Markets, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vondear.rxtools.view.ticker;

/**
 * Static utility class for the ticker package. This class contains helper methods such as those
 * that generate default character lists to use for animation.
 *
 * @author Jin Cao, Robinhood
 */
public class RxTickerUtils {
    public static final char EMPTY_CHAR = (char) 0;

    /**
     * @return a default ordering for all viewable ASCII characters. This list also special cases
     *         space to be in front of the 0 and swap '.' with '/'. These special cases make
     *         typical US currency animations intuitive.
     */
    public static char[] getDefaultListForUSCurrency() {
        final int indexOf0 = (int) '0';
        final int indexOfPeriod = (int) '.';
        final int indexOfSlash = (int) '/';

        final int start = 33;
        final int end = 256;

        final char[] charList = new char[end - start + 1];
        for (int i = start; i < indexOf0; i++) {
            charList[i - start] = (char) i;
        }

        // Special case EMPTY_CHAR and '.' <-> '/'
        charList[indexOf0 - start] = EMPTY_CHAR;
        charList[indexOfPeriod - start] = '/';
        charList[indexOfSlash - start] = '.';

        for (int i = indexOf0 + 1; i < end + 1; i++) {
            charList[i - start] = (char) (i - 1);
        }

        return charList;
    }

    /**
     * @return a default ordering for numbers (including periods).
     */
    public static char[] getDefaultNumberList() {
        final char[] charList = new char[11];
        charList[0] = EMPTY_CHAR;
        for (int i = 0; i < 10; i++) {
            charList[i + 1] = (char) (i + 48);
        }
        return charList;
    }
}
