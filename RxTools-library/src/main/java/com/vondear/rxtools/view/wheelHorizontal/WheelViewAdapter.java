/*
 * android-spinnerwheel
 * https://github.com/ai212983/android-spinnerwheel
 *
 * based on
 *
 * Android Wheel Control.
 * https://code.google.com/p/android-wheel/
 *
 * Copyright 2011 Yuri Kanivets
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

package com.vondear.rxtools.view.wheelhorizontal;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wheel items adapter interface
 */
public interface WheelViewAdapter {
    /**
     * Gets items count
     * @return the count of spinnerwheel items
     */
    public int getItemsCount();

    /**
     * Get a View that displays the data at the specified position in the data set
     *
     * @param index the item index
     * @param convertView the old view to reuse if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the spinnerwheel item View
     */
    public View getItem(int index, View convertView, ViewGroup parent);

    /**
     * Get a View that displays an empty spinnerwheel item placed before the first or after
     * the last spinnerwheel item.
     *
     * @param convertView the old view to reuse if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the empty item View
     */
    public View getEmptyItem(View convertView, ViewGroup parent);

    /**
     * Register an observer that is called when changes happen to the data used by this adapter.
     * @param observer the observer to be registered
     */
    public void registerDataSetObserver(DataSetObserver observer);

    /**
     * Unregister an observer that has previously been registered
     * @param observer the observer to be unregistered
     */
    void unregisterDataSetObserver(DataSetObserver observer);
}
