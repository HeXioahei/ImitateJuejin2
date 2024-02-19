package com.example.imitatejuejin2.response

import com.example.imitatejuejin2.data.ArticleListData
import com.example.imitatejuejin2.model.Article

class GetArticleListResponse (
    val code: Int,
    val msg: String,
    val data: MutableList<Article>
)