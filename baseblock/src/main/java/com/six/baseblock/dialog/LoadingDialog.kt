package com.six.baseblock.dialog

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import android.view.View
import com.six.baseblock.R
import com.six.baseblock.util.ToastUtil
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog(context: Context) : AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
//        progress.indeterminateDrawable.setColorFilter(ThemeResUtil.getColorByAttr(context, R.attr.colorAccent), PorterDuff.Mode.MULTIPLY)
        tv_msg.text = msg
        if (msg.isNullOrEmpty()) {
            tv_msg.visibility = View.GONE
        } else {
            tv_msg.visibility = View.VISIBLE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.setDimAmount(0.2f)
        }
    }

    private var lastBackPressed = 0L
    override fun onBackPressed() {
        super.onBackPressed()
        val now = System.currentTimeMillis()
        if (now - lastBackPressed < 2200) {
            cancel()
        } else {
            ToastUtil.show(R.string.push_again_to_cancel)
            lastBackPressed = now
        }
    }

    private var msg: CharSequence? = null

    fun setMessage(message: CharSequence?) {
        msg = message
        if (isShowing){
            tv_msg.text = msg
            if (msg.isNullOrEmpty()) {
                tv_msg.visibility = View.GONE
            } else {
                tv_msg.visibility = View.VISIBLE
            }
        }
    }

    fun setProgress(progress: Int) {
        progress_loading.progress = progress
    }
}