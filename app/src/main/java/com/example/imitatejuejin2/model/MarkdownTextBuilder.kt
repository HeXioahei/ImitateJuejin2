package com.example.imitatejuejin2.model

/**
 *      desc     ： 展示markdown文本的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon

object MarkdownTextBuilder {

    fun setMarkdownText(textView: TextView, text: String, context: Context) {
        val markwon = Markwon.create(context)
        markwon.setMarkdown(textView, text)
    }

}