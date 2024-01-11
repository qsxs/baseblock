package com.lihb.baseblock.util

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lihb.baseblock.R
import kotlin.math.abs


/**
 * toast 工具类
 * Created by Administrator on 2017/06/17.
 */

object ToastUtil {
    private var lastToast: CharSequence = ""
    private var lastDuration = -99
    private var lastToastTime: Long = 0
    private lateinit var app: Application
    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    @DrawableRes
    private var defaultIcon: Int = 0
    private var defaultGravity: Int = 0
    private var toastIntervalsMinMillis: Long = 2500

    /**
     * @param defaultGravity  Set the location at which the notification should appear on the screen.
     * Warning: Starting from Android [Build.VERSION_CODES.R], for apps
     * targeting API level [Build.VERSION_CODES.R] or higher, this method is a no-op when
     * called on text toasts.see also[android.view.Gravity] and [Toast.getGravity]
     *
     * @param toastIntervalsMinMillis The minimum time interval between two toasts with the same content
     */
    @JvmStatic
    fun init(
        application: Application,
        @DrawableRes defaultIcon: Int = this.defaultIcon,
        defaultGravity: Int = this.defaultGravity,
        toastIntervalsMinMillis: Long = this.toastIntervalsMinMillis
    ) {
        app = application
        this.defaultIcon = defaultIcon
        this.defaultGravity = defaultGravity
        this.toastIntervalsMinMillis = toastIntervalsMinMillis
    }

    /**
     * @param gravity  Set the location at which the notification should appear on the screen.
     *
     * Warning: Starting from Android [Build.VERSION_CODES.R], for apps
     * targeting API level [Build.VERSION_CODES.R] or higher, this method is a no-op when
     * called on text toasts.
     *
     * @see [android.view.Gravity]
     * @see [Toast.getGravity]
     */
    @JvmOverloads
    @JvmStatic
    fun show(
        @StringRes message: Int,
        duration: Int = Toast.LENGTH_SHORT,
        @DrawableRes icon: Int = defaultIcon,
        gravity: Int = defaultGravity,
        onAttachStateChangeListener: View.OnAttachStateChangeListener? = null
    ) {
        show(app.getString(message), duration, icon, gravity, onAttachStateChangeListener)
    }

    /**
     * @param gravity  Set the location at which the notification should appear on the screen.
     *
     * Warning: Starting from Android [Build.VERSION_CODES.R], for apps
     * targeting API level [Build.VERSION_CODES.R] or higher, this method is a no-op when
     * called on text toasts.
     *
     * @see [android.view.Gravity]
     * @see [Toast.getGravity]
     */
    @SuppressLint("ResourceType")
    @JvmOverloads
    @JvmStatic
    fun show(
        message: CharSequence?,
        duration: Int = Toast.LENGTH_SHORT,
        @DrawableRes icon: Int = defaultIcon,
        gravity: Int = defaultGravity,
        onAttachStateChangeListener: View.OnAttachStateChangeListener? = null
    ) {
        val showToastRunnable = Runnable {
            if (!message.isNullOrEmpty()) {
                val time = System.currentTimeMillis()
                if (!message.contentEquals(lastToast, ignoreCase = false)
                    || abs(time - lastToastTime) > toastIntervalsMinMillis
                    || lastDuration != duration
                ) {
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
                    lastDuration = duration
                    lastToastTime = System.currentTimeMillis()
                }
            }
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {
            mainHandler.post(showToastRunnable)
        } else {
            showToastRunnable.run()
        }
    }
//    }
}
