package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.response.BaseResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface EditHeadImageService {
    @PUT("/users/changes/head/")
    fun editHeadImageService(
        @Query("head_image") head_image: String,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse>
}