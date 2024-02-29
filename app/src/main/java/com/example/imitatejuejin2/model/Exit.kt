package com.example.imitatejuejin2.model

/**
 *      desc     ： 通知是否登出的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

object Exit {
    private var exit: Boolean = false

    fun getExit(): Boolean{
        return exit
    }

    fun setExit(value: Boolean) {
        exit = value
    }
}