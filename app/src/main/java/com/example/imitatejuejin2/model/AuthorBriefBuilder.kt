package com.example.imitatejuejin2.model

/**
 *      desc     ： 封装个人信息的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.util.Log
import com.example.imitatejuejin2.data.basedata.AuthorBrief
import com.example.imitatejuejin2.data.response.GetMyInfoResponse
import com.example.imitatejuejin2.requestinterface.mine.GetMyInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthorBriefBuilder {

    private var authorBrief: AuthorBrief = AuthorBrief("head_image", "username")

    fun getAuthorBrief(): AuthorBrief {
        Log.d("head_image2", authorBrief.head_image)
        return authorBrief
    }

    fun setAuthorBrief(Authorization: String) {
        ServiceCreator.create(GetMyInfoService::class.java)
            .getMyInfo(Authorization)
            .enqueue(object : Callback<GetMyInfoResponse> {
                override fun onResponse(
                    call: Call<GetMyInfoResponse>,
                    response: Response<GetMyInfoResponse>,
                ) {
                    val back = response.body()

                    Log.d("a","a")
                    Log.d("username", "${back?.data?.username}")

                    if (back?.data != null) {
                        Log.d("head_image1", authorBrief.head_image)
                        authorBrief = back.data
                        // 通知个人信息创建完毕
                        Flag.setHasSetAuthorBrief(true)
                    }
                }

                override fun onFailure(call: Call<GetMyInfoResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}