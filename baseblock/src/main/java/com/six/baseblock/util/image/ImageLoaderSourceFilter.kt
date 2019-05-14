package com.six.baseblock.util.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import java.io.File

interface ImageLoaderSourceFilter {
    fun filter(bitmap: Bitmap?): Bitmap?
    fun filter(drawable: Drawable?): Drawable?
    fun filter(string: String?): String?
    fun filter(uri: Uri?): Uri?
    fun filter(file: File?): File?
    fun filter(@DrawableRes @RawRes resId: Int?): Int?
    fun filter(bytes: ByteArray?): ByteArray?
    fun filter(o: Any?): Any?
}