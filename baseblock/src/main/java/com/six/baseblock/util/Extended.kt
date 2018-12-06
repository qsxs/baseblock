package com.six.baseblock.util

import android.os.Build
import android.text.Html

fun String?.fromHtml(): CharSequence? {
    return when {
        this.isNullOrEmpty() -> this
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(this, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        else -> Html.fromHtml(this)
    }
}