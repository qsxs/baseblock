package com.lihb.baseblock.util

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object CopyHelper {
    fun copyText(value: CharSequence?) {
        if (value.isNullOrEmpty()) return
        //获取剪贴板管理器：
        val cm =
            App.context<Application>()
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("异间大陆", value)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }
}