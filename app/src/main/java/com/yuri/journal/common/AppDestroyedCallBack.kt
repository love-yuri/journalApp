package com.yuri.journal.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * app销毁后的回调方法
 */
object AppDestroyedCallBack: Application.ActivityLifecycleCallbacks {
    private var activityCount = 0
    private val callBackList = mutableListOf<() -> Unit>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityCount++
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            log.i("所有Activity都被销毁....")
            callBackList.forEach { it() }
        }
    }

    /**
     * 添加回调函数
     */
    fun addCallBack(method: () -> Unit) {
        callBackList.add(method)
    }

    /**
     * 删除回调函数
     */
    fun removeCallBack(method: () -> Unit): Boolean {
        return callBackList.remove(method)
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityResumed(activity: Activity) {}
}