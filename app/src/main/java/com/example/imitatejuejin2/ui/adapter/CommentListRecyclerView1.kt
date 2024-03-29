package com.example.imitatejuejin2.ui.adapter

/**
 *      desc     ： 父评论列表的适配器
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.imitatejuejin2.data.basedata.GetCommentsData
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.databinding.ItemParentCommentsBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.CommentsListBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.model.TimeBuilder
import com.example.imitatejuejin2.requestinterface.article.WriteCommentService
import com.example.imitatejuejin2.ui.activity.ArticleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @param parentCommentList ：父评论列表
 * @param activity ：所在的activity
 * @param articleId ：所属文章在总文章列表中的id
 */
class CommentListRecyclerView1(
    val parentCommentList: MutableList<GetCommentsData>,
    val activity: ArticleActivity,
    val articleId: String
) : RecyclerView.Adapter<CommentListRecyclerView1.ViewHolder>() {

    inner class ViewHolder(binding: ItemParentCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        val headImage = binding.parentCommentHeadImage
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

        // 创建子评论
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
                val time = TimeBuilder.getNowTime()
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
                                CommentsListBuilder.createParentCommentsList(articleId)
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