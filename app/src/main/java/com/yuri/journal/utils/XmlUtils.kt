package com.yuri.journal.utils

import com.yuri.journal.common.log
import com.yuri.journal.retrofit.WebDavRetrofit
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.util.Stack

object XmlUtils {
    fun String.parseWebDavXml(): List<WebDavRetrofit.WebdavFile> {
        val inputStream = ByteArrayInputStream(toByteArray()) // 将响应字符串转换为输入流

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()

        parser.setInput(inputStream, "utf-8") // 设置XmlPullParser的输入流

        var eventType = parser.eventType
        val fileList = mutableListOf<WebDavRetrofit.WebdavFile>()
        var currentFile: WebDavRetrofit.WebdavFile = WebDavRetrofit.WebdavFile()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "d:response" -> currentFile = WebDavRetrofit.WebdavFile()
                    "d:href" -> currentFile.path = parser.nextText()
                    "d:resourcetype" -> {
                        currentFile.isFile = parser.isEmptyElementTag
                        currentFile.isFolder = !parser.isEmptyElementTag
                    }
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.name == "d:response") {
                    fileList.add(currentFile)
                }
            }
            eventType = parser.next()
        }
        return fileList
    }
}