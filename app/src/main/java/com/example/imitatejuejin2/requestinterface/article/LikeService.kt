package com.example.imitatejuejin2.requestinterface.article

import com.example.imitatejuejin2.response.BaseResponse
import okhttp3.FormBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface LikeService {
    @PUT("/articles/{id}/likes/")
    fun like(
        @Path("id") id: String,
        @Header("Authorization") Anthorization: String,
        @Body status: FormBody
    ): Call<BaseResponse>
}