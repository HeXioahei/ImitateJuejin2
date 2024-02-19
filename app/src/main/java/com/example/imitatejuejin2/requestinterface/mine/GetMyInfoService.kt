package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.response.GetMyInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetMyInfoService {
    @GET("/users/information/")
    fun getMyInfoService(
        @Header("Authorization") Authorization: String
    ): Call<GetMyInfoResponse>
}