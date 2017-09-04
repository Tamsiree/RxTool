/*
Copyright 2014 David Morrissey

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

package com.vondear.tools.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.vondear.rxtools.view.scaleimage.RxScaleImageView;
import com.vondear.tools.R;


public class RxPinView extends RxScaleImageView {

    private PointF sPin;
    private Bitmap pin;

    public RxPinView(Context context) {
        this(context, null);
    }

    public RxPinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public PointF getPin() {
        return sPin;
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_blue);
        float w = (density / 420f) * pin.getWidth();
        float h = (density / 420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int) w, (int) h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (sPin != null && pin != null) {
            PointF vPin = sourceToViewCoord(sPin);
            float vX = vPin.x - (pin.getWidth() / 2);
            float vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
        }
    }
}
