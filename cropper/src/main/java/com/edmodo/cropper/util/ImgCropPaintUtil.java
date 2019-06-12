/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.edmodo.cropper.util;

import android.content.res.Resources;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.edmodo.cropper.R;

/**
 * Utility class for handling all of the Paint used to draw the CropOverlayView.
 */
public class ImgCropPaintUtil {

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Creates the Paint object for drawing the crop window border.
     */
    public static Paint newBorderPaint(float width, int color) {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(color);

        return paint;
    }

    /**
     * Creates the Paint object for drawing the crop window guidelines.
     */
    public static Paint newGuidelinePaint(float width, int color) {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(color);

        return paint;
    }

    /**
     * Creates the Paint object for drawing the translucent overlay outside the crop window.
     *
     * @return the new Paint object
     */
    public static Paint newSurroundingAreaOverlayPaint(int color) {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }

    /**
     * Creates the Paint object for drawing the corners of the border
     */
    public static Paint newCornerPaint(float width, int color) {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(color);
        return paint;
    }
}
