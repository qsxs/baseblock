package com.lihb.baseblock.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet

class MarqueeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    init {
        this.ellipsize = TextUtils.TruncateAt.MARQUEE
        this.isSingleLine = true
        this.marqueeRepeatLimit = -1
    }

    override fun isFocused(): Boolean {
        return true
    }
}