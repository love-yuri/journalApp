package com.yuri.journal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuri.journal.common.log
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.database.entity.JournalEntity
import kotlinx.coroutines.launch

class JournalViewModel: ViewModel() {
    val data: MutableLiveData<List<JournalEntity>> by lazy {
        MutableLiveData<List<JournalEntity>>().also {
            viewModelScope.launch {

                it.value = AppDatabase.journalDao.list()
                log.i("初始化..., ${it.value}")
            }
        }
    }
}