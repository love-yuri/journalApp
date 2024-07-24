package com.yuri.journal.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.concurrent.atomic.AtomicInteger

/**
 * app销毁后的回调方法
 */
object AppDestroyedCallBack: Application.ActivityLifecycleCallbacks {
    private var activityCount = AtomicInteger(0)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityCount.getAndIncrement()
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityCount.getAndDecrement()
    }


    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityResumed(activity: Activity) {}
}