package com.six.baseblock.util.image

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

object ImageLoaderConfig {
    @DrawableRes
    var placeholderDrawableRes: Int = 0
    @DrawableRes
    var fallbackDrawableRes: Int = 0
    @DrawableRes
    var errorDrawableRes: Int = 0

    var filterByDefault = true

    var filter: ImageLoaderSourceFilter? = null

    @JvmStatic
    @JvmOverloads
    @Deprecated("弃用", level = DeprecationLevel.WARNING)
    fun init(@DrawableRes placeholderDrawableRes: Int, @DrawableRes fallbackDrawableRes: Int = 0, @DrawableRes errorDrawableRes: Int = 0) {
        this.placeholderDrawableRes = placeholderDrawableRes
        this.fallbackDrawableRes = fallbackDrawableRes
        this.errorDrawableRes = errorDrawableRes
    }

    @JvmStatic
    fun setPlaceholderRes(@DrawableRes @RawRes placeholderDrawableRes: Int): ImageLoaderConfig {
        this.placeholderDrawableRes = placeholderDrawableRes
        return this
    }

    @JvmStatic
    fun setFallbackRes(@DrawableRes @RawRes fallbackDrawableRes: Int): ImageLoaderConfig {
        this.fallbackDrawableRes = fallbackDrawableRes
        return this
    }

    @JvmStatic
    fun setErrorRes(@DrawableRes @RawRes errorDrawableRes: Int): ImageLoaderConfig {
        this.errorDrawableRes = errorDrawableRes
        return this
    }

    @JvmStatic
    fun setImageLoaderSourceFilter(filter: ImageLoaderSourceFilter?): ImageLoaderConfig {
        this.filter = filter
        return this
    }

    @JvmStatic
    fun setFilterDefault(value: Boolean): ImageLoaderConfig {
        this.filterByDefault = value
        return this
    }
}