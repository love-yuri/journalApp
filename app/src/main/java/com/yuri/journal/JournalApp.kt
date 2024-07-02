package com.yuri.journal

import android.app.Application
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.utils.MessageUtils

class JournalApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // 初始化数据库
        AppDatabase.init(this)
    }
}