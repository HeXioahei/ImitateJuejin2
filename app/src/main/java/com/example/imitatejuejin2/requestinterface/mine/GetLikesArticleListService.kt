package com.example.imitatejuejin2.requestinterface.mine

import com.example.imitatejuejin2.data.response.GetArticleListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetLikesArticleListService {
    @GET("/articles/likes")
    fun getLikesArticleListService (
        @Header("Authorization") Authorization:String
    ): Call<GetArticleListResponse>
}