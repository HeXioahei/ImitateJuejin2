package com.example.imitatejuejin2.ui.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imitatejuejin2.databinding.ItemKidCommentsBinding
import com.example.imitatejuejin2.data.KidComments
import com.example.imitatejuejin2.ui.activity.ArticleActivity

class CommentListRecyclerView2 (
    val kidCommentList: MutableList<KidComments>,
    val activity: ArticleActivity
) : RecyclerView.Adapter<CommentListRecyclerView2.ViewHolder>() {

    inner class ViewHolder(binding: ItemKidCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val headImage: ImageView = binding.kidCommentHeadImage
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
        // 设置头像
        val decodedBytes = Base64.decode(item.head_image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        holder.headImage.setImageBitmap(bitmap)

        holder.userName.text = item.username
        holder.time.text = item.time
        holder.comment.text = item.comment
    }
}