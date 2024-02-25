package com.example.imitatejuejin2.data.response

import com.example.imitatejuejin2.data.basedata.GetCommentsData

class GetCommentsResponse (
    val baseResponse: BaseResponse,
    val data: MutableList<GetCommentsData>
)