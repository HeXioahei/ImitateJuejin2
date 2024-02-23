package com.example.imitatejuejin2.requestinterface.head

import com.example.imitatejuejin2.data.response.GetArticleListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetNewArticleListService {
    @GET("/articles/new")
    fun getNewArticleListService(
        @Header("Authorization") Authorization:String
    ): Call<GetArticleListResponse>
}