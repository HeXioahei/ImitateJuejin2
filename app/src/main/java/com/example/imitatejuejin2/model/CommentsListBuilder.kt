package com.example.imitatejuejin2.model

/**
 *      desc     ： 创建评论列表的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.util.Log
import com.example.imitatejuejin2.data.basedata.GetCommentsData
import com.example.imitatejuejin2.requestinterface.article.GetCommentsService
import com.example.imitatejuejin2.data.response.GetCommentsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommentsListBuilder {

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
                    Flag.setHasSetCommentsList(true)
                }

                override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}