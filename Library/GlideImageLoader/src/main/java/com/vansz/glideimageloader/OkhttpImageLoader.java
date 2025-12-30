package com.vansz.glideimageloader;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hitomi.tilibrary.loader.ImageLoader;
import com.hitomi.tilibrary.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhijun on 2023-02-17.
 * 支持百分比进度指示器(Okhttp)
 */
public class OkhttpImageLoader implements ImageLoader, LifecycleEventObserver {
    private final Context context;
    private Map<String, SourceCallback> callbackMap = new HashMap<>();
    private final String taskTag;


    private static final String CACHE_DIR = "TransGlide";

    private OkhttpImageLoader(Context context) {
        if (context instanceof FragmentActivity) {
            ((FragmentActivity) context).getLifecycle().addObserver(this);
        }
        taskTag = context.toString();
        this.context = context;
        this.callbackMap = new HashMap<>();
    }

    public static OkhttpImageLoader with(Context context) {
        return new OkhttpImageLoader(context);
    }

    /**
     * {@link LifecycleEventObserver}
     */
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            DownloadTask.SingletonHolder.instance.clear(taskTag);
        }
    }

    @Override
    public void loadSource(final String imageUrl, final SourceCallback callback) {
        callbackMap.put(imageUrl, callback);
        if (callback != null) callback.onStart();
        if (!TextUtils.isEmpty(imageUrl) && !imageUrl.startsWith("http")) {
            Glide.with(context).download(imageUrl).listener(new RequestListener<File>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                    SourceCallback callback = callbackMap.get(imageUrl);
                    if (callback != null)
                        callback.onDelivered(STATUS_DISPLAY_FAILED, null);
                    callbackMap.remove(imageUrl);
                    return false;
                }

                @Override
                public boolean onResourceReady(final File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                    if (callback != null)
                        callback.onDelivered(STATUS_DISPLAY_SUCCESS, resource);
                    callbackMap.remove(imageUrl);
                    return false;
                }
            }).preload();
            return;
        }
        Disposable subscribe = DownloadTask.SingletonHolder.instance.requestDownloadTask(context,imageUrl, getCacheDir())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        SourceCallback callback = callbackMap.get(imageUrl);
                        if (o instanceof File) {
                            if (callback != null)
                                callback.onDelivered(STATUS_DISPLAY_SUCCESS, (File) o);
                            callbackMap.remove(imageUrl);
                        } else if (o instanceof Integer) {
                            if (callback != null)
                                callback.onProgress((Integer) o);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        SourceCallback callback = callbackMap.get(imageUrl);
                        if (callback != null)
                            callback.onDelivered(STATUS_DISPLAY_FAILED, null);
                        callbackMap.remove(imageUrl);
                    }
                });
        DownloadTask.SingletonHolder.instance.add(taskTag, imageUrl, subscribe);
    }

    @Override
    public boolean isDownloading(String imageUrl) {
        return callbackMap.containsKey(imageUrl);
    }

    @Override
    public File getCache(String url) {
        File cacheFile = new File(getCacheDir(), getFileName(url));
        return cacheFile.exists() ? cacheFile : null;
    }

    @Override
    public void clearCache() {
        Glide.get(context).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
                delete(getCacheDir());
            }
        }).start();
    }

    @Override
    public File getCacheDir() {
        File cacheDir = new File(context.getCacheDir(), CACHE_DIR);
        if (!cacheDir.exists()) cacheDir.mkdirs();
        return cacheDir;
    }

    private String getFileName(String imageUrl) {
        return FileUtils.getFileName(imageUrl);//FIX BUG
    }


    /**
     * Delete the directory.
     *
     * @param file The file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean delete(final File file) {
        if (file == null) return false;
        if (file.isDirectory()) {
            return deleteDir(file);
        }
        return deleteFile(file);
    }

    /**
     * Delete the directory.
     *
     * @param dir The directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    private static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Delete the file.
     *
     * @param file The file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    private static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }


}
