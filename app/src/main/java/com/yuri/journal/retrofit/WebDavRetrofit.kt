package com.yuri.journal.retrofit

import com.yuri.journal.constants.WebDavConfig.HOST
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

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
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    data class WebdavFile(
        val isFile: Boolean,
        val isFolder: Boolean,
        val path: String,
        val parent: String?,
        val children: List<WebdavFile>?
    )
}