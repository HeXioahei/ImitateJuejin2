package com.example.imitatejuejin2.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.data.GetCommentsData
import com.example.imitatejuejin2.databinding.ActivityArticleBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.MarkdownText
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.model.Time
import com.example.imitatejuejin2.requestinterface.article.CollectService
import com.example.imitatejuejin2.requestinterface.article.GetCommentsService
import com.example.imitatejuejin2.requestinterface.article.LikeService
import com.example.imitatejuejin2.requestinterface.article.WriteCommentService
import com.example.imitatejuejin2.requestinterface.mine.EditPasswordService
import com.example.imitatejuejin2.response.BaseResponse
import com.example.imitatejuejin2.response.GetCommentsResponse
import com.example.imitatejuejin2.ui.adapter.CommentListRecyclerView1
import okhttp3.FormBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    companion object {
        private lateinit var parentCommentList: MutableList<GetCommentsData>
        private lateinit var commentListRecyclerView1: CommentListRecyclerView1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 呈现视图内容
        val articleHeadImageUriString = intent.getStringExtra("headImage") as String
        Glide.with(this).load(articleHeadImageUriString.toUri()).into(binding.articleHeadImage)
        binding.articleUsername.text = intent.getStringExtra("username") as String
        binding.articleTitle.text = intent.getStringExtra("title") as String

        val content = intent.getStringExtra("content") as String
        MarkdownText.setMarkdownText(binding.articleContent, content, this)

        binding.articleLikes.text = intent.getStringExtra("likes").toString()
        binding.articleCollects.text = intent.getStringExtra("collects").toString()
        binding.articleComments.text = intent.getStringExtra("comments").toString()

        val articleId = intent.getStringExtra("id") as String
        val likeStatusValue = intent.getStringExtra("like_status").toString()
        val collectStatusValue = intent.getStringExtra("collect_status").toString()
        val Authorization = AuthorizationBuilder.getAuthorization()

        // 获取评论列表并呈现
        setComments(articleId)
        commentListRecyclerView1 = CommentListRecyclerView1(parentCommentList, this@ArticleActivity, articleId)
        binding.commentRV.adapter = commentListRecyclerView1
        val layoutManager = LinearLayoutManager(this@ArticleActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.commentRV.layoutManager = layoutManager

//        binding.articleTitle.text = "测试标题"
//        binding.articleContent.text = "测试内容"
//        binding.articleLikes.text = "1024"
//        binding.articleCollects.text = "1024"
//        binding.articleComments.text = "1024"

        // 返回上一页
        binding.articleReturn.setOnClickListener {
            finish()
        }

        // 点赞
        binding.articleLike.setOnClickListener {
//            val id = "1"
//            val Anthorization = ""
//            val statusValue = "0"
            val id = articleId
            val Anthorization = Authorization
            val statusValue = likeStatusValue
            val appService = ServiceCreator.create(LikeService::class.java)
            val status = FormBody.Builder().add("status", statusValue).build()
            appService.like(id, Anthorization, status).enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>,
                ) {
                    val back = response.body()
                    val code = back?.code
                    if (code == 200) {
                        // 图标变化 和 数字变化
                        if (statusValue == "0") {
                            // 点赞后的变化
                            binding.articleLike.setImageResource(R.drawable.img_liked)
                            val likes = binding.articleLikes.text.toString().toInt() + 1
                            binding.articleLikes.text = likes.toString()
                        } else {
                            // 取消点赞后的变化
                            binding.articleLike.setImageResource(R.drawable.img_unliked)
                            val likes = binding.articleLikes.text.toString().toInt() - 1
                            binding.articleLikes.text = likes.toString()
                        }
                        HasChanged.setArticlesItemHasChangedValue(true)
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

        // 收藏
        binding.articleCollect.setOnClickListener {
//            val id = "1"
//            val Anthorization = ""
//            val statusValue = "0"
            val id = articleId
            val Anthorization = Authorization
            val statusValue = collectStatusValue

            val status = FormBody.Builder().add("status", statusValue).build()
            ServiceCreator.create(CollectService::class.java)
                .collect(id, Anthorization, status)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>,
                    ) {
                        val back = response.body()
                        val code = back?.code
                        if (code == 200) {
                            // 图标变化 和 数字变化
                            if (statusValue == "0") {
                                binding.articleLike.setImageResource(R.drawable.img_collected)
                                val collects = binding.articleCollects.text.toString().toInt() + 1
                                binding.articleCollects.text = collects.toString()
                            } else {
                                binding.articleLike.setImageResource(R.drawable.img_uncollected)
                                val collects = binding.articleCollects.text.toString().toInt() - 1
                                binding.articleCollects.text = collects.toString()
                            }
                            HasChanged.setArticlesItemHasChangedValue(true)
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
            })
        }

        // 写一级评论
        binding.writeComment.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)

            val input = EditText(this)
            input.setBackgroundResource(R.drawable.bg_input)
            alertDialogBuilder.setView(input)

            alertDialogBuilder.setPositiveButton("发表") { dialog, _ ->
                val comment = input.text.toString()
                val time = Time.getNowTime()
                if (comment != "") {
                    ServiceCreator.create(WriteCommentService::class.java)
                        .writeCommentService(articleId, comment, time, "0", "1", Authorization)
                        .enqueue(object : Callback<BaseResponse> {
                            override fun onResponse(
                                call: Call<BaseResponse>,
                                response: Response<BaseResponse>,
                            ) {
                                if (response.body()?.code == 200) {
                                    Log.d("comment", "评论成功")
                                    // 重新获取评论列表
                                    updateParentComments(articleId)
                                    // 跳转到评论区
                                    toComments()
                                    dialog.dismiss()
                                } else {
                                    Log.d("comment", "评论失败")
                                }
                            }

                            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                }
            }

            alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }

            alertDialogBuilder.show()
        }

        // 写二级评论的功能在 adapter 中

        // 查看评论
        binding.articleComment.setOnClickListener {
            toComments()
        }
    }

    // 跳转到评论区
    fun toComments() {
        val locationOnScreen = IntArray(2)
        binding.commentRV.getLocationOnScreen(locationOnScreen)
        val x = locationOnScreen[0]
        val y = locationOnScreen[1]
        binding.nestedScrollView.smoothScrollTo(x, y)
    }

    // 获取评论列表
    fun setComments(articleId: String) {
        ServiceCreator.create(GetCommentsService::class.java)
            .getCommentService(articleId)
            .enqueue(object : Callback<GetCommentsResponse> {
                override fun onResponse(
                    call: Call<GetCommentsResponse>,
                    response: Response<GetCommentsResponse>,
                ) {
                    val back = response.body()
                    val code = back?.baseResponse?.code
                    if (code == 200) {
                        parentCommentList = back.data
                    }
                }

                override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    // 更新一级评论列表hjj1
    @SuppressLint("NotifyDataSetChanged")
    fun updateParentComments(articleId: String) {
        setComments(articleId)
        // 外部 notify （即一级评论列表notify）
        commentListRecyclerView1.notifyDataSetChanged()
    }
}