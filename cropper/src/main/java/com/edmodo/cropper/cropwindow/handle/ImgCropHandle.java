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
 * Enum representing a pressable, draggable ImgCropHandle on the crop window.
 */
public enum ImgCropHandle {

    TOP_LEFT(new ImgCropCornerHandleHelper(ImgCropEdge.TOP, ImgCropEdge.LEFT)),
    TOP_RIGHT(new ImgCropCornerHandleHelper(ImgCropEdge.TOP, ImgCropEdge.RIGHT)),
    BOTTOM_LEFT(new ImgCropCornerHandleHelper(ImgCropEdge.BOTTOM, ImgCropEdge.LEFT)),
    BOTTOM_RIGHT(new ImgCropCornerHandleHelper(ImgCropEdge.BOTTOM, ImgCropEdge.RIGHT)),
    LEFT(new ImgCropVerticalHandleHelper(ImgCropEdge.LEFT)),
    TOP(new ImgCropHorizontalHandleHelper(ImgCropEdge.TOP)),
    RIGHT(new ImgCropVerticalHandleHelper(ImgCropEdge.RIGHT)),
    BOTTOM(new ImgCropHorizontalHandleHelper(ImgCropEdge.BOTTOM)),
    CENTER(new ImgCropCenterHandleHelper());

    // Member Variables ////////////////////////////////////////////////////////////////////////////

    private ImgCropHandleHelper mHelper;

    // Constructors ////////////////////////////////////////////////////////////////////////////////

    ImgCropHandle(ImgCropHandleHelper helper) {
        mHelper = helper;
    }

    // Public Methods //////////////////////////////////////////////////////////

    public void updateCropWindow(float x,
                                 float y,
                                 @NonNull RectF imageRect,
                                 float snapRadius) {

        mHelper.updateCropWindow(x, y, imageRect, snapRadius);
    }

    public void updateCropWindow(float x,
                                 float y,
                                 float targetAspectRatio,
                                 @NonNull RectF imageRect,
                                 float snapRadius) {

        mHelper.updateCropWindow(x, y, targetAspectRatio, imageRect, snapRadius);
    }
}
