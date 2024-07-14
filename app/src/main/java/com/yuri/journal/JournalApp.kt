package com.yuri.journal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.yuri.journal.common.AppDestroyedCallBack
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.utils.MessageUtils

class JournalApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化数据库
        AppDatabase.init(this)

        // 初始化Activity周期函数
        registerActivityLifecycleCallbacks(AppDestroyedCallBack)

        //初始化全局周期数据
        GlobalSharedConstant.init(this)
    }
}