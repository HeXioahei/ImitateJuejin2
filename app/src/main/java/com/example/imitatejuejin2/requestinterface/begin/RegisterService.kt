package com.example.imitatejuejin2.requestinterface.begin

import com.example.imitatejuejin2.data.response.BaseResponse
import okhttp3.FormBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RegisterService {
    @Multipart
    @POST("/users/register/")
    fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Part headImage: MultipartBody.Part
    ): Call<BaseResponse>
}