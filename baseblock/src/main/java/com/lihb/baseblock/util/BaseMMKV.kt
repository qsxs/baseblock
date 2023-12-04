package com.lihb.baseblock.util

import com.tencent.mmkv.MMKV

open class BaseMMKV(private val tag: String, private val mode: Mode = Mode.MULTI_PROCESS_MODE) {
    enum class Mode(int: Int) {
        SINGLE_PROCESS_MODE(MMKV.SINGLE_PROCESS_MODE),
        MULTI_PROCESS_MODE(MMKV.MULTI_PROCESS_MODE)
    }

    fun get(): MMKV {
        return MMKV.mmkvWithID(tag, mode.ordinal)!!//多进程模式
    }


    protected fun remove(key: String) {
        get().removeValueForKey(key)
    }

    protected fun setBoolean(key: String, value: Boolean) {
        get().encode(key, value)
    }

    protected fun getBoolean(key: String, defValue: Boolean): Boolean {
        return get().decodeBool(key, defValue)
    }

    protected fun setString(key: String, value: String?) {
        if (value.isNullOrEmpty()) {
            remove(key)
        } else {
            get().encode(key, value)
        }
    }

    protected fun getString(key: String, defValue: String? = null): String? {
        return get().decodeString(key, defValue) ?: defValue
    }

    protected fun setInt(key: String, value: Int?) {
        if (value == null) {
            remove(key)
        } else {
            get().encode(key, value)
        }
    }

    protected fun getInt(key: String, defValue: Int): Int {
        return get().decodeInt(key, defValue)
    }

    protected fun setLong(key: String, value: Long?) {
        if (value == null) {
            remove(key)
        } else {
            get().encode(key, value)
        }
    }

    protected fun getLong(key: String, defValue: Long): Long {
        return get().decodeLong(key, defValue)
    }

    protected fun setFloat(key: String, value: Float?) {
        if (value == null) {
            remove(key)
        } else {
            get().encode(key, value)
        }
    }

    protected fun getFloat(key: String, defValue: Float): Float {
        return get().decodeFloat(key, defValue)
    }

    protected fun setDouble(key: String, value: Double?) {
        if (value == null) {
            remove(key)
        } else {
            get().encode(key, value)
        }
    }

    protected fun getDouble(key: String, defValue: Double): Double {
        return get().decodeDouble(key, defValue)
    }
}