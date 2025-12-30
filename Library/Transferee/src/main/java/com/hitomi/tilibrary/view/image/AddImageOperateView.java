package com.hitomi.tilibrary.view.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hitomi.tilibrary.R;

/**
 * desc:
 * verson:
 * create by zhijun on 2023/02/20 10:38
 * update by zhijun on 2023/02/20 10:38
 */
public class AddImageOperateView extends RelativeLayout {

    public AddImageOperateView(Context context) {
        this(context, null);
    }

    public AddImageOperateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddImageOperateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.transferee_img_operate_view, this);
        initView();
    }

    private static final int DISMISS_TIME = 2000;

    private final Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            setShowOriginImgClick();
        }
    };

    public void initView() {
        View iv_rotate_left = findViewById(R.id.iv_rotate_left);
        if (iv_rotate_left != null) {
            iv_rotate_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOperateListener != null) {
                        onOperateListener.bindTransferImage().rotate(90);
                    }
                }
            });
        }
        View iv_rotate_right = findViewById(R.id.iv_rotate_right);
        if (iv_rotate_right != null) {
            iv_rotate_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOperateListener != null) {
                        onOperateListener.bindTransferImage().rotate(-90);
                    }
                }
            });
        }
    }


    /**
     * 添加图片操作
     */
    private void setShowOriginImgClick() {
        final TextView tv_show_text = findViewById(R.id.tv_show_text);
        if (tv_show_text != null) {
            if (onOperateListener == null || onOperateListener.isDownload()) {
                tv_show_text.setText("已完成");
                tv_show_text.setVisibility(View.INVISIBLE);
            } else {
                tv_show_text.setText("查看原图");
                tv_show_text.setVisibility(View.VISIBLE);
                tv_show_text.setEnabled(!onOperateListener.isDownloading());
            }
            tv_show_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOperateListener != null) {
                        onOperateListener.onDownloadClick(tv_show_text);
                    }
                }
            });
        }
    }

    private OnOperateListener onOperateListener;

    public void setOnOperateListener(OnOperateListener onOperateListener) {
        this.onOperateListener = onOperateListener;
        removeCallbackDismiss();
        if (onOperateListener != null) {
            setShowOriginImgClick();
        }
    }

    public void postDismiss() {
        removeCallbackDismiss();
        postDelayed(dismissRunnable, DISMISS_TIME);
    }

    public void removeCallbackDismiss() {
        removeCallbacks(dismissRunnable);
    }

    public interface OnOperateListener {
        TransferImage bindTransferImage();

        //是否已经下载
        boolean isDownload();

        //是否正在下载
        boolean isDownloading();

        //点击下载按钮
        void onDownloadClick(TextView tv_show_text);
    }
}
