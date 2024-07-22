package com.yuri.journal.utils

import com.yuri.journal.common.log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream

object XmlUtils {
    fun parseXml(response: String) {
        val inputStream = ByteArrayInputStream(response.toByteArray()) // 将响应字符串转换为输入流

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()

        parser.setInput(inputStream, "utf-8") // 设置XmlPullParser的输入流

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                val tagName = parser.name

                // 根据需要解析特定的标签和属性，并进行相应处理
                if (tagName == "d:href") {
                    val text = parser.nextText() // 获取指定标签的文本内容

                    log.i("d:href ${parser.lineNumber}")
                    // 处理解析的数据
                }

            }

            eventType = parser.next()
        }
    }
}