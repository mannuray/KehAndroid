package com.dubmania.vidcraft.utils.media;

import android.graphics.Bitmap;
import android.util.Log;

import com.dubmania.vidcraft.utils.ConstantsStore;

/**
 * Created by rat on 9/28/2015.
 */
public class WaterMarkPositionCalculator implements ImageOverlayer.ImagePostioner {
    private Bitmap mBitmap;

    public WaterMarkPositionCalculator(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    @Override
    public ImageOverlayer.Coordinate calculatePosition(int mWith, int mHeight) {
        int x = (int)(mBitmap.getWidth()* ConstantsStore.IMAGE_SCALE_RATIO_X);
        int y = (int)(mBitmap.getHeight()*ConstantsStore.IMAGE_SCALE_RATIO_X);
        return new ImageOverlayer.Coordinate((mWith - x), (mHeight - y), Bitmap.createScaledBitmap(mBitmap,
                (int)(mBitmap.getWidth()* ConstantsStore.IMAGE_SCALE_RATIO_X), (int)(mBitmap.getHeight()*ConstantsStore.IMAGE_SCALE_RATIO_X), true));
    }
}
