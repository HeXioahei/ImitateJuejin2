package com.example.imitatejuejin2.model

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon

object MarkdownText {

    fun setMarkdownText(textView: TextView, text: String, context: Context) {
        val markwon = Markwon.create(context)
        markwon.setMarkdown(textView, text)
    }

}