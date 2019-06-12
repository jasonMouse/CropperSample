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
import com.edmodo.cropper.cropwindow.edge.ImgCropEdgePair;
import com.edmodo.cropper.util.ImgCropAspectRatioUtil;

/**
 * Abstract helper class to handle operations on a crop window ImgCropHandle.
 */
abstract class ImgCropHandleHelper {

    // Member Variables ////////////////////////////////////////////////////////

    private static final float UNFIXED_ASPECT_RATIO_CONSTANT = 1;
    private ImgCropEdge mHorizontalEdge;
    private ImgCropEdge mVerticalEdge;

    // Save the Pair object as a member variable to avoid having to instantiate
    // a new Object every time getActiveEdges() is called.
    private ImgCropEdgePair mActiveEdges;

    // Constructor /////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor.
     *
     * @param horizontalEdge the horizontal edge associated with this handle; may be null
     * @param verticalEdge   the vertical edge associated with this handle; may be null
     */
    ImgCropHandleHelper(ImgCropEdge horizontalEdge, ImgCropEdge verticalEdge) {
        mHorizontalEdge = horizontalEdge;
        mVerticalEdge = verticalEdge;
        mActiveEdges = new ImgCropEdgePair(mHorizontalEdge, mVerticalEdge);
    }

    // Package-Private Methods /////////////////////////////////////////////////////////////////////

    /**
     * Updates the crop window by directly setting the ImgCropEdge coordinates.
     *
     * @param x          the new x-coordinate of this handle
     * @param y          the new y-coordinate of this handle
     * @param imageRect  the bounding rectangle of the image
     * @param snapRadius the maximum distance (in pixels) at which the crop window should snap to
     *                   the image
     */
    void updateCropWindow(float x,
                          float y,
                          @NonNull RectF imageRect,
                          float snapRadius) {

        final ImgCropEdgePair activeEdges = getActiveEdges();
        final ImgCropEdge primaryEdge = activeEdges.primary;
        final ImgCropEdge secondaryEdge = activeEdges.secondary;

        if (primaryEdge != null)
            primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, UNFIXED_ASPECT_RATIO_CONSTANT);

        if (secondaryEdge != null)
            secondaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, UNFIXED_ASPECT_RATIO_CONSTANT);
    }

    /**
     * Updates the crop window by directly setting the ImgCropEdge coordinates; this method maintains a
     * given aspect ratio.
     *
     * @param x                 the new x-coordinate of this handle
     * @param y                 the new y-coordinate of this handle
     * @param targetAspectRatio the aspect ratio to maintain
     * @param imageRect         the bounding rectangle of the image
     * @param snapRadius        the maximum distance (in pixels) at which the crop window should
     *                          snap to the image
     */
    abstract void updateCropWindow(float x,
                                   float y,
                                   float targetAspectRatio,
                                   @NonNull RectF imageRect,
                                   float snapRadius);

    /**
     * Gets the Edges associated with this handle (i.e. the Edges that should be moved when this
     * handle is dragged). This is used when we are not maintaining the aspect ratio.
     *
     * @return the active edge as a pair (the pair may contain null values for the
     * <code>primary</code>, <code>secondary</code> or both fields)
     */
    ImgCropEdgePair getActiveEdges() {
        return mActiveEdges;
    }

    /**
     * Gets the Edges associated with this handle as an ordered Pair. The <code>primary</code> ImgCropEdge
     * in the pair is the determining side. This method is used when we need to maintain the aspect
     * ratio.
     *
     * @param x                 the x-coordinate of the touch point
     * @param y                 the y-coordinate of the touch point
     * @param targetAspectRatio the aspect ratio that we are maintaining
     *
     * @return the active edges as an ordered pair
     */
    ImgCropEdgePair getActiveEdges(float x, float y, float targetAspectRatio) {

        // Calculate the aspect ratio if this handle were dragged to the given x-y coordinate.
        final float potentialAspectRatio = getAspectRatio(x, y);

        // If the touched point is wider than the aspect ratio, then x is the determining side. Else, y is the determining side.
        if (potentialAspectRatio > targetAspectRatio) {
            mActiveEdges.primary = mVerticalEdge;
            mActiveEdges.secondary = mHorizontalEdge;
        } else {
            mActiveEdges.primary = mHorizontalEdge;
            mActiveEdges.secondary = mVerticalEdge;
        }
        return mActiveEdges;
    }

    // Private Methods /////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the aspect ratio of the resulting crop window if this handle were dragged to the given
     * point.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     *
     * @return the aspect ratio
     */
    private float getAspectRatio(float x, float y) {

        // Replace the active edge coordinate with the given touch coordinate.
        final float left = (mVerticalEdge == ImgCropEdge.LEFT) ? x : ImgCropEdge.LEFT.getCoordinate();
        final float top = (mHorizontalEdge == ImgCropEdge.TOP) ? y : ImgCropEdge.TOP.getCoordinate();
        final float right = (mVerticalEdge == ImgCropEdge.RIGHT) ? x : ImgCropEdge.RIGHT.getCoordinate();
        final float bottom = (mHorizontalEdge == ImgCropEdge.BOTTOM) ? y : ImgCropEdge.BOTTOM.getCoordinate();

        return ImgCropAspectRatioUtil.calculateAspectRatio(left, top, right, bottom);
    }
}
