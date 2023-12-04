package com.lihb.baseblock.util

import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned

fun String?.fromHtml(): CharSequence? {
    return when {
        this.isNullOrEmpty() -> this
        else -> HtmlCompat.toHtml(this.toSpanned(), HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
    }
}