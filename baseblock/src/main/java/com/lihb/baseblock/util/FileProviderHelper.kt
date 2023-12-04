package com.lihb.baseblock.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileProviderHelper {

    fun getUriForFile(context: Context, file: File): Uri? {
        return FileProvider.getUriForFile(context, "${App.applicationId()}.fileprovider", file)
    }
}