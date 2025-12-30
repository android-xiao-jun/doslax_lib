package com.hitomi.tilibrary.view.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * desc:
 * verson:
 * create by zhijun on 2024/10/18 09:55
 * update by zhijun on 2024/10/18 09:55
 */
public class ExoVideoView extends FrameLayout {

    public static final String CACHE_DIR = "video";

    public ExoVideoView(@NonNull Context context) {
        super(context);
    }

    @Nullable
    public Bitmap getBitmap() {
        return null;
    }

    public void play() {

    }

    public void reset() {

    }

    public void pause() {

    }

    public void resume() {

    }
}
