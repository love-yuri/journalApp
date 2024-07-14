package com.yuri.journal.common

import androidx.arch.core.internal.SafeIterableMap
import com.yuri.journal.utils.ClassUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class BaseMediatorLiveData<T>: MediatorLiveData<T>() {

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        super.addSource(source, onChanged)

        // 调用active 激活所有数据
        onActive()
    }
}