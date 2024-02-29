package com.example.imitatejuejin2.model

object Flag {

    private var hasSetAuthorization: Boolean = false
    private var hasSetUsername: Boolean = false
    private var hasSetHeadImage: Boolean = false
    private var hasSetAuthorBrief: Boolean = false
    private var hasSetNewList: Boolean = false
    private var hasSetHotList: Boolean = false
    private var hasSetMyList: Boolean = false
    private var hasSetLikeList: Boolean = false
    private var hasSetCollectList: Boolean = false
    private var hasSetCommentsList: Boolean = false

    fun setHasAuthorization(status: Boolean) {
        hasSetAuthorization = status
    }

    fun setHasSetUsername(status: Boolean) {
        hasSetUsername = status
    }

    fun setHasSetHeadImage(status: Boolean) {
        hasSetHeadImage = status
    }

    fun setHasSetAuthorBrief(status: Boolean) {
        hasSetAuthorBrief = status
    }

    fun setHasSetNewList(status: Boolean) {
        hasSetNewList = status
    }

    fun setHasSetHotList(status: Boolean) {
        hasSetHotList = status
    }

    fun setHasSetMyList(status: Boolean) {
        hasSetMyList = status
    }

    fun setHasSetLikeList(status: Boolean) {
        hasSetLikeList = status
    }

    fun setHasSetCollectList(status: Boolean) {
        hasSetCollectList = status
    }

    fun setHasSetCommentsList(status: Boolean) {
        hasSetCommentsList = status
    }

    fun getHasSetAuthorization(): Boolean {
        return hasSetAuthorization
    }

    fun getHasSetUsername(): Boolean {
        return hasSetUsername
    }

    fun getHasSetHeadImage(): Boolean {
        return hasSetHeadImage
    }

    fun getHasSetAuthorBrief(): Boolean {
        return hasSetAuthorBrief
    }

    fun getHasSetNewList(): Boolean {
        return hasSetNewList
    }

    fun getHasSetHotList(): Boolean {
        return hasSetHotList
    }

    fun getHasSetMyList(): Boolean {
        return hasSetMyList
    }

    fun getHasSetLikeList(): Boolean {
        return hasSetLikeList
    }

    fun getHasSetCollectList(): Boolean {
        return hasSetCollectList
    }

    fun getHasSetCommentsList(): Boolean {
        return hasSetCommentsList
    }
}