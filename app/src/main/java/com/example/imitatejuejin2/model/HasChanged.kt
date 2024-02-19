package com.example.imitatejuejin2.model

object HasChanged {
    private var articlesItemHasChangedValue = false
    private var commentsItemHasChangedValue = false

    fun getArticlesItemHasChangedValue(): Boolean {
        return articlesItemHasChangedValue
    }

    fun setArticlesItemHasChangedValue(articlesItemHasChangedValue2: Boolean) {
        articlesItemHasChangedValue = articlesItemHasChangedValue2
    }

    fun getCommentsItemHasChangedValue(): Boolean {
        return commentsItemHasChangedValue
    }

    fun setCommentsItemHasChangedValue(commentsItemHasChangedValue2: Boolean) {
        commentsItemHasChangedValue = commentsItemHasChangedValue2
    }
}