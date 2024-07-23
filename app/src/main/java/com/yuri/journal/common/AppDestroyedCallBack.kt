package com.yuri.journal.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.yuri.journal.constants.SharedPreferencesConstant
import com.yuri.journal.database.AppDatabase.dbFiles
import com.yuri.journal.retrofit.WebDavRetrofit
import com.yuri.journal.utils.FileUtils
import com.yuri.journal.utils.SharedPreferencesUtils.getSp
import com.yuri.journal.utils.SharedPreferencesUtils.getSp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * app销毁后的回调方法
 */
object AppDestroyedCallBack: Application.ActivityLifecycleCallbacks {
    private var activityCount = AtomicInteger(0)
    private val callBackList = mutableListOf<() -> Unit>()
    private lateinit var application: Application
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    // 初始化
    fun init(context: Application) {
        if (::application.isInitialized) {
            return
        }
        application = context
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityCount.getAndIncrement()
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityCount.getAndDecrement()
        if (activityCount.get() == 0) {
            try {
                updateDb()
                log.i("执行完毕")
                callBackList.forEach { it() }
            } catch (e: Exception) {
                log.e("退出异常: ${e.message}")
            }

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

    private fun updateDb() {
        log.i("回调")
        val files = application.dbFiles()
        log.i("files $files")
        val account = application.getSp(SharedPreferencesConstant.SpType.WEBDAV, SharedPreferencesConstant.WebDavKey.ACCOUNT)
        val password = application.getSp(SharedPreferencesConstant.SpType.WEBDAV, SharedPreferencesConstant.WebDavKey.PASSWORD)
        log.i("account $account, password $password")
        if (account.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }

        val file = File.createTempFile("temp", "db.zip").apply {
            deleteOnExit()
        }
        log.i("files 2 $files")

        FileUtils.compressFilesToZip(file.path, *files.toTypedArray())
        log.i("上传: ${file.path}")
        applicationScope.launch {
            WebDavRetrofit.upload(account, password, file)
        }
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityResumed(activity: Activity) {}
}