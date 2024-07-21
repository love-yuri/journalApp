package com.yuri.journal.utils

import android.content.Context
import android.content.SharedPreferences
import com.yuri.journal.constants.SharedPreferencesConstant.SpType
import com.yuri.journal.constants.SharedPreferencesConstant.BaseKey

object SharedPreferencesUtils {
    private val mutableMap = mutableMapOf<SpType, SharedPreferences>()

    /**
     * 获取 SharedPreferences 实例
     * 需要传入type
     */
    fun Context.getSp(type: SpType): SharedPreferences {
        if (!mutableMap.containsKey(type)) {
            mutableMap[type] = getSharedPreferences(type.name, Context.MODE_PRIVATE)
        }
        return mutableMap.getValue(type)
    }

    /**
     * 获取实例内容
     * 默认返回null
     */
    fun Context.getSp(type: SpType, key: BaseKey, default: String? = null): String? {
        if (!mutableMap.containsKey(type)) {
            mutableMap[type] = getSharedPreferences(type.name, Context.MODE_PRIVATE)
        }
        return mutableMap.getValue(type).getString(key.toString(), default)
    }

    /**
     * 设置实例内容
     */
    fun Context.setSp(type: SpType, key: Any, value: String?) {
        if (!mutableMap.containsKey(type)) {
            mutableMap[type] = getSharedPreferences(type.name, Context.MODE_PRIVATE)
        }
        mutableMap.getValue(type).edit().apply {
            putString(key.toString(), value)
            commit()
        }
    }

    /**
     * 删除实列内容
     * 传入key
     */
    fun Context.removeSp(type: SpType, key: Any) {
        if (!mutableMap.containsKey(type)) {
            mutableMap[type] = getSharedPreferences(type.name, Context.MODE_PRIVATE)
        }
        mutableMap.getValue(type).edit().apply {
            remove(key.toString())
            commit()
        }
    }
}