package com.yuri.journal.utils

import android.app.Activity
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yuri.journal.R
import com.yuri.journal.constants.ActivityConstant.DEFAULT_NOTIFY_CHANNEL_ID
import com.yuri.journal.constants.ActivityConstant.DEFAULT_NOTIFY_CHANNEL_NAME

/**
 * 全局消息提示工具
 * 上下文为activity
 */
object MessageUtils {
    fun Context.createToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun View.createToast(msg: String) {
        context.createToast(msg)
    }

    /**
     * 创建对话框
     */
    fun Context.createDialog(title: String, msg: String, listener: DialogInterface.OnClickListener? = null) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(getString(R.string.ok), listener)
            .show()
    }

    fun Activity.createErrorDialog(msg: String) {
        createDialog(getString(R.string.error), msg)
    }

    /**
     * 发送通知
     */
    fun Context.notify(msg: String, intent: Intent? = null) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val intent = Intent(this, EditJournalActivity::class.java)
        var pendingIntent: PendingIntent? = null
        intent?.also {
            pendingIntent = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        val mChannel = NotificationChannel(DEFAULT_NOTIFY_CHANNEL_ID, DEFAULT_NOTIFY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        mChannel.setShowBadge(true)
        mChannel.enableLights(true)

        notificationManager.createNotificationChannel(mChannel)

        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("标题") //设置通知栏标题
            .setContentText(msg) //设置通知栏显示内容
            .setWhen(System.currentTimeMillis()) //通知产生的时间。
            .setSmallIcon(android.R.mipmap.sym_def_app_icon) //设置通知小ICON
            .setDefaults(NotificationManager.IMPORTANCE_HIGH)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true) // 自动关闭
            .setDefaults(Notification.DEFAULT_ALL) //.setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    android.R.mipmap.sym_def_app_icon
                )
            ) //设置通知大ICON
            .build()

        notificationManager.notify(1, notification)
    }
}