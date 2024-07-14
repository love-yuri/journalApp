package com.yuri.journal.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.app.NotificationManagerCompat


object ViewUtils {

    // 返回是否在这个view里
    fun View.inView(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()

        val toolbarRect = Rect()
        getGlobalVisibleRect(toolbarRect)
        return toolbarRect.contains(x, y)
    }

    /**
     * 显示软键盘
     */
    fun Activity.showSoftInput(view: View) {
        view.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 检查是否有通知权限
     */
    fun Activity.isOpenNotify(): Boolean {
        try {
            val from = NotificationManagerCompat.from(this)
            return from.areNotificationsEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 前往设置通知权限
     */
    fun Activity.goToSetNotify() {
        startActivity(Intent().apply {
            setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        })
    }
}