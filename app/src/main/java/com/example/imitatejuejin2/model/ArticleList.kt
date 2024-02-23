package com.example.imitatejuejin2.model

import android.util.Log
import com.example.imitatejuejin2.data.Article
import com.example.imitatejuejin2.requestinterface.mine.GetCollectArticleListService
import com.example.imitatejuejin2.requestinterface.head.GetHotArticleListService
import com.example.imitatejuejin2.requestinterface.mine.GetLikesArticleListService
import com.example.imitatejuejin2.requestinterface.mine.GetMyArticleListService
import com.example.imitatejuejin2.requestinterface.head.GetNewArticleListService
import com.example.imitatejuejin2.data.response.GetArticleListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ArticleList {
    private var newList: MutableList<Article> = mutableListOf()
    private var hotList: MutableList<Article> = mutableListOf()
    private var myList: MutableList<Article> = mutableListOf()
    private var likeList: MutableList<Article> = mutableListOf()
    private var collectList: MutableList<Article> = mutableListOf()
    private var blankList: MutableList<Article> = mutableListOf()

    fun createHotArticleList(Authorization: String) {
        val appService = ServiceCreator.create(GetHotArticleListService::class.java)
        appService.getHotArticleListService(Authorization).enqueue(object : Callback<GetArticleListResponse> {
            override fun onResponse(
                call: Call<GetArticleListResponse>,
                response: Response<GetArticleListResponse>,
            ) {
                val back = response.body()
                Log.d("list1", "list1")
                if (back?.data != null) {
                    hotList = back.data
                }
                FlagBuilder.setHasSetHotList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun createNewArticleList(Authorization: String) {
        val appService = ServiceCreator.create(GetNewArticleListService::class.java)
        appService.getNewArticleListService(Authorization).enqueue(object : Callback<GetArticleListResponse> {
            override fun onResponse(
                call: Call<GetArticleListResponse>,
                response: Response<GetArticleListResponse>,
            ) {
                val back = response.body()
                Log.d("list2", "list2")
                Log.d("newcode", back?.code.toString())
                if (back?.data != null) {
                    newList = back.data
                    Log.d("newList", newList.size.toString())
                    Log.d("newListContent", newList.toString())
                    Log.d("newListContent1", newList[0].toString())
                    Log.d("newListContent11", newList[0].author.toString())
                    Log.d("newListContent111", newList[0].author.username)

                }

                FlagBuilder.setHasSetNewList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun createMyArticleList(Authorization: String) {
        val appService = ServiceCreator.create(GetMyArticleListService::class.java)
        appService.getMyArticleListService(Authorization).enqueue(object : Callback<GetArticleListResponse> {
            override fun onResponse(
                call: Call<GetArticleListResponse>,
                response: Response<GetArticleListResponse>,
            ) {
                val back = response.body()
                Log.d("list3", "list3")
                Log.d("mycode", back?.code.toString())
                if (back?.data != null) {
                    myList = back.data
                    Log.d("myList", myList.size.toString())

                }

                FlagBuilder.setHasSetMyList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun createCollectArticleList(Authorization: String) {
        val appService = ServiceCreator.create(GetCollectArticleListService::class.java)
        appService.getCollectArticleListService(Authorization).enqueue(object : Callback<GetArticleListResponse> {
            override fun onResponse(
                call: Call<GetArticleListResponse>,
                response: Response<GetArticleListResponse>,
            ) {
                val back = response.body()
                Log.d("list4", "list4")
                if (back?.data != null) {
                    collectList = back.data

                }

                FlagBuilder.setHasSetCollectList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun createLikesArticleList(Authorization: String) {
        val appService = ServiceCreator.create(GetLikesArticleListService::class.java)
        appService.getLikesArticleListService(Authorization).enqueue(object : Callback<GetArticleListResponse> {
            override fun onResponse(
                call: Call<GetArticleListResponse>,
                response: Response<GetArticleListResponse>,
            ) {
                val back = response.body()
                Log.d("list5", "list5")
                if (back?.data != null) {
                    likeList = back.data

                }

                FlagBuilder.setHasSetLikeList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getNewList(): MutableList<Article> {
        return newList
    }

    fun getHotList(): MutableList<Article> {
        return hotList
    }

    fun getMyList(): MutableList<Article> {
        return myList
    }

    fun getCollectList(): MutableList<Article> {
        return collectList
    }

    fun getLikeList(): MutableList<Article> {
        return likeList
    }

    fun getBlankList(): MutableList<Article> {
        return blankList
    }

    fun setAllArticleList(Authorization: String) {
        createNewArticleList(Authorization)
        createHotArticleList(Authorization)
        createMyArticleList(Authorization)
        createLikesArticleList(Authorization)
        createCollectArticleList(Authorization)
    }
}