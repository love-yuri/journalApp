package com.yuri.journal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.yuri.journal.common.log
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.database.dao.JournalDao
import com.yuri.journal.database.entity.JournalEntity
import kotlinx.coroutines.launch

class JournalViewModel: ViewModel() {
    val data: MutableLiveData<MutableList<JournalEntity>> by lazy {
        MutableLiveData<MutableList<JournalEntity>>().also {
            log.i("初始化数据....")
            viewModelScope.launch {
                it.value = AppDatabase.journalDao.list().toMutableList()
            }
        }
    }

    /**
     * 插入数据
     * 异步执行
     */
    fun insert(entity: JournalEntity) {
        viewModelScope.launch {
            val res = AppDatabase.journalDao.insert(entity)
            if (res != 0L) {
                data.value?.add(0, entity.copy(
                    id = res.toInt()
                ))
                data.postValue(data.value)
            }
        }
    }

    /**
     * 更新视图数据
     */
    fun update(entity: JournalEntity) {
        if (entity.id == null) {
            log.e("id为空，更新失败!!")
            return
        }
        viewModelScope.launch {
            val res = AppDatabase.journalDao.update(entity)
            if (res != 0) {
                val index = data.value?.indexOfFirst { it.id == entity.id }
                if (index != null && index != -1) {
                    data.value?.set(index, entity)
                    data.postValue(data.value)
                }
            }
        }
    }

    /**
     * 删除数据
     */
    fun delete(id: Int) {
        viewModelScope.launch {
            val res = AppDatabase.journalDao.deleteById(id)
            if (res != 0) {
                val index = data.value?.indexOfFirst { it.id == id }
                if (index != null && index != -1) {
                    data.value?.removeAt(index)
                    data.postValue(data.value)
                }
            }
        }
    }
}