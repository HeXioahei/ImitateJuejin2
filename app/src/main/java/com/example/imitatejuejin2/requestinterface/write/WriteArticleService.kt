package com.example.imitatejuejin2.requestinterface.write

import com.example.imitatejuejin2.data.response.BaseResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface WriteArticleService {
    @POST("/articles/editor/")
    fun writeArticleService(
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("create_time") create_time: String,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse>
}