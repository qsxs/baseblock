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
    fun hideInputWindow(context: Context, edt: EditText) {
        val imm = context.getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager//隐藏键盘
        imm.hideSoftInputFromWindow(edt.windowToken, 0)

    }

    @JvmStatic
    fun hideInputWindow(view: View) {
        val imm = view.context.getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager//隐藏键盘
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    @JvmStatic
    fun hideInputWindow(activity: Activity) {
        val imm = activity.getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager//隐藏键盘
        imm.hideSoftInputFromWindow(activity.window.peekDecorView().windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        //            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }

    @JvmStatic
    fun showInputWindow(context: Context, edt: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edt, 0)
    }

    @JvmStatic
    fun showInputWindow(edt: EditText) {
        val imm = edt.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edt, 0)
    }
}
