package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.data.response.GetMyHeadImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetMyHeadImageService {
    @GET("/users/images/paths")
    fun getMyHeadImage(
        @Header("Authorization") Authorization: String
    ): Call<GetMyHeadImageResponse>
}