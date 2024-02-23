package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.data.response.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface EditHeadImageService {
    @Multipart
    @PUT("/users/changes/head/")
    fun editHeadImageService(
        @Header("Authorization") Authorization: String,
        @Part headImage: MultipartBody.Part
    ): Call<BaseResponse>
}