package com.yuri.journal.utils

import android.text.TextUtils
import android.widget.TextView
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer

object StringUtils {
    inline fun <reified T> String.parseJson(): T {
        return Json.decodeFromString(serializersModule.serializer(), this)
    }

    fun TextView.ellipsize(maxLength: Int) {
        val originalText = text.toString()
        if (originalText.length > maxLength) {
            val ellipsizedText = TextUtils.ellipsize(originalText, paint, maxLength.toFloat(), TextUtils.TruncateAt.END)
            text = ellipsizedText
        }
    }

    fun String.baseFileName(): String {
        val index = lastIndexOf('.')
        return if (index > 0) {
            substring(0, index)
        } else {
            this
        }
    }
}