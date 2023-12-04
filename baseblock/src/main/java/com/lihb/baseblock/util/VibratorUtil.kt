package com.lihb.baseblock.util

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity

/**
 * 振动工具类
 */
object VibratorUtil {
    fun vibrate(context: Context, milliseconds: Long = 200) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager =
                context.getSystemService(AppCompatActivity.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.vibrate(
                CombinedVibration.createParallel(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            )
        } else {
            val vibrator = context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(milliseconds)
            }
        }
    }
}