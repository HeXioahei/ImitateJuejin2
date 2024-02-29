package com.example.imitatejuejin2.model

/**
 *      desc     ： 通知列表和头像更新的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

object HasChanged {
    private var articlesItemHasChangedValue1 = false
    private var articlesItemHasChangedValue2 = false
    private var commentsItemHasChangedValue = false
    private var headImageHasChangedValue = false

    fun getArticlesItemHasChangedValue1(): Boolean {
        return articlesItemHasChangedValue1
    }

    fun setArticlesItemHasChangedValue1(newArticlesItemHasChangedValue1: Boolean) {
        articlesItemHasChangedValue1 = newArticlesItemHasChangedValue1
    }

    fun getArticlesItemHasChangedValue2(): Boolean {
        return articlesItemHasChangedValue2
    }

    fun setArticlesItemHasChangedValue2(newArticlesItemHasChangedValue2: Boolean) {
        articlesItemHasChangedValue2 = newArticlesItemHasChangedValue2
    }

    fun getCommentsItemHasChangedValue(): Boolean {
        return commentsItemHasChangedValue
    }

    fun setCommentsItemHasChangedValue(commentsItemHasChangedValue2: Boolean) {
        commentsItemHasChangedValue = commentsItemHasChangedValue2
    }

    fun getHeadImageHasChangedValue(): Boolean {
        return headImageHasChangedValue
    }

    fun setHeadImageHasChangedValue(headImageHasChangedValue2: Boolean) {
        headImageHasChangedValue = headImageHasChangedValue2
    }

}