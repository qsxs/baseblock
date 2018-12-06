package com.six.baseblock.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.support.annotation.StringRes
import com.six.baseblock.R
import com.six.baseblock.dialog.LoadingDialog

/**
 * 加载框弹窗工具类
 * Created by lhb on 2017/07/05.
 */

object LoadingDialogUtil {
    private var mDialog: Dialog? = null

    fun newLoadingDialog(context: Context, message: CharSequence? = context.getString(R.string.loading), onCancelListener: DialogInterface.OnCancelListener? = null): LoadingDialog {
        val dialog = LoadingDialog(context)
        dialog.setMessage(message)
        dialog.setCanceledOnTouchOutside(onCancelListener != null)
        dialog.setCancelable(onCancelListener != null)
        dialog.setOnCancelListener { d ->
            dismiss()
            onCancelListener?.onCancel(d)
        }
        return dialog
    }

    /**
     * onCancelListener不为null的时候可以点击外部取消弹窗
     */
    @JvmOverloads
    fun show(context: Context, @StringRes message: Int, onCancelListener: DialogInterface.OnCancelListener? = null) {
        show(context, context.getString(message), onCancelListener)
    }

    /**
     * onCancelListener不为null的时候可以点击外部取消弹窗
     */
    @JvmOverloads
    fun show(context: Context, message: CharSequence? = context.getString(R.string.loading), onCancelListener: DialogInterface.OnCancelListener? = null) {
        if (mDialog != null) {
            dismiss()
        }
        val dialog = LoadingDialog(context)
        dialog.setMessage(message)
        dialog.setCanceledOnTouchOutside(onCancelListener != null)
        dialog.setCancelable(onCancelListener != null)
        dialog.setOnCancelListener { d ->
            dismiss()
            onCancelListener?.onCancel(d)
        }
        mDialog = dialog
        dialog.show()
    }


    fun dismiss() {
        try {
            mDialog!!.dismiss()
        } catch (e: Exception) {
            //
        } finally {
            mDialog = null
        }
    }

    fun dismissDelayed(delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, delayMillis)
    }
}
