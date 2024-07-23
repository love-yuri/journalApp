package com.yuri.journal

import android.app.Application
import com.yuri.journal.common.AppDestroyedCallBack
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class JournalApp: Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        // 初始化数据库
        AppDatabase.init(this)

        // 初始化Activity周期函数
        registerActivityLifecycleCallbacks(AppDestroyedCallBack)

        //初始化全局周期数据
        GlobalSharedConstant.init(this)

        // 初始化周期函数
        AppDestroyedCallBack.init(this)

    }
}