package com.six.baseblock.util

import android.app.Activity
import java.util.*

/**
 * 应用程序Activity管理类:用于Activity管理和应用程序退出
 */
object AppManager {

//    companion object {


    private var activityStack: Stack<Activity>? = null
//        private var instance: AppManager? = null


//        fun getInstance(): AppManager {
//            if (instance == null) {
//                synchronized(AppManager::class.java) {
//                    instance = AppManager()
//                }
//            }
//            return instance!!
//        }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 回到最近一个activity
     */
    fun backToActivity(aClass: Class<*>): Boolean {
        if (activityStack == null) {
            activityStack = Stack()
        }

        for (activity in activityStack!!) {
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
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
        //        return activity;
    }

    /**
     * 结束当前Activity(堆栈中最后一个压入的)
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
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
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
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
    fun removeActivity(activity: Activity) {
        if (activityStack == null) {
            return
        }
        activityStack!!.remove(activity)
    }


    fun activitySize(): Int {
        return if (null == activityStack) {
            0
        } else activityStack!!.size
    }

    /**
     * 退出应用程序
     */
    fun appExit() {
        //        finishAllActivity();
        //友盟统计
        //        MobclickAgent.onKillProcess(App.context());
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }
//    }
}
