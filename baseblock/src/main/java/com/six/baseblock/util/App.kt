package com.six.baseblock.util

import android.app.Application

object App {
    private lateinit var application: Application
    private lateinit var versionName: String
    private var versionCode: Int = 0
    private lateinit var applicationId: String
    private var debug: Boolean = false
    private lateinit var flavor: String
    private lateinit var buildType: String
    fun init(app: Application, versionName: String, versionCode: Int, applicationId: String, debug: Boolean, flavor: String, buildType: String) {
        application = app
        App.versionName = versionName
        App.versionCode = versionCode
        App.applicationId = applicationId
        App.debug = debug
        App.flavor = flavor
        App.buildType = buildType
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Application> context(): T {
        return application as T
    }

    fun appVersionName(): String {
        return versionName
    }

    fun appVersionCode(): Int {
        return versionCode
    }

    fun applicationId(): String {
        return applicationId
    }

    fun debug(): Boolean {
        return debug
    }

    fun flavor(): String {
        return flavor
    }

    fun buildType(): String {
        return buildType
    }
}