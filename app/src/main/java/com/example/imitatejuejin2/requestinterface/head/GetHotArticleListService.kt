package com.example.imitatejuejin2.requestinterface.head

import com.example.imitatejuejin2.response.GetArticleListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetHotArticleListService {
    @GET("/articles/hot")
    fun getHotArticleListService(
        @Header("Authorization") Authorization:String
    ): Call<GetArticleListResponse>
}