package com.example.imitatejuejin2.requestinterface.end

import com.example.imitatejuejin2.response.BaseResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface ExitService {
    @POST("/users/exit/")
    fun exitService (
        @Header("Authorization") Authorization: String
    ) : Call<BaseResponse>
}