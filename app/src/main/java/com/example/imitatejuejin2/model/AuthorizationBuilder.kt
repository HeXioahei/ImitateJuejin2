package com.example.imitatejuejin2.model

object AuthorizationBuilder {

    private var Authorization: String = "test"
    private var AuthorizationRefresh: String = "test"

    /**
     * 获取 Authorization
     */
    fun getAuthorization(): String {
        return Authorization
    }

    /**
     * 获取用于刷新token的中介token
     */
    fun getAuthorizationRefresh(): String {
        return AuthorizationRefresh
    }

    /**
     * 初始化 Authorization
     */
    fun setAuthorization(Authorization2: String?) {
        if (Authorization2 != null) {
            Authorization = Authorization2
        }
    }

    /**
     * 初始化用于刷新token的中介token
     */
    fun setAuthorizationRefresh2(AuthorizationRefresh2: String?) {
        if (AuthorizationRefresh2 != null) {
            AuthorizationRefresh = AuthorizationRefresh2
        }
    }
}