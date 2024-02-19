package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.response.BaseResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface DeleteArticleService {
    @DELETE("/articles/{id}/del/")
    fun deleteService(
        @Path("id") id: String,
        @Header("Authorization") Authorization: String
    ): Call<BaseResponse>
}