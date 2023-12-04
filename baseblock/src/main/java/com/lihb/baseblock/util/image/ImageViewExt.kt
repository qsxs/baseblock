package com.lihb.baseblock.util.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.Transformation
import jp.wasabeef.glide.transformations.BlurTransformation

@SuppressLint("CheckResult")
fun ImageView.load(
    url: String?,
    @DrawableRes placeholderDrawable: Int = -1,
    @DrawableRes fallbackDrawable: Int = placeholderDrawable,
    context: Context = this.context,
    transform: Transformation<Bitmap>? = null
) {
    val load = ImageLoaders.with(context)
        .load(url)
    if (placeholderDrawable != -1) {
        load.placeholder(placeholderDrawable)
    }
    if (fallbackDrawable != -1) {
        load.fallback(fallbackDrawable)
    }
    if (transform != null) load.transform(transform)
    load.into(this)
}

@SuppressLint("CheckResult")
fun ImageView.load(
    @DrawableRes url: Int,
    @DrawableRes placeholderDrawable: Int = -1,
    @DrawableRes fallbackDrawable: Int = placeholderDrawable,
    context: Context = this.context,
    transform: Transformation<Bitmap>? = null
) {
    val load = ImageLoaders.with(context)
        .load(url)
    if (placeholderDrawable != -1) {
        load.placeholder(placeholderDrawable)
    }
    if (fallbackDrawable != -1) {
        load.fallback(fallbackDrawable)
    }
    if (transform != null) load.transform(transform)
    load.into(this)
}

@SuppressLint("CheckResult")
fun ImageView.loadBlur(
    url: String?,
    @DrawableRes placeholderDrawable: Int = -1,
    @DrawableRes fallbackDrawable: Int = placeholderDrawable,
    context: Context = this.context
) {
    load(url, placeholderDrawable, fallbackDrawable, context, BlurTransformation(38))
}

@SuppressLint("CheckResult")
fun ImageView.loadBlur(
    @DrawableRes url: Int,
    @DrawableRes placeholderDrawable: Int = -1,
    @DrawableRes fallbackDrawable: Int = placeholderDrawable,
    context: Context = this.context,
) {
    load(url, placeholderDrawable, fallbackDrawable, context, BlurTransformation(38))
}