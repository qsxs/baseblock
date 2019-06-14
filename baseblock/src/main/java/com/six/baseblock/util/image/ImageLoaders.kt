package com.six.baseblock.util.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
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

    @JvmOverloads
    @CheckResult
    fun load(bitmap: Bitmap?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(drawable: Drawable?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(string: String?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(uri: Uri?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(file: File?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(@RawRes @DrawableRes id: Int?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(bytes: ByteArray?,filter:Boolean = true): GlideRequest<Drawable> {
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
    fun load(o: Any?,filter:Boolean = true): GlideRequest<Drawable> {
        var finalSource = o
        if (ImageLoaderConfig.filter != null  && filter) {
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