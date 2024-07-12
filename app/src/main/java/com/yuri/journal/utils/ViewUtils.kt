package com.yuri.journal.utils

import android.app.Activity
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE

object ViewUtils {

    // 返回是否在这个view里
    fun View.inView(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()

        val toolbarRect = Rect()
        getGlobalVisibleRect(toolbarRect)
        return toolbarRect.contains(x, y)
    }

    fun Activity.showSoftInput(view: View) {
        view.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}