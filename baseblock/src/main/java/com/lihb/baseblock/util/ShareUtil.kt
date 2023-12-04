package com.lihb.baseblock.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object ShareUtil {

    @JvmStatic
    fun shareText(context: Context, text: String, title: CharSequence?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain" //分享的是文本类型
        intent.putExtra(Intent.EXTRA_TEXT, text)//分享出去的内容
        context.startActivity(Intent.createChooser(intent, title))
    }

    fun sharePicture(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*" //分享的是文本类型
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        context.startActivity(Intent.createChooser(intent, "分享好友"))
    }
}