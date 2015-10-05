package com.dubmania.vidcraft.utils.media;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by rat on 9/28/2015.
 */
public class WaterMarkPositionCalculator implements ImageOverlayer.CoordinateCalculator {
    @Override
    public ImageOverlayer.Coordinate calculatePosition(int mWith, int mHeight, Bitmap mBitmap) {
        int x = mBitmap.getWidth();
        int y = mBitmap.getHeight();
        return new ImageOverlayer.Coordinate((mWith - x), (mHeight - y));
    }
}
