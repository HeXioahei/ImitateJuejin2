package com.example.imitatejuejin2.model

/**
 *      desc     ： 创建文章列表的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.util.Log
import com.example.imitatejuejin2.data.basedata.Article
import com.example.imitatejuejin2.requestinterface.mine.GetCollectArticleListService
import com.example.imitatejuejin2.requestinterface.head.GetHotArticleListService
import com.example.imitatejuejin2.requestinterface.mine.GetLikesArticleListService
import com.example.imitatejuejin2.requestinterface.mine.GetMyArticleListService
import com.example.imitatejuejin2.requestinterface.head.GetNewArticleListService
import com.example.imitatejuejin2.data.response.GetArticleListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ArticleListBuilder {
    private var newList: MutableList<Article> = mutableListOf()
    private var hotList: MutableList<Article> = mutableListOf()
    private var myList: MutableList<Article> = mutableListOf()
    private var likeList: MutableList<Article> = mutableListOf()
    private var collectList: MutableList<Article> = mutableListOf()
    private var blankList: MutableList<Article> = mutableListOf()

    // 创建热门文章
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
                // 声明文章创建完毕
                Flag.setHasSetHotList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    // 最新文章
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
//                    Log.d("newList", newList.size.toString())
//                    Log.d("newListContent", newList.toString())
//                    Log.d("newListContent1", newList[0].toString())
//                    Log.d("newListContent11", newList[0].author.toString())
//                    Log.d("newListContent111", newList[0].author.username)

                }

                Flag.setHasSetNewList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    // 我的文章
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

                Flag.setHasSetMyList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    // 收藏的文章
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

                Flag.setHasSetCollectList(true)
            }

            override fun onFailure(call: Call<GetArticleListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    // 点赞的文章
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

                Flag.setHasSetLikeList(true)
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