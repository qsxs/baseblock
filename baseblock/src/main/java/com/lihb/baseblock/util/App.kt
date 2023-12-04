package com.lihb.baseblock.util

import android.app.Application
import com.tencent.mmkv.MMKV

object App {
    private lateinit var application: Application
    private lateinit var versionName: String
    private var versionCode: Int = 0
    private lateinit var applicationId: String
    private var debug: Boolean = false
    private lateinit var flavor: String
    private lateinit var buildType: String

    @JvmStatic
    fun init(
        app: Application,
        versionName: String,
        versionCode: Int,
        applicationId: String,
        debug: Boolean,
        flavor: String,
        buildType: String
    ) {
        application = app
        App.versionName = versionName
        App.versionCode = versionCode
        App.applicationId = applicationId
        App.debug = debug
        App.flavor = flavor
        App.buildType = buildType

        ActManager.init(app)
        ToastUtil.init(app)
        MMKV.initialize(app)
        NetworkStateUtil.init(app)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Application> context(): T {
        return application as T
    }

    @JvmStatic
    fun appVersionName(): String {
        return versionName
    }

    @JvmStatic
    fun appVersionCode(): Int {
        return versionCode
    }

    @JvmStatic
    fun applicationId(): String {
        return applicationId
    }

    @JvmStatic
    fun debug(): Boolean {
        return debug
    }

    @JvmStatic
    fun flavor(): String {
        return flavor
    }

    @JvmStatic
    fun buildType(): String {
        return buildType
    }
}