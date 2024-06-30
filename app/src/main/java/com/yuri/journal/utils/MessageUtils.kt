package com.yuri.journal.utils

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yuri.journal.R

/**
 * 全局消息提示工具
 * 上下文为activity
 */
class MessageUtils(
    private val context: Context
) {
    fun createToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun createDialog(title: String, msg: String, listener: DialogInterface.OnClickListener? = null) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(context.getString(R.string.ok), listener)
            .show()
    }

    fun createErrorDialog(msg: String) {
        createDialog(context.getString(R.string.error), msg)
    }

    fun valueCheck(view: Any?, msg: String): Boolean {
        if (view.toString().isEmpty()) {
            createDialog(context.getString(R.string.valueCheckError), msg)
            return false
        }
        return true
    }
}