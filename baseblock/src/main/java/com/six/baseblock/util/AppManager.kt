package com.six.baseblock.util

import android.app.Activity
import java.util.*
import kotlin.system.exitProcess

/**
 * 应用程序Activity管理类:用于Activity管理和应用程序退出
 */
object AppManager {
    private var activityStack: Stack<Activity>? = null

    @JvmStatic
    fun activityStack(): Stack<Activity> {
        if (activityStack == null) {
            activityStack = Stack()
        }
        return activityStack!!
    }

    /**
     * 添加Activity到堆栈
     */
    @JvmStatic
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 回到最近一个activity
     */
    @JvmStatic
    fun backToActivity(aClass: Class<*>): Boolean {
        if (activityStack == null) {
            activityStack = Stack()
        }

        for (activity in activityStack()) {
            if (activity.javaClass == aClass) {
                var b = true
                while (b) {
                    if (activityStack!!.lastElement().javaClass == aClass) {
                        b = false
                    } else {
                        activityStack!!.pop().finish()
                    }
                }
                return true

            }
        }
        return false
    }

    /**
     * 获取当前Activity(堆栈中最后一个压入的)
     */
    @JvmStatic
    fun currentActivity(): Activity? {
        return activityStack?.lastElement()
        //        return activity;
    }

    /**
     * 结束当前Activity(堆栈中最后一个压入的)
     */
    @JvmStatic
    fun finishActivity() {
        val activity = activityStack?.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    private fun finishActivity(activity: Activity?) {
        activity?.finish()
    }

    /**
     * 结束指定类名的Activity
     */
    @JvmStatic
    fun finishActivity(aClass: Class<*>) {
        if (activityStack == null) {
            return
        }
        for (activity in activityStack!!) {
            if (activity.javaClass == aClass) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        var i = 0
        val size = activityStack().size
        while (i < size) {
            if (null != activityStack!![i]) {
                //				activityStack.get(i).finish();
                finishActivity(activityStack!![i])
            }
            i++
        }
        //		activityStack.clear();
    }

    /**
     * 从栈中移除Activity，但不调用Activity的finish方法，用于非手动调用finishActivity导致Activity destroy情况
     */
    @JvmStatic
    fun removeActivity(activity: Activity) {
        activityStack?.remove(activity)
    }

    @JvmStatic
    fun activitySize(): Int {
        return activityStack?.size ?: 0
    }

    /**
     * 退出应用程序
     */
    @JvmStatic
    fun appExit() {
        finishAllActivity()
        //友盟统计
        //        MobclickAgent.onKillProcess(App.context());
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }
//    }
}
