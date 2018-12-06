package com.six.baseblock.util

import android.content.Context
import android.content.Intent

object ShareUtil {
    const val storeUrl = ""
    fun shareText(context: Context, text: String, title: CharSequence?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain" //分享的是文本类型
        intent.putExtra(Intent.EXTRA_TEXT, text)//分享出去的内容
        context.startActivity(Intent.createChooser(intent, title))
    }
}