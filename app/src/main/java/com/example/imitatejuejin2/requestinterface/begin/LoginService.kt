package com.example.imitatejuejin2.requestinterface.begin

import com.example.imitatejuejin2.response.LoginResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST("/users/login/")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<LoginResponse>
}