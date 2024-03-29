package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.data.response.GetMyInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetMyInfoService {
    @GET("/users/information/")
    fun getMyInfo(
        @Header("Authorization") Authorization: String
    ): Call<GetMyInfoResponse>
}