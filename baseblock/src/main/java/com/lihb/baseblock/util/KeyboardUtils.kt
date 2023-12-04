package com.lihb.baseblock.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.max

object KeyboardUtils {

    fun observerKeyboardHeight(activity: Activity, callback: (Int) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.decorView.setWindowInsetsAnimationCallback(object :
                WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
                override fun onProgress(
                    insets: WindowInsets,
                    runningAnimations: MutableList<WindowInsetsAnimation>
                ): WindowInsets {
                    var keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    if (insets.isVisible(WindowInsetsCompat.Type.navigationBars())) {
                        keyboardHeight -= insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                    }

                    callback(max(0, keyboardHeight))
                    return insets
                }

            })
        } else {
            activity.window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                activity.window.decorView.getWindowVisibleDisplayFrame(rect)
//                Logger.i("onViewReady: rect:$rect")
                var keyboardHeight = DensityUtil.getWindowHeight(activity) - rect.bottom
                ViewCompat.getRootWindowInsets(activity.window.decorView)?.let {
                    if (it.isVisible(WindowInsetsCompat.Type.navigationBars())) {
                        keyboardHeight -= it.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                    }
                }
                callback(keyboardHeight)
//                binding.tvProtocol.updatePadding(bottom = (2392 - rect.bottom))

            }
        }
//        ViewCompat.setWindowInsetsAnimationCallback(binding.tvProtocol, object :
//            WindowInsetsAnimationCompat.Callback(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP) {
//            override fun onProgress(
//                insets: WindowInsetsCompat,
//                runningAnimations: MutableList<WindowInsetsAnimationCompat>
//            ): WindowInsetsCompat {
//                binding.tvProtocol.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom)
//                return insets
//            }
//
//        })

    }

    @JvmStatic
    fun isActive(context: Context): Boolean {
        val imm = context.getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        return imm.isActive
    }


    @JvmStatic
    fun hideKeyboard(view: View?) {
        view?.apply {
            val imm = this.context.getSystemService(
                Context
                    .INPUT_METHOD_SERVICE
            ) as InputMethodManager//隐藏键盘
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager//隐藏键盘
        imm.hideSoftInputFromWindow(
            activity.window.peekDecorView().windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    }


    @JvmStatic
    fun showKeyboard(edt: View?) {
        edt?.apply {
            if (this is EditText) {
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
                setSelection(text.length)
            }
            val imm =
                this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, 0)
        }
    }
}
