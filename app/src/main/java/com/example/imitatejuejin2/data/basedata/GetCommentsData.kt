package com.example.imitatejuejin2.data.basedata

/**
 * @param comment ：评论内容
 * @param cid ：该评论在父评论列表中的id
 * @param kid_comments ：子评论列表
 */
class GetCommentsData (
    val head_image: String,
    val username: String,
    val comment: String,
    val time: String,
    val cid: String,
    val kid_comments: MutableList<KidComments>
)