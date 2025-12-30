package com.vansz.glideimageloader;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hitomi.tilibrary.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * desc:
 * verson:
 * create by zhijun on 2023/02/24 17:23
 * update by zhijun on 2023/02/24 17:23
 */
public class DownloadTask {
    private final HashMap<String, HashMap<String, Disposable>> mMaps;
    private final OkHttpClient okHttpClient;
    private static final int sBufferSize = 8192;
    private static final int DEFAULT_COMPRESSION_SIZE = 512 << 10;

    private DownloadTask() {
        mMaps = new HashMap<>();
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static class SingletonHolder {
        public final static DownloadTask instance = new DownloadTask();
    }

    public Observable<Object> requestDownloadTask(final Context context,final String imageUrl, final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> downloadListener) throws Exception {
                        try {
                            File fileGlide = Glide.with(context).downloadOnly().load(imageUrl).onlyRetrieveFromCache(true).submit().get();
                            if (fileGlide != null && fileGlide.isFile() && fileGlide.exists()) {
                                //是个文件存在，，已经下载过直接返回
                                downloadListener.onNext(fileGlide);
                                downloadListener.onComplete();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Request request = new Request.Builder()
                                .url(imageUrl)
                                .tag(imageUrl)
                                .addHeader("Accept-Encoding", "identity")
                                .build();
                        Response response = okHttpClient.newCall(request).execute();

                        ResponseBody body = response.body();
                        if (!response.isSuccessful() || body == null || downloadListener.isDisposed()) {
                            downloadListener.onError(new Throwable("服务器返回数据异常"));
                            return;
                        }
                        final InputStream is = body.byteStream();
                        final long totalLength = body.contentLength();

                        File fileOut = new File(cacheDir, FileUtils.getFileName(imageUrl));//下载好的文件
                        if (fileOut.exists() && fileOut.canRead() && fileOut.isFile() && fileOut.length() > 0) {
                            //是个文件存在，，已经下载过直接返回
                            downloadListener.onNext(fileOut);
                            downloadListener.onComplete();
                            return;
                        }
                        File fileTemp = new File(cacheDir, FileUtils.getFileName(imageUrl) + "_origin");//下载过程临时文件(源文件)

                        //创建文件
                        if (!fileTemp.getParentFile().exists())
                            fileTemp.getParentFile().mkdirs();
                        FileUtils.delete(fileTemp);
                        try {
                            fileTemp.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                            downloadListener.onError(new IOException("文件创建失败"));
                            return;
                        }

                        OutputStream os = null;
                        long currentLength = 0;
                        try {
                            os = new BufferedOutputStream(new FileOutputStream(fileTemp));
                            byte data[] = new byte[sBufferSize];
                            int len;
                            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                                os.write(data, 0, len);
                                currentLength += len;
                                //计算当前下载进度
                                if (totalLength != -1) {
                                    downloadListener.onNext((int) (100 * currentLength / totalLength));
                                }
                                if (downloadListener.isDisposed()) {
                                    return;
                                }
                            }
                            if (fileTemp.exists() && fileTemp.length() > DEFAULT_COMPRESSION_SIZE) {
                                //文件过大(超过)需要压缩，才能返回，，这里直接返回下载的临时文件，，页面显示会处理压缩
                                downloadListener.onNext(fileTemp);
                            } else {
                                //下载完成，并返回保存的文件路径
                                boolean b = fileTemp.renameTo(fileOut);
                                if (b) {
                                    downloadListener.onNext(fileOut);
                                } else {
                                    downloadListener.onNext(fileTemp);
                                }
                            }
                            downloadListener.onComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            downloadListener.onError(new IOException("文件保存失败"));
                        } finally {
                            try {
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (os != null) {
                                    os.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public void add(String tagTask, String imageUrl, Disposable disposable) {
        HashMap<String, Disposable> stringDisposableHashMap = mMaps.get(tagTask);
        if (stringDisposableHashMap == null) {
            stringDisposableHashMap = new HashMap<>();
            mMaps.put(tagTask, stringDisposableHashMap);
        }
        dispose(stringDisposableHashMap.put(imageUrl, disposable));
    }

    /**
     * 清空所有任务
     */
    public void clear() {
        for (Map.Entry<String, HashMap<String, Disposable>> disposableEntry : mMaps.entrySet()) {
            clearImageTask(disposableEntry.getValue());
        }
        mMaps.clear();
    }

    private void clearImageTask(@Nullable HashMap<String, Disposable> value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Disposable> child : value.entrySet()) {
            dispose(child.getValue());
        }
    }

    /**
     * 清空当前Tag所有任务
     */
    public void clear(@Nullable String tag) {
        if (TextUtils.isEmpty(tag)) {
            clear();
        }
        clearImageTask(mMaps.get(tag));
    }


    /**
     * 取消当前下载任务
     *
     * @param disposable
     */
    public void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
