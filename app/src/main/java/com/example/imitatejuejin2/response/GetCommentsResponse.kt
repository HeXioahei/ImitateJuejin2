package com.example.imitatejuejin2.response

import com.example.imitatejuejin2.data.GetCommentsData

class GetCommentsResponse (
    val baseResponse: BaseResponse,
    val data: MutableList<GetCommentsData>
)