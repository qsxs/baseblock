package com.six.baseblock.util.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.resource.gif.GifDrawable
import java.io.File

class ImageLoaders private constructor() {

    private lateinit var glide: GlideRequests

    companion object {
        @JvmStatic
        fun with(context: Context): ImageLoaders {
            val loader = ImageLoaders()
            loader.glide = GlideApp.with(context)
            return loader
        }

        @JvmStatic
        fun with(activity: Activity): ImageLoaders {
            val loader = ImageLoaders()
            loader.glide = GlideApp.with(activity)
            return loader
        }

        @JvmStatic
        fun with(activity: FragmentActivity): ImageLoaders {
            val loader = ImageLoaders()
            loader.glide = GlideApp.with(activity)
            return loader
        }

        @JvmStatic
        fun with(fragment: Fragment): ImageLoaders {
            val loader = ImageLoaders()
            loader.glide = GlideApp.with(fragment)
            return loader
        }

        @JvmStatic
        fun with(view: View): ImageLoaders {
            val loader = ImageLoaders()
            loader.glide = GlideApp.with(view)
            return loader
        }
    }

    @CheckResult
    fun asBitmap(): ImageLoader<Bitmap> {
        return ImageLoader<Bitmap>(glide.asBitmap())
    }

    @CheckResult
    fun asGif(): ImageLoader<GifDrawable> {
        return ImageLoader<GifDrawable>(glide.asGif())
    }

    @CheckResult
    fun asDrawable(): ImageLoader<Drawable> {
        return ImageLoader<Drawable>(glide.asDrawable())
    }


    @CheckResult
    fun load(bitmap: Bitmap?): GlideRequest<Drawable> {
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
    fun load(drawable: Drawable?): GlideRequest<Drawable> {
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
    fun load(string: String?): GlideRequest<Drawable> {
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
    fun load(uri: Uri?): GlideRequest<Drawable> {
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
    fun load(file: File?): GlideRequest<Drawable> {
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
    fun load(@RawRes @DrawableRes id: Int?): GlideRequest<Drawable> {
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
    fun load(bytes: ByteArray?): GlideRequest<Drawable> {
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
    fun load(o: Any?): GlideRequest<Drawable> {
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