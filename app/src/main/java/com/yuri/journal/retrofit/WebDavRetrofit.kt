package com.yuri.journal.retrofit

import android.util.Base64
import com.yuri.journal.common.log
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.constants.WebDavConfig.DB_FOLDER
import com.yuri.journal.constants.WebDavConfig.HOST
import com.yuri.journal.utils.XmlUtils.parseWebDavXml
import okhttp3.MediaType

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Url
import java.io.File
import java.io.FileOutputStream

object WebDavRetrofit {
    val service: WebDavService by lazy {
        retrofit.create(WebDavService::class.java)
    }

    interface WebDavService {
        @Headers("Depth: 1")
        @HTTP(method = "PROPFIND", hasBody = false)
        suspend fun dir(
            @Url url: String,
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String
        ): ResponseBody

        @HTTP(method = "MKCOL", hasBody = false)
        suspend fun mkdir(
            @Url url: String,
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String
        ): ResponseBody

        @PUT
        suspend fun uploadFile(
            @Url url: String,
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Header("Content-Length") contentLength: Long,
            @Body file: RequestBody
        ): Response<ResponseBody>

        @GET
        suspend fun downloadFile(
            @Url url: String,
            @Header("Authorization") authorization: String
        ): Response<ResponseBody>

    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 获取数据库文件所在目录
     */
    suspend fun dir(
        account: String? = GlobalSharedConstant.account,
        password: String? = GlobalSharedConstant.password
    ): List<WebdavFile> {
        if (account.isNullOrEmpty() || password.isNullOrEmpty()) {
            return listOf()
        }

        try {
            val response = service.dir(
                "dav/$DB_FOLDER",
                "Basic ${Base64.encodeToString("$account:$password".toByteArray(), Base64.NO_WRAP)}",
                "text/xml"
            )
            return response.string().parseWebDavXml()
        } catch (e: Exception) {
            log.e("发生错误错误: $e")
            return listOf()
        }
    }

    /**
     * 创建目录
     */
    suspend fun mkdir(account: String, password: String): Boolean {
        try {
            service.mkdir(
                "dav/$DB_FOLDER",
                "Basic ${Base64.encodeToString("$account:$password".toByteArray(), Base64.NO_WRAP)}",
                "text/xml"
            )
            return true
        } catch (e: Exception) {
            log.e("发生错误错误: $e")
            return false
        }
    }

    /**
     * 上传文件
     */
    suspend fun upload(account: String, password: String, file: File, fileName: String = file.name): Boolean {
        try {
            return try {
                if (!file.exists()) {
                    return false
                }
                val url = "dav/$DB_FOLDER/$fileName"
                val authorization = "Basic ${Base64.encodeToString("$account:$password".toByteArray(), Base64.NO_WRAP)}"
                val contentType = "application/octet-stream"
                val contentLength = file.length()
                val mediaType = MediaType.parse(contentType)
                val requestBody = RequestBody.create(mediaType, file)

                val response = service.uploadFile(url, authorization, contentType, contentLength, requestBody)
                log.i(response.toString())
                response.code() == 201 || response.code() == 204
            } catch (e: Exception) {
                log.e("解析错误: ${e.message}")
                false
            }
        } catch (e: Exception) {
            log.e("发生错误错误: $e")
            return false
        }
    }

    /**
     * 下载文件
     */
    suspend fun download(
        account: String? = GlobalSharedConstant.account,
        password: String? = GlobalSharedConstant.password,
        fileName: String,
        destination: File
    ): Boolean {
        if (account.isNullOrEmpty() || password.isNullOrEmpty()) {
            return false
        }

        val url = "dav/$DB_FOLDER/$fileName"
        val authorization = "Basic ${Base64.encodeToString("$account:$password".toByteArray(), Base64.NO_WRAP)}"

        return try {
            val response = service.downloadFile(url, authorization)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val inputStream = body.byteStream()
                    val outputStream = FileOutputStream(destination)
                    inputStream.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    true
                } ?: false
            } else {
                log.e("下载错误: $response")
                false
            }
        } catch (e: Exception) {
            log.e("下载错误: ${e.message}")
            false
        }
    }

    data class WebdavFile(
        var isFile: Boolean = true,
        var isFolder: Boolean = false,
        var path: String? = null,
        var parent: String? = null,
        var children: List<WebdavFile>? = null,
        var fileName: String? = null
    )
}