package com.example.imitatejuejin2.requestinterface.article

import com.example.imitatejuejin2.response.BaseResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface HitService {
    @PUT("/articles/{id}/hits/")
    fun hit(
        @Path("id") id: String,
        @Header("Authorization") Authorization: String
    ) : Call<BaseResponse>
}