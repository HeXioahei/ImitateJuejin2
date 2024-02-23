package com.example.imitatejuejin2.requestinterface.article

import com.example.imitatejuejin2.data.response.BaseResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WriteCommentService {
    @POST("/articles/{id}/comments/")
    fun writeCommentService(
        @Path("id") id: String,
        @Query("comment") comment: String,
        @Query("time") time: String,
        @Query("last_cid") last_cid: String,
        @Query("level") level: String,
        @Header("Authorization") Authorization: String
    ) : Call<BaseResponse>
}