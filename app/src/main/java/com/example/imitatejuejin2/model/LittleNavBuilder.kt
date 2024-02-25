package com.example.imitatejuejin2.model

object LittleNavBuilder {
    fun createArticleTypeNav(): MutableList<String> {
        val item1 = "最新"
        val item2 = "热门"
        val item3 = "关注"
        val item4 = "推荐"
        val item5 = "敬请"
        val item6 = "期待"
        val item7 = "谢谢"
        return mutableListOf(item1, item2, item3, item4, item5, item6, item7)
    }

    fun createMyInfoNav(): MutableList<String> {
        val item1 = "我的"
        val item2 = "点赞"
        val item3 = "收藏"
        val item4 = "草稿"
        val item5 = "历史"
        val item6 = "敬请"
        val item7 = "期待"
        return mutableListOf(item1, item2, item3, item4, item5, item6, item7)
    }
}