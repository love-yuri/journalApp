package com.yuri.journal.constants

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.yuri.journal.viewModel.JournalViewModel

object GlobalSharedConstant {
    private var isInit = false
    private lateinit var journalViewModel_: JournalViewModel
    val journalViewModel: JournalViewModel get() = journalViewModel_


    /**
     * 初始化
     */
    fun init(application: Application) {
        if (isInit) {
            return
        }
        journalViewModel_ = ViewModelProvider
            .AndroidViewModelFactory(application)
            .create(JournalViewModel::class.java)
        isInit = true
    }
}