/*
Copyright 2016 Tomer Goldstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.vondear.rxtools.view.tooltips;

import android.view.View;

public class RxCoordinates {

    int left;
    int top;
    int right;
    int bottom;

    public RxCoordinates(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        left = location[0];
        right = left + view.getWidth();;
        top = location[1];
        bottom = top + view.getHeight();
    }


}
