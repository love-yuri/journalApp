package com.yuri.journal.constants

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.yuri.journal.constants.SharedPreferencesConstant.SpType
import com.yuri.journal.constants.SharedPreferencesConstant.WebDavKey
import com.yuri.journal.utils.SharedPreferencesUtils.getSp
import com.yuri.journal.utils.SharedPreferencesUtils.setSp
import com.yuri.journal.viewModel.JournalViewModel
import java.io.File

object GlobalSharedConstant {
    private lateinit var journalViewModel_: JournalViewModel
    private lateinit var application: Application
    val journalViewModel: JournalViewModel get() = journalViewModel_

    /**
     * webDav 用户名
     */
    var account: String?
        get() = application.getSp(SpType.WEBDAV, WebDavKey.ACCOUNT)
        set(value) = application.setSp(SpType.WEBDAV, WebDavKey.ACCOUNT, value)

    /**
     * webDav 密码
     */
    var password: String?
        get() = application.getSp(SpType.WEBDAV, WebDavKey.PASSWORD)
        set(value) = application.setSp(SpType.WEBDAV, WebDavKey.PASSWORD, value)

    /**
     * 临时文件夹
     */
    val tempDir: File get() {
        val file = File("${application.filesDir}/temp")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 数据库所在目录
     */
    val dbDir: File get() {
        val path = application.getDatabasePath(DataBaseConstant.DATABASE_FILE_NAME).parent
        if (path.isNullOrEmpty()) {
            error("数据库目录获取失败!!!")
        }
        return File(path)
    }

    /**
     * 初始化
     */
    fun init(application: Application) {
        if (::application.isInitialized) {
            return
        }

        journalViewModel_ = ViewModelProvider
            .AndroidViewModelFactory(application)
            .create(JournalViewModel::class.java)

        this.application = application
    }
}