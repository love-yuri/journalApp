package com.yuri.journal.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


object FileUtils {

    /**
     * 压缩文件
     */
    @Throws(IOException::class)
    fun compressFilesToZip(zipFilePath: String, vararg filesToCompress: String) {
        val buffer = ByteArray(1024)

        ZipOutputStream(FileOutputStream(zipFilePath)).use { zipOutputStream ->
            for (filePath in filesToCompress) {
                val file = File(filePath)
                val fileInputStream = FileInputStream(file)

                // 添加文件到 zip 压缩包
                zipOutputStream.putNextEntry(ZipEntry(file.name))

                var length: Int
                while ((fileInputStream.read(buffer).also { length = it }) > 0) {
                    zipOutputStream.write(buffer, 0, length)
                }

                zipOutputStream.closeEntry()
                fileInputStream.close()
            }
        }
    }
}