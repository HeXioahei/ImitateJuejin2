package com.example.imitatejuejin2.ui.adapter

/**
 *      desc     ： 子评论列表的适配器
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.imitatejuejin2.data.basedata.KidComments
import com.example.imitatejuejin2.databinding.ItemKidCommentsBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.ui.activity.ArticleActivity

/**
 * @param kidCommentList ：子评论列表
 * @param activity ：所在activity
 */
class CommentListRecyclerView2 (
    val kidCommentList: MutableList<KidComments>,
    val activity: ArticleActivity
) : RecyclerView.Adapter<CommentListRecyclerView2.ViewHolder>() {

    inner class ViewHolder(binding: ItemKidCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        val headImage = binding.kidCommentHeadImage
        val userName: TextView = binding.kidCommentUsername
        val time: TextView = binding.kidCommentTime
        val comment: TextView = binding.kidCommentContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKidCommentsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int = kidCommentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = kidCommentList[position]

        val glideUrl = GlideUrl(
            item.head_image,
            LazyHeaders.Builder()
                .addHeader("Authorization", AuthorizationBuilder.getAuthorization())
                .build()
        )
        Glide.with(activity).load(glideUrl).into(holder.headImage)

        holder.userName.text = item.username
        holder.time.text = item.time
        holder.comment.text = item.comment
    }
}