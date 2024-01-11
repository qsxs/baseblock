package com.lihb.baseblock.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.LinkedList
import kotlin.reflect.KClass
import kotlin.system.exitProcess

/**
 * 应用程序Activity管理类:用于Activity管理和应用程序退出
 */
object ActManager {
    private val activityLinkedList by lazy { LinkedList<Activity>() }

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityLinkedList.push(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                activityLinkedList.remove(activity)
            }

        })
    }

    fun activityStack() = activityLinkedList

    @Deprecated(
        "弃用",
        replaceWith = ReplaceWith("backToActivity(aClass)"),
        level = DeprecationLevel.WARNING
    )
    fun backToActivityJava(aClass: Class<out Activity>): Boolean {
        return backToActivity(aClass)
    }

    /**
     * 回到最近一个activity
     */
    fun backToActivity(aClass: Class<out Activity>): Boolean {
        if (activityLinkedList.firstOrNull() == null) return false
        var b = true
        while (b) {
            val firstOrNull = activityLinkedList.firstOrNull() ?: return false
            if (firstOrNull.javaClass == aClass) {
                b = false
            } else {
                activityLinkedList.pop().finish()
            }
        }
        return !b
    }

    fun backToActivity(kClass: KClass<out Activity>): Boolean {
        return backToActivity(kClass.java)
    }

    fun isExistActivity(aClass: Class<out Activity>): Boolean {
        return activityStack().firstOrNull { it::class.java == aClass } != null
    }

    fun isExistActivity(kClass: KClass<out Activity>): Boolean {
        return isExistActivity(kClass.java)
    }

    /**
     * 获取当前Activity(堆栈中最后一个压入的)
     */
    fun currentActivity(): Activity? {
        return activityLinkedList.peekFirst()
    }

    /**
     * 结束当前Activity(堆栈中最后一个压入的)
     */
    fun finishActivity() {
        activityLinkedList.pop().finish()
    }

    /**
     * 结束指定的Activity
     */
    private fun finishActivity(activity: Activity?) {
        activity?.finish()
    }

    /**
     * 结束指定类名最新的一个Activity
     */
    fun finishActivityNewestOne(aClass: Class<out Activity>) {
        for (activity in activityLinkedList) {
            if (activity.javaClass == aClass) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束指定类名最新的一个Activity
     */
    fun finishActivityNewestOne(kClass: KClass<out Activity>) {
        finishActivityNewestOne(kClass.java)
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(aClass: Class<out Activity>) {
        for (activity in activityLinkedList) {
            if (activity.javaClass == aClass) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(kClass: KClass<out Activity>) {
        finishActivity(kClass.java)
    }


    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        while (activityLinkedList.peekFirst() != null) {
            finishActivity(activityLinkedList.pop())
        }
    }

    fun activitySize(): Int = activityLinkedList.size

    /**
     * 退出应用程序
     */
    fun appExit() {
        finishAllActivity()
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }
}
