package com.six.baseblock.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

open class BaseSpHelper(app: Application, spName: String) {
    private var sp: SharedPreferences = app.getSharedPreferences(spName, Context.MODE_PRIVATE)
    private val editor = sp.edit()

    protected fun setLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }

    protected fun getLong(key: String, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }

    protected fun setString(key: String, value: String?) {
        if (value == null) {
            editor.remove(key)
        } else {
            editor.putString(key, value)
        }
        editor.apply()
    }

    protected fun getString(key: String, defValue: String?): String? {
        return sp.getString(key, defValue)
    }

    protected fun setBoolean(key: String, value: Boolean?) {
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    protected fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    protected fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.apply()
    }

    protected fun getFloat(key: String, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }

    protected fun setInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    protected fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    protected fun setStringSet(key: String, value: Set<String>) {
        editor.putStringSet(key, value)
        editor.apply()
    }

    protected fun getStringSet(key: String, defValue: Set<String>?): Set<String>? {
        return sp.getStringSet(key, defValue)
    }

    protected fun remove(key: String) {
        editor.remove(key)
        editor.apply()
    }
}