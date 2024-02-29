package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.data.response.GetMyUsernameResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetMyUsernameService {
    @GET("/users/information/")
    fun getMyUsernameService(
        @Header("Authorization") Authorization: String
    ): Call<GetMyUsernameResponse>
}