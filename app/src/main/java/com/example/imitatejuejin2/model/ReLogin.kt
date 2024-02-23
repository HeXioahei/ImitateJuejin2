package com.example.imitatejuejin2.model

object ReLogin {
    private var isReLogin = false

    fun getIsReLogin(): Boolean {
        return isReLogin
    }

    fun setIsReLogin(value: Boolean) {
        isReLogin = value
    }
}