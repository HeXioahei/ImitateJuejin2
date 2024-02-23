package com.example.imitatejuejin2.requestinterface.article

import com.example.imitatejuejin2.data.response.GetCommentsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetCommentsService {
    @GET("/articles/{id}/comments/")
    fun getCommentService(
        @Path("id") id: String
    ): Call<GetCommentsResponse>
}