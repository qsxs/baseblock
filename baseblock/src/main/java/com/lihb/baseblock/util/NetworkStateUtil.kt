package com.lihb.baseblock.util

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executors


object NetworkStateUtil : MutableLiveData<Boolean>() {
    private const val TAG = "NetworkStateUtil"
    private var online = false
    private val data by lazy { MutableLiveData<Boolean>() }
    private val executors = Executors.newFixedThreadPool(2)

    fun init(app: Application) {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback: NetworkCallback = object : NetworkCallback() {
            /**
             * 网络可用的回调连接成功
             */
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                onChange()
            }

            /**
             * 网络不可用时调用和onAvailable成对出现
             */
            override fun onLost(network: Network) {
                super.onLost(network)
                onChange()
            }

            /**
             * 在网络连接正常的情况下，丢失数据会有回调 即将断开时
             */
            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                onChange()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                onChange()
            }

            override fun onLinkPropertiesChanged(
                network: Network,
                linkProperties: LinkProperties
            ) {
                super.onLinkPropertiesChanged(network, linkProperties)
                onChange()
            }

            /**
             * 网络缺失network时调用
             */
            override fun onUnavailable() {
                super.onUnavailable()
                onChange()
            }
        }
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { //API 大于26时
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> { //API 大于21时
                val builder = NetworkRequest.Builder()
                val request = builder.build()
                connectivityManager.registerNetworkCallback(request, networkCallback)
            }
            else -> { //低版本
                app.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(c: Context?, intent: Intent?) {
//                            Logger.d("收到广播：${intent?.action}")
                        onChange()
                    }
                }, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }

        onChange()
    }

    @Synchronized
    fun getAsLiveData(): LiveData<Boolean> {
        return data
    }

    private fun onChange() {
        check { isConnect ->
            data.postValue(isConnect)
        }
    }

    fun isOnline() = online

    fun check(callback: ((Boolean) -> Unit)?) {
        executors.execute {
            val checkIsOnline = checkIsOnline()
            callback?.invoke(checkIsOnline)
        }
    }

    private fun checkIsOnline(): Boolean {
//        Log.i(TAG, "isOnline,start${System.currentTimeMillis()}")
        val runtime = Runtime.getRuntime()
        try {
            val exec = runtime.exec("ping -c 2 www.baidu.com")
            val exitValue = exec.waitFor()
//            Log.i(TAG, "isOnline,end${System.currentTimeMillis()}")
            online = (exitValue == 0)
            return online
        } catch (e: Throwable) {
            e.printStackTrace()
        }
//        Log.i(TAG, "isOnline,end${System.currentTimeMillis()}")
        online = false
        return false
    }


}