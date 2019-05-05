package com.six.baseblock.util.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import java.io.File

class ImageLoader<T>(private var glide: GlideRequest<T>) {
    @CheckResult
    fun load(bitmap: Bitmap?): GlideRequest<T> {
        val request = glide.load(bitmap)
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

    @CheckResult
    fun load(drawable: Drawable?): GlideRequest<T> {
        val request = glide.load(drawable)
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

    @CheckResult
    fun load(string: String?): GlideRequest<T> {
        val request = glide.load(string)
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

    @CheckResult
    fun load(uri: Uri?): GlideRequest<T> {
        val request = glide.load(uri)
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

    @CheckResult
    fun load(file: File?): GlideRequest<T> {
        val request = glide.load(file)
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

    @CheckResult
    fun load(@RawRes @DrawableRes id: Int?): GlideRequest<T> {
        val request = glide.load(id)
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

    @CheckResult
    fun load(bytes: ByteArray?): GlideRequest<T> {
        val request = glide.load(bytes)
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

    @CheckResult
    fun load(o: Any?): GlideRequest<T> {
        val request = glide.load(o)
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