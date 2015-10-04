package com.dubmania.vidcraft.utils.media;

import android.graphics.Bitmap;

/**
 * Created by rat on 9/28/2015.
 */
public class WaterMarkPositionCalculator implements ImageOverlayer.CoordinateCalculator {
    @Override
    public ImageOverlayer.Coordinate calculatePosition(int mWith, int mHeight, Bitmap mBitmap) {
        int x = mBitmap.getHeight();
        int y = mBitmap.getWidth();
        return new ImageOverlayer.Coordinate((mHeight - x), (mWith -y));
    }
}
