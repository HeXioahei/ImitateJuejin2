package com.example.imitatejuejin2.data.response

import com.example.imitatejuejin2.data.basedata.AuthorBrief

class GetMyInfoResponse (
    val code: Int,
    val msg: String,
    val data: AuthorBrief
)