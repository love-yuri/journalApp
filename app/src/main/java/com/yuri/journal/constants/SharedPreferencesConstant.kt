package com.yuri.journal.constants

object SharedPreferencesConstant {
    interface BaseKey
    enum class SpType {
        WEBDAV, // webdav相关信息
    }

    // webDav相关配置
    enum class WebDavKey: BaseKey {
        ACCOUNT, // 用户名
        PASSWORD // 密码
    }
}