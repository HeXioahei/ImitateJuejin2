package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.response.GetArticleListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetCollectArticleListService {
    @GET("/articles/collections")
    fun getCollectArticleListService(
        @Header("Authorization") Authorization:String
    ): Call<GetArticleListResponse>
}