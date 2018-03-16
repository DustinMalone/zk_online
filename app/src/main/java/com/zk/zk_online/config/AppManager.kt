package com.pda.wph.config

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*


/**
 * Created by mukun on 2017/5/30.
 */
class AppManager {
    private var activityStack: Stack<Activity>? = null

    private var count = 0

    private fun AppManager() {}

    fun getCount(): Int {
        return count
    }

    fun setCount(c: Int) {
        this.count = c
    }

    /**
     * 单一实例
     */
    companion object {
        private var instance: AppManager? = null
        fun getAppManager(): AppManager {
            if (instance == null) {
                instance = AppManager()
            }
            return instance as AppManager
        }
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack<Activity>()
        }
        activityStack!!.add(activity)
        count++
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        val activity = activityStack!!.lastElement()
        return activity
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
        count--
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            count--
            activity = null
        }

    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        val delList = ArrayList<Activity>()
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                delList.add(activity)
            }
        }
        for (activity in delList) {
            finishActivity(activity)
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
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
        count = 0
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {

        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
        }

    }

    /**
     * 将指定Activity移除堆栈
     */
    fun removeActivityFromStack(activity: Activity) {
        activityStack!!.remove(activity)
    }

    /**
     * 搜索栈中是否存在当前类名Activity
     */
    operator fun contains(cls: Class<*>): Boolean {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                return true
            }
        }
        return false
    }
}