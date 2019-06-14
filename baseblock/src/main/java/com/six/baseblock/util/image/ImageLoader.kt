package com.six.baseblock.util.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import java.io.File

class ImageLoader<T>(private var glide: GlideRequest<T>) {
    @JvmOverloads
    @CheckResult
    fun load(bitmap: Bitmap?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = bitmap
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(bitmap)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(drawable: Drawable?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = drawable
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(drawable)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(string: String?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = string
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(string)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(uri: Uri?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = uri
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(uri)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(file: File?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = file
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(file)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(@RawRes @DrawableRes id: Int?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = id
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(id)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(bytes: ByteArray?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = bytes
        if (ImageLoaderConfig.filter != null  && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(bytes)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }

    @JvmOverloads
    @CheckResult
    fun load(o: Any?,filter:Boolean = true): GlideRequest<T> {
        var finalSource = o
        if (ImageLoaderConfig.filter != null && filter) {
            finalSource = ImageLoaderConfig.filter?.filter(o)
        }
        val request = glide.load(finalSource)

        if (ImageLoaderConfig.placeholderDrawableRes > 0) {
            request.placeholder(ImageLoaderConfig.placeholderDrawableRes)
        }
        if (ImageLoaderConfig.errorDrawableRes > 0) {
            request.error(ImageLoaderConfig.errorDrawableRes)
        }
        if (ImageLoaderConfig.fallbackDrawableRes > 0) {
            request.fallback(ImageLoaderConfig.fallbackDrawableRes)
        }
        return request
    }
}