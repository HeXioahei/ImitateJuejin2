package com.example.imitatejuejin2.model

import android.util.Log
import com.example.imitatejuejin2.data.GetCommentsData
import com.example.imitatejuejin2.requestinterface.article.GetCommentsService
import com.example.imitatejuejin2.data.response.GetCommentsResponse
import com.example.imitatejuejin2.ui.activity.ArticleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommentsList {

    private var parentCommentsList: MutableList<GetCommentsData> = mutableListOf()

    fun getParentCommentsList(): MutableList<GetCommentsData> {
        return parentCommentsList
    }

    fun createParentCommentsList(articleId: String) {
        ServiceCreator.create(GetCommentsService::class.java)
            .getCommentService(articleId)
            .enqueue(object : Callback<GetCommentsResponse> {
                override fun onResponse(
                    call: Call<GetCommentsResponse>,
                    response: Response<GetCommentsResponse>,
                ) {
                    val back = response.body()
                    val code = back?.baseResponse?.code
                    if (back?.data != null) {
                        parentCommentsList = back.data
                        Log.d("createComments", "success")
                    }
                    FlagBuilder.setHasSetCommentsList(true)
                }

                override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}