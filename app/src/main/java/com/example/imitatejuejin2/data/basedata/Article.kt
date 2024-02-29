package com.example.imitatejuejin2.data.basedata

/**
 * @param hits ：点击量
 * @param likes ：点赞量
 * @param like_status ：是否被点赞
 * @param collects ：收藏量
 * @param collect_status ：是否被收藏
 * @param comments ：评论量
 * @param id ：文章在总文章列表中的id
 */
class Article (
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