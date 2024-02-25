package com.example.imitatejuejin2.requestinterface.begin

import com.example.imitatejuejin2.data.basedata.AccessToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RefreshTokenService {
    @GET("/users/login")
    fun refreshTokenService(
        @Header("Authorization") Authorization: String
    ): Call<AccessToken>
}