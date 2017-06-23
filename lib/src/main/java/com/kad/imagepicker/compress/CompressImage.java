package com.kad.imagepicker.compress;


import com.kad.imagepicker.model.KadImage;

import java.util.ArrayList;

/**
 * 压缩照片
 * Created by Winner on 2016/12/28.
 */
public interface CompressImage {
    void compress();

    /**
     * 压缩结果监听器
     */
    interface CompressListener {
        /**
         * 压缩成功
         * @param images 已经压缩图片
         */
        void onCompressSuccess(ArrayList<KadImage> images);

        /**
         * 压缩失败
         * @param images 压缩失败的图片
         * @param msg 失败的原因
         */
        void onCompressFailed(ArrayList<KadImage> images, String msg);
    }
}
