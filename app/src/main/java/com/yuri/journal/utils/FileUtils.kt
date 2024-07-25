package com.yuri.journal.utils

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
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

    /**
     * 解压文件
     */
    @Throws(IOException::class)
    fun decompressZip(zipFilePath: String, destinationPath: String) {
        val buffer = ByteArray(1024)

        // 创建目标文件夹
        val destDir = File(destinationPath)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }

        ZipInputStream(BufferedInputStream(FileInputStream(zipFilePath))).use { zipInputStream ->
            var zipEntry = zipInputStream.nextEntry

            while (zipEntry != null) {
                val newFilePath = destinationPath + File.separator + zipEntry.name
                val file = File(newFilePath)

                // 如果当前 entry 是文件夹，则创建对应的文件夹
                if (zipEntry.isDirectory) {
                    file.mkdirs()
                } else {
                    // 如果当前 entry 是文件，则解压文件到目标路径
                    file.createNewFile()
                    FileOutputStream(file).use { fileOutputStream ->
                        var length: Int
                        while (zipInputStream.read(buffer).also { length = it } > 0) {
                            fileOutputStream.write(buffer, 0, length)
                        }
                    }
                }

                zipEntry = zipInputStream.nextEntry
            }
        }
    }
}