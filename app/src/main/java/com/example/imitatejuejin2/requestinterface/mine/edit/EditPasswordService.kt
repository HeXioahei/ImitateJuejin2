package com.example.imitatejuejin2.requestinterface.mine.edit

import com.example.imitatejuejin2.data.response.BaseResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface EditPasswordService {
    @PUT("/users/changes/password/")
    fun editPasswordService(
        @Query("old_password") old_password: String,
        @Query("new_password") new_password: String,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse>
}