package com.six.baseblock.util.image

import android.support.annotation.DrawableRes

object ImageLoaderConfig {
    @DrawableRes
    var placeholderDrawableRes: Int = 0
    @DrawableRes
    var fallbackDrawableRes: Int = 0
    @DrawableRes
    var errorDrawableRes: Int = 0


    @JvmOverloads
    fun init(@DrawableRes placeholderDrawableRes: Int, @DrawableRes fallbackDrawableRes: Int = 0, @DrawableRes errorDrawableRes: Int = 0) {
        this.placeholderDrawableRes = placeholderDrawableRes
        this.fallbackDrawableRes = fallbackDrawableRes
        this.errorDrawableRes = errorDrawableRes
    }
}