package com.yuri.journal.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yuri.journal.common.log
import com.yuri.journal.constants.DataBaseConstant
import com.yuri.journal.constants.SharedPreferencesConstant
import com.yuri.journal.database.AppDatabase.dbFiles
import com.yuri.journal.retrofit.WebDavRetrofit
import com.yuri.journal.utils.FileUtils
import com.yuri.journal.utils.SharedPreferencesUtils.getSp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

// 备份数据库文件
class BackupDbService: Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        uploadDatabase()
        return START_NOT_STICKY
    }

    /**
     * 上传数据库文件
     */
    private fun uploadDatabase() {
        val files = applicationContext.dbFiles()

        val zipFile = File.createTempFile(DataBaseConstant.BACKUP_DB_FILE_NAME, DataBaseConstant.BACKUP_DB_FILE_NAME).apply {
            deleteOnExit()
        }

        FileUtils.compressFilesToZip(zipFile.path, *files.toTypedArray())

        val account = applicationContext.getSp(SharedPreferencesConstant.SpType.WEBDAV, SharedPreferencesConstant.WebDavKey.ACCOUNT)
        val password = applicationContext.getSp(SharedPreferencesConstant.SpType.WEBDAV, SharedPreferencesConstant.WebDavKey.PASSWORD)
        log.i("account $account, password $password")
        if (account.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }
        coroutineScope.launch {
            WebDavRetrofit.upload(account, password, zipFile, DataBaseConstant.BACKUP_DB_FILE_NAME)
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}