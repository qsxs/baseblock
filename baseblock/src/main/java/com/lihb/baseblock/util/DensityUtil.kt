package com.lihb.baseblock.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager

object DensityUtil {
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val result: Int
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            dip2px(context, 24f)
        }
        return result
    }

    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmStatic
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    @JvmStatic
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getWindowHeight(act: Activity): Int {
        return androidx.window.layout.WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(act).bounds.height()
    }

    fun getWindowWidth(act: Activity): Int {
        return androidx.window.layout.WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(act).bounds.width()
    }

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Activity): Int {
        val dm = DisplayMetrics()
        // 获取屏幕信息
        context.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
        //        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
        //        SCREEN_WIDTH = dm.widthPixels;
        //        SCREEN_HEIGHT = dm.heightPixels;
        //        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(context: Activity): Int {
        val dm = DisplayMetrics()
        // 获取屏幕信息
        context.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getDecorViewTop(context: Activity): Int {
        val frame = Rect()
        context.window.decorView.getWindowVisibleDisplayFrame(frame)
        return frame.top
    }
}
