package com.example.imitatejuejin2.data.response

import com.example.imitatejuejin2.data.Article

class GetArticleListResponse (
    val code: Int,
    val msg: String,
    val data: MutableList<Article>
)