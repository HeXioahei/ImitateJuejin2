package com.example.imitatejuejin2.model

import android.util.Log
import com.example.imitatejuejin2.data.AuthorBrief
import com.example.imitatejuejin2.requestinterface.mine.GetMyInfoService
import com.example.imitatejuejin2.data.response.GetMyInfoResponse
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
            .getMyInfoService(Authorization)
            .enqueue(object : Callback<GetMyInfoResponse> {
                override fun onResponse(
                    call: Call<GetMyInfoResponse>,
                    response: Response<GetMyInfoResponse>,
                ) {
                    val back = response.body()
                    Log.d("a","a")
                    Log.d("haed_image", "${back?.data?.head_image}")
                    Log.d("username", "${back?.data?.username}")
                    if (back?.data != null) {
                        authorBrief = back.data
                        Log.d("head_image1", authorBrief.head_image)
                        FlagBuilder.setHasSetAuthorBrief(true)
                    }
                }

                override fun onFailure(call: Call<GetMyInfoResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}