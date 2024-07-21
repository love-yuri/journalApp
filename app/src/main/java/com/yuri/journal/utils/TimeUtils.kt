package com.yuri.journal.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtils {
    val now: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }

    val now_no_sec: String
        get() {
            val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
            return sdf.format(Date())
        }


    /**
     * 格式时间，格式化规则
     * 如果是今天/昨天 则显示 今天/昨天 月-日
     * 如果是本周 则显示 周几 月-日
     * 否则 年-月-日
     */
    fun String.formatDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateTime = dateFormat.parse(this)

        val today = Calendar.getInstance()
        val calendar = Calendar.getInstance().apply {
            time = dateTime!!
        }

        return when {
            isSameDay(calendar, today) -> {
                val monthDayFormat = SimpleDateFormat("MM月-dd日", Locale.getDefault())
                "今天 ${monthDayFormat.format(calendar.time)}"
            }

            calendar.isYesterday() -> {
                val monthDayFormat = SimpleDateFormat("MM月-dd日", Locale.getDefault())
                "昨天 ${monthDayFormat.format(calendar.time)}"
            }

            calendar.isSameWeek(today) -> {
                val dayOfWeekFormat = SimpleDateFormat("E", Locale.getDefault())
                val monthDayFormat = SimpleDateFormat("MM月-dd日", Locale.getDefault())
                "${dayOfWeekFormat.format(calendar.time)} ${monthDayFormat.format(calendar.time)}"
            }

            else -> {
                val dd = SimpleDateFormat("yyyy年-MM月-dd日", Locale.getDefault())
                dd.format(calendar.time)
            }
        }
    }

    private fun isSameDay(calendar1: Calendar, calendar2: Calendar): Boolean {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    private fun Calendar.isYesterday(): Boolean {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && get(Calendar.DAY_OF_YEAR) == yesterday.get(
            Calendar.DAY_OF_YEAR
        )
    }

    private fun Calendar.isSameWeek(v: Calendar): Boolean {
        return get(Calendar.YEAR) == v.get(Calendar.YEAR) && get(Calendar.WEEK_OF_YEAR) == v.get(
            Calendar.WEEK_OF_YEAR
        )
    }
}