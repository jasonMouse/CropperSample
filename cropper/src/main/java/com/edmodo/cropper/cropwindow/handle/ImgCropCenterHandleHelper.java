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

package com.edmodo.cropper.cropwindow.handle;

import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.edmodo.cropper.cropwindow.edge.ImgCropEdge;

/**
 * ImgCropHandleHelper class to handle the center handle.
 */
class ImgCropCenterHandleHelper extends ImgCropHandleHelper {

    // Constructor /////////////////////////////////////////////////////////////////////////////////

    ImgCropCenterHandleHelper() {
        super(null, null);
    }

    // ImgCropHandleHelper Methods ////////////////////////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          @NonNull RectF imageRect,
                          float snapRadius) {

        float left = ImgCropEdge.LEFT.getCoordinate();
        float top = ImgCropEdge.TOP.getCoordinate();
        float right = ImgCropEdge.RIGHT.getCoordinate();
        float bottom = ImgCropEdge.BOTTOM.getCoordinate();

        final float currentCenterX = (left + right) / 2;
        final float currentCenterY = (top + bottom) / 2;

        final float offsetX = x - currentCenterX;
        final float offsetY = y - currentCenterY;

        // Adjust the crop window.
        ImgCropEdge.LEFT.offset(offsetX);
        ImgCropEdge.TOP.offset(offsetY);
        ImgCropEdge.RIGHT.offset(offsetX);
        ImgCropEdge.BOTTOM.offset(offsetY);

        // Check if we have gone out of bounds on the sides, and fix.
        if (ImgCropEdge.LEFT.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = ImgCropEdge.LEFT.snapToRect(imageRect);
            ImgCropEdge.RIGHT.offset(offset);
        } else if (ImgCropEdge.RIGHT.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = ImgCropEdge.RIGHT.snapToRect(imageRect);
            ImgCropEdge.LEFT.offset(offset);
        }

        // Check if we have gone out of bounds on the top or bottom, and fix.
        if (ImgCropEdge.TOP.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = ImgCropEdge.TOP.snapToRect(imageRect);
            ImgCropEdge.BOTTOM.offset(offset);
        } else if (ImgCropEdge.BOTTOM.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = ImgCropEdge.BOTTOM.snapToRect(imageRect);
            ImgCropEdge.TOP.offset(offset);
        }
    }

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          @NonNull RectF imageRect,
                          float snapRadius) {

        updateCropWindow(x, y, imageRect, snapRadius);
    }
}
