package com.example.imitatejuejin2.data

import com.example.imitatejuejin2.model.KidComments

class GetCommentsData (
    val head_image: String,
    val username: String,
    val comment: String,
    val time: String,
    val cid: String,
    val kid_comments: MutableList<KidComments>
)