package com.example.imitatejuejin2.model

/**
 *      desc     ： 通知重新登录的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

object ReLogin {
    private var isReLogin = false

    fun getIsReLogin(): Boolean {
        return isReLogin
    }

    fun setIsReLogin(value: Boolean) {
        isReLogin = value
    }
}