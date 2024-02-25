package com.example.imitatejuejin2.data.basedata

class GetCommentsData (
    val head_image: String,
    val username: String,
    val comment: String,
    val time: String,
    val cid: String,
    val kid_comments: MutableList<KidComments>
)