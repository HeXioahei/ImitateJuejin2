package com.example.imitatejuejin2.data

import com.example.imitatejuejin2.model.Article
import com.example.imitatejuejin2.model.AuthorBrief
import com.example.imitatejuejin2.model.Time

class ArticleListData (
    val title: String,
    val content: String,
    val hits: Int,
    val likes: Int,
    val like_status: Int,
    val collects: Int,
    val collect_status: Int,
    val comments: Int,
    val author: AuthorBrief,
    val time: String,
    val id: Int
)