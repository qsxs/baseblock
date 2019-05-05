package com.six.baseblock.util

import android.app.Application
import android.provider.Settings
import java.util.*

object DevicesUtil {
    @JvmStatic
    fun uuid(): String {
        var token = Settings.System.getString(App.context<Application>().contentResolver, Settings.Secure.ANDROID_ID)
        if (token.isEmpty() || token == "9774d56d682e549c") {
            val installtionId = DevicesSP.getInstalltionId()
            token = if (installtionId.isNullOrEmpty()) {
                val uuid = UUID.randomUUID().toString()
                DevicesSP.saveInstalltionId(uuid)
                uuid
            } else {
                installtionId
            }

        }
        return token
//        return UUID.randomUUID().toString()用这个的话每次都会变，需要保存起来
    }

    private object DevicesSP : BaseSpHelper(App.context(), "DevicesSP") {
        @JvmStatic
    fun saveInstalltionId(id: String) {
            setString("InstalltionID", id)
        }

        @JvmStatic
    fun getInstalltionId(): String? {
            return getString("InstalltionID", null)
        }
    }
}