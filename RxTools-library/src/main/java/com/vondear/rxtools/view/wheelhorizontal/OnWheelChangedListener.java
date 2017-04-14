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


/**
 * Wheel changed listener interface.
 * <p>The onChanged() method is called whenever current spinnerwheel positions is changed:
 * <li> New Wheel position is set
 * <li> Wheel view is scrolled
 */
public interface OnWheelChangedListener {
	/**
	 * Callback method to be invoked when current item changed
	 * @param wheel the spinnerwheel view whose state has changed
	 * @param oldValue the old value of current item
	 * @param newValue the new value of current item
	 */
	void onChanged(AbstractWheel wheel, int oldValue, int newValue);
}
