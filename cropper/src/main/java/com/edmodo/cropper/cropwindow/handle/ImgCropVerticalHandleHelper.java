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
import com.edmodo.cropper.util.ImgCropAspectRatioUtil;

/**
 * ImgCropHandleHelper class to handle vertical handles (i.e. left and right handles).
 */
class ImgCropVerticalHandleHelper extends ImgCropHandleHelper {

    // Member Variables ////////////////////////////////////////////////////////////////////////////

    private ImgCropEdge mEdge;

    // Constructor /////////////////////////////////////////////////////////////////////////////////

    ImgCropVerticalHandleHelper(ImgCropEdge edge) {
        super(null, edge);
        mEdge = edge;
    }

    // ImgCropHandleHelper Methods ////////////////////////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          @NonNull RectF imageRect,
                          float snapRadius) {

        // Adjust this ImgCropEdge accordingly.
        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);

        float top = ImgCropEdge.TOP.getCoordinate();
        float bottom = ImgCropEdge.BOTTOM.getCoordinate();

        // After this ImgCropEdge is moved, our crop window is now out of proportion.
        final float targetHeight = ImgCropAspectRatioUtil.calculateHeight(ImgCropEdge.getWidth(), targetAspectRatio);

        // Adjust the crop window so that it maintains the given aspect ratio by
        // moving the adjacent edges symmetrically in or out.
        final float difference = targetHeight - ImgCropEdge.getHeight();
        final float halfDifference = difference / 2;
        top -= halfDifference;
        bottom += halfDifference;

        ImgCropEdge.TOP.setCoordinate(top);
        ImgCropEdge.BOTTOM.setCoordinate(bottom);

        // Check if we have gone out of bounds on the top or bottom, and fix.
        if (ImgCropEdge.TOP.isOutsideMargin(imageRect, snapRadius)
                && !mEdge.isNewRectangleOutOfBounds(ImgCropEdge.TOP, imageRect, targetAspectRatio)) {

            final float offset = ImgCropEdge.TOP.snapToRect(imageRect);
            ImgCropEdge.BOTTOM.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }

        if (ImgCropEdge.BOTTOM.isOutsideMargin(imageRect, snapRadius)
                && !mEdge.isNewRectangleOutOfBounds(ImgCropEdge.BOTTOM, imageRect, targetAspectRatio)) {

            final float offset = ImgCropEdge.BOTTOM.snapToRect(imageRect);
            ImgCropEdge.TOP.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}
