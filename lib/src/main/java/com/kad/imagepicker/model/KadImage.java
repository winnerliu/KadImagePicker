package com.kad.imagepicker.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Winner on 2016/12/28.
 */

public class KadImage implements Serializable {
    private String originalPath;
    private String compressPath;
    private boolean compressed;

    public static KadImage of(String path) {
        return new KadImage(path);
    }

    public static KadImage of(Uri uri) {
        return new KadImage(uri);
    }

    private KadImage(String path) {
        this.originalPath = path;
    }

    private KadImage(Uri uri) {
        this.originalPath = uri.getPath();
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }
}
