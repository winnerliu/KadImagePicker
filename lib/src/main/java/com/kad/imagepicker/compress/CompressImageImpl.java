package com.kad.imagepicker.compress;

import android.content.Context;
import android.text.TextUtils;


import com.kad.imagepicker.model.KadImage;

import java.io.File;
import java.util.ArrayList;

/**
 * 压缩照片
 * Created by Winner on 2016/12/28.
 */
public class CompressImageImpl implements CompressImage {
    private CompressImageUtil compressImageUtil;
    private ArrayList<KadImage> images;
    private CompressImage.CompressListener listener;

    public static CompressImage of(Context context, CompressConfig config, ArrayList<KadImage> images, CompressImage.CompressListener listener) {
        return new CompressImageImpl(context, config, images, listener);
    }

    public static CompressImage of(Context context, CompressConfig config, KadImage image, CompressImage.CompressListener listener) {
        ArrayList<KadImage> images = new ArrayList<>();
        images.add(image);
        return new CompressImageImpl(context, config, images, listener);
    }

    private CompressImageImpl(Context context, CompressConfig config, ArrayList<KadImage> images, CompressImage.CompressListener listener) {
        compressImageUtil = new CompressImageUtil(context, config);
        this.images = images;
        this.listener = listener;
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty())
            listener.onCompressFailed(images, " images is null");
        for (KadImage image : images) {
            if (image == null) {
                listener.onCompressFailed(images, " There are pictures of compress  is null.");
                return;
            }
        }
        compress(images.get(0));
    }

    private void compress(final KadImage image) {
        if (TextUtils.isEmpty(image.getOriginalPath())) {
            continueCompress(image, false);
            return;
        }

        File file = new File(image.getOriginalPath());
        if (file == null || !file.exists() || !file.isFile()) {
            continueCompress(image, false);
            return;
        }

        compressImageUtil.compress(image.getOriginalPath(), new CompressImageUtil.CompressListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                image.setCompressPath(imgPath);
                continueCompress(image, true);
            }

            @Override
            public void onCompressFailed(String imgPath, String msg) {
                continueCompress(image, false, msg);
            }
        });
    }

    private void continueCompress(KadImage image, boolean preSuccess, String... message) {
        image.setCompressed(preSuccess);
        int index = images.indexOf(image);
        boolean isLast = index == images.size() - 1;
        if (isLast) {
            handleCompressCallBack(message);
        } else {
            compress(images.get(index + 1));
        }
    }

    private void handleCompressCallBack(String... message) {
        if (message.length > 0) {
            listener.onCompressFailed(images, message[0]);
            return;
        }

        for (KadImage image : images) {
            if (!image.isCompressed()) {
                listener.onCompressFailed(images, image.getCompressPath() + " is compress failures");
                return;
            }
        }
        listener.onCompressSuccess(images);
    }
}
