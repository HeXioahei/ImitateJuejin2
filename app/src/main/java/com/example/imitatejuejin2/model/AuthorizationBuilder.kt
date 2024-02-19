package com.example.imitatejuejin2.model

import android.content.Intent
import com.example.imitatejuejin2.ui.activity.MainActivity

object AuthorizationBuilder {

    private var Authorization: String = "test"

    fun getAuthorization(): String {
        return Authorization
    }

    fun setAuthorization(Authorization2: String?) {
        if (Authorization2 != null) {
            Authorization = Authorization2
        }
    }
}