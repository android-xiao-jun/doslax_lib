package com.allo.adlib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.bytedance.sdk.openadsdk.TTDislikeDialogAbstract

/**
 * @Author yforyoung
 * @Date 2021/7/14 12:00
 * @Desc
 */
class DislikeAdDialog(context: Context) : TTDislikeDialogAbstract(context) {
    var dislikeListener: (() -> Unit)? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        findViewById<TextView>(R.id.tv_saw).setOnClickListener {
            dislikeListener?.invoke()
            dismiss()
        }

        findViewById<TextView>(R.id.tv_uninteresting).setOnClickListener {
            dislikeListener?.invoke()
            dismiss()
        }

    }


    companion object {
        private val TAG = DislikeAdDialog::class.java.simpleName
        fun create(context: Context): DislikeAdDialog {
            return DislikeAdDialog(context)
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_dislike_ad

    override fun getTTDislikeListViewIds(): IntArray {
        return intArrayOf(R.id.lv_dislike_custom)
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window?.let {
            // 不设置的话 window attrs 会无效
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val lp = it.attributes
            lp.gravity = Gravity.CENTER
            it.decorView.setPadding(0, 0, 0, 0)
            it.attributes = lp
            return lp
        }
        return ViewGroup.LayoutParams(-1, -1)
    }
}