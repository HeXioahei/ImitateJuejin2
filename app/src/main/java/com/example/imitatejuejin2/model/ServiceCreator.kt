package com.example.imitatejuejin2.model

/**
 *      desc     ： 创建网络请求的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val BASE_URL = "http://47.115.212.55:5000/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}