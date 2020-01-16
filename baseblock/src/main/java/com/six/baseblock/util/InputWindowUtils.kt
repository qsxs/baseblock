package com.six.baseblock.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object InputWindowUtils {
    @JvmStatic
    fun isActive(context: Context): Boolean {
        val imm = context.getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        return imm.isActive
    }


    @JvmStatic
    fun hideInputWindow(view: View?) {
        view?.apply {
            val imm = this.context.getSystemService(
                Context
                    .INPUT_METHOD_SERVICE
            ) as InputMethodManager//隐藏键盘
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }

    @JvmStatic
    fun hideInputWindow(activity: Activity) {
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
    fun showInputWindow(edt: View?) {
        edt?.apply {
            if (this is EditText){
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
                setSelection(text.length)
            }
            val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, 0)
        }

    }
}
