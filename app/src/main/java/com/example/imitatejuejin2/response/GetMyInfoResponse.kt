package com.example.imitatejuejin2.response

import com.example.imitatejuejin2.data.MyInfoData
import com.example.imitatejuejin2.model.AuthorBrief

class GetMyInfoResponse (
    val code: Int,
    val msg: String,
    val data: AuthorBrief
)