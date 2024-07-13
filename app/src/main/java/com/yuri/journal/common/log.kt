package com.yuri.journal.common

import android.util.Log

@Suppress("ClassName")
object log {
    fun i(message: String) {
        Log.i("yuri", message)
    }

    fun e(message: String) {
        Log.e("yuri", message)
    }
}