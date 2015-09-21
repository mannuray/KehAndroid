package com.dubmania.vidcraft.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by rat on 9/18/2015.
 */
public class FileCache {

    private File mCacheDir;// = getExternalCacheDir();

    public FileCache(File mCacheDir) {
        this.mCacheDir = mCacheDir;
    }

    public File getFile(String path) throws IOException {
        File f = new File(this.mCacheDir + "/" + path);
        if(f.exists()) {
            return f;
        }
        else
            throw new IOException("File do not exist");
    }

    public File getTempFile(String path) {
        return new File(this.mCacheDir + "/" + path);
    }
}
