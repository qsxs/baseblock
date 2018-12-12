package com.six.baseblock.util

import android.app.Application
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.six.baseblock.R


/**
 * toast 工具类
 * Created by Administrator on 2017/06/17.
 */

object ToastUtil {
    private var lastToast = ""
    private var lastToastTime: Long = 0
    private lateinit var app: Application

    fun init(application: Application) {
        app = application
    }

    fun show(@StringRes message: Int) {
        show(app.getString(message), Toast.LENGTH_SHORT, 0, 0, null)
    }

    @JvmOverloads
    fun show(
        message: String?,
        duration: Int = Toast.LENGTH_SHORT,
        icon: Int = 0,
        gravity: Int = 0,
        onAttachStateChangeListener: View.OnAttachStateChangeListener? = null
    ) {
        if (!TextUtils.isEmpty(message)) {
            val time = System.currentTimeMillis()
            if (!message.equals(lastToast, ignoreCase = true) || Math.abs(time - lastToastTime) > 2500) {
                if (icon > 0) {
                    val view = LayoutInflater.from(app).inflate(R.layout.view_base_toast, null)
                    val tv = view.findViewById<TextView>(R.id.toast_msg)
                    tv.text = message
                    tv.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
                    if (null != onAttachStateChangeListener) {
                        view.addOnAttachStateChangeListener(onAttachStateChangeListener)
                    }
                    val toast = Toast(app)
                    toast.view = view
                    if (gravity == Gravity.CENTER) {
                        toast.setGravity(gravity, 0, 0)
                    } else {
                        toast.setGravity(gravity, 0, 35)
                    }
                    toast.duration = duration
                    toast.show()
                } else {
                    Toast.makeText(app, message, duration).show()
                }
                lastToast = message!!
                lastToastTime = System.currentTimeMillis()
            }
        }


    }
//    }
}
