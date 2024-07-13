package com.yuri.journal.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    val now: String get() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    val now_no_sec: String get() {
        val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

}