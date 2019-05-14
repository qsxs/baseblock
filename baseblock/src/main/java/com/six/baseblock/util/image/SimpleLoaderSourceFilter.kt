package com.six.baseblock.util.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.File

open class SimpleLoaderSourceFilter : ImageLoaderSourceFilter {
    override fun filter(bitmap: Bitmap?): Bitmap? {
        return bitmap
    }

    override fun filter(drawable: Drawable?): Drawable? {
        return drawable
    }

    override fun filter(string: String?): String? {
        return string
    }

    override fun filter(uri: Uri?): Uri? {
        return uri
    }

    override fun filter(file: File?): File? {
        return file
    }

    override fun filter(resId: Int?): Int? {
        return resId
    }

    override fun filter(bytes: ByteArray?): ByteArray? {
        return bytes
    }

    override fun filter(o: Any?): Any? {
        return o
    }
}