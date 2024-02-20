package com.example.imitatejuejin2.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.data.GetCommentsData
import com.example.imitatejuejin2.databinding.ItemParentCommentsBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.CommentsList
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.model.Time
import com.example.imitatejuejin2.requestinterface.article.WriteCommentService
import com.example.imitatejuejin2.requestinterface.mine.EditUsernameService
import com.example.imitatejuejin2.response.BaseResponse
import com.example.imitatejuejin2.ui.activity.ArticleActivity
import com.example.imitatejuejin2.ui.activity.EditMyInfoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentListRecyclerView1(
    val parentCommentList: MutableList<GetCommentsData>,
    val activity: ArticleActivity,
    val articleId: String
) : RecyclerView.Adapter<CommentListRecyclerView1.ViewHolder>() {

    inner class ViewHolder(binding: ItemParentCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        val headImage: ImageView = binding.parentCommentHeadImage
        val userName: TextView = binding.parentCommentUsername
        val time: TextView = binding.parentCommentTime
        val comment: TextView = binding.parentCommentContent
        val respondBtn: TextView = binding.parentCommentRespond
        val kidComments: RecyclerView = binding.kidCommentRV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParentCommentsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int = parentCommentList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = parentCommentList[position]
        // 设置头像
        val headImageUri = item.head_image.toUri()
        Glide.with(activity).load(headImageUri).into(holder.headImage)

        holder.userName.text = item.username
        holder.time.text = item.time
        holder.comment.text = item.comment
        val adapter = CommentListRecyclerView2(item.kid_comments, activity)
        holder.kidComments.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        holder.kidComments.layoutManager = layoutManager

        // 写二级评论
        holder.respondBtn.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(activity)
            alertDialogBuilder.setTitle("回复")

            // 创建一个 EditText 视图
            val input = EditText(activity)
            input.hint = "请输入你的评论"
            alertDialogBuilder.setView(input)

            // 设置对话框的按钮
            alertDialogBuilder.setPositiveButton("回复") { dialog, _ ->
                val comment = input.text.toString()
                val time = Time.getNowTime()
                val Authorization = AuthorizationBuilder.getAuthorization()
                // 在这里处理输入框中的文本
                ServiceCreator.create(WriteCommentService::class.java)
                    .writeCommentService(articleId, comment, time, item.cid, "2", Authorization)
                    .enqueue(object : Callback<BaseResponse> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<BaseResponse>,
                            response: Response<BaseResponse>,
                        ) {
                            if (response.body()?.code == 200) {
                                Log.d("comment2", "评论成功")
                                // 二级评论 notify
                                CommentsList.createParentCommentsList(articleId)
                                // HasChanged.setCommentsItemHasChangedValue(true)
                                Thread.sleep(1000L)
                                dialog.dismiss()
                                activity.recreate()
                            } else {
                                Log.d("comment2", "评论失败")
                            }
                        }

                        override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }
            alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
                // 用户点击了取消按钮，这里可以不做处理或者执行相应的逻辑
                dialog.dismiss()
            }
            // 显示对话框
            alertDialogBuilder.show()
        }
    }
}