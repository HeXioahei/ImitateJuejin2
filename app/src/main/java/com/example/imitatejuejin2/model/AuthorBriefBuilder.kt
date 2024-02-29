package com.example.imitatejuejin2.model

import android.util.Log
import com.example.imitatejuejin2.data.basedata.AuthorBrief
import com.example.imitatejuejin2.data.response.GetMyHeadImageResponse
import com.example.imitatejuejin2.data.response.GetMyInfoResponse
import com.example.imitatejuejin2.requestinterface.mine.GetMyUsernameService
import com.example.imitatejuejin2.data.response.GetMyUsernameResponse
import com.example.imitatejuejin2.requestinterface.mine.GetMyHeadImageService
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

    fun setUsername(Authorization: String) {
        ServiceCreator.create(GetMyUsernameService::class.java)
            .getMyUsernameService(Authorization)
            .enqueue(object : Callback<GetMyUsernameResponse> {
                override fun onResponse(
                    call: Call<GetMyUsernameResponse>,
                    response: Response<GetMyUsernameResponse>,
                ) {
                    val back = response.body()
                    Log.d("a","a")
                    if (back?.data != null) {
                        authorBrief.username = back.data.username
                        Flag.setHasSetUsername(true)
                    }
                }

                override fun onFailure(call: Call<GetMyUsernameResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    fun setHeadImage(Authorization: String) {
        ServiceCreator.create(GetMyHeadImageService::class.java)
            .getMyHeadImage(Authorization)
            .enqueue(object : Callback<GetMyHeadImageResponse> {
                override fun onResponse(
                    call: Call<GetMyHeadImageResponse>,
                    response: Response<GetMyHeadImageResponse>,
                ) {
                    val back = response.body()
                    Log.d("a","a")
                    if (back?.data != null) {
                        authorBrief.head_image = back.data.image_url
                        Flag.setHasSetHeadImage(true)
                    }
                }

                override fun onFailure(call: Call<GetMyHeadImageResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
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
//                        Flag.setHasSetUsername(true)
                        Flag.setHasSetAuthorBrief(true)
                    }
                }

                override fun onFailure(call: Call<GetMyInfoResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}