package com.example.imitatejuejin2.ui.activity

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.data.basedata.GetCommentsData
import com.example.imitatejuejin2.databinding.ActivityArticleBinding
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.CommentsListBuilder
import com.example.imitatejuejin2.model.Flag
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.MarkdownTextBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.model.TimeBuilder
import com.example.imitatejuejin2.requestinterface.article.CollectService
import com.example.imitatejuejin2.requestinterface.article.LikeService
import com.example.imitatejuejin2.requestinterface.article.WriteCommentService
import com.example.imitatejuejin2.data.response.BaseResponse
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

        Flag.setHasSetCommentsList(false)

        // 呈现视图内容
        val articleHeadImageUriString = intent.getStringExtra("headImage") as String
        val decodedBytes = Base64.decode(articleHeadImageUriString, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        binding.articleHeadImage.setImageBitmap(bitmap)
        binding.articleUsername.text = intent.getStringExtra("username") as String
        binding.articleTitle.text = intent.getStringExtra("title") as String
        binding.articleTime.text = intent.getStringExtra("time") as String

        val content = intent.getStringExtra("content") as String
        MarkdownTextBuilder.setMarkdownText(binding.articleContent, content, this)

        binding.articleLikes.text = intent.getStringExtra("likes").toString()
        val collects = intent.getStringExtra("collects").toString().toInt() + 1
        binding.articleCollects.text = collects.toString()
        binding.articleComments.text = intent.getStringExtra("comments").toString()

        val articleId = intent.getStringExtra("id") as String


        var likeStatusValue = intent.getStringExtra("like_status").toString()
        if (likeStatusValue == "1") {
            binding.articleLike.setImageResource(R.drawable.img_liked)
        } else {
            binding.articleLike.setImageResource(R.drawable.img_unliked)
        }

        var collectStatusValue = intent.getStringExtra("collect_status").toString()
        if (collectStatusValue == "1") {
            binding.articleCollect.setImageResource(R.drawable.img_collected)
            val collects = binding.articleCollects.text.toString().toInt() + 1
            binding.articleCollects.text = collects.toString()
        } else {
            binding.articleCollect.setImageResource(R.drawable.img_uncollected)
            val collects = binding.articleCollects.text.toString().toInt() - 1
            binding.articleCollects.text = collects.toString()
        }

        val Authorization = AuthorizationBuilder.getAuthorization()

        // 获取评论列表并呈现
        parentCommentList = CommentsListBuilder.getParentCommentsList()
        commentListRecyclerView1 = CommentListRecyclerView1(parentCommentList, this@ArticleActivity, articleId)
        binding.commentRV.adapter = commentListRecyclerView1
        val layoutManager = LinearLayoutManager(this@ArticleActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.commentRV.layoutManager = layoutManager

        // 返回上一页
        binding.articleReturn.setOnClickListener {
            finish()
        }

        // 点赞
        binding.articleLike.setOnClickListener {
            val id = articleId
            val Anthorization = Authorization
            val aimStatusValue: String
            if (likeStatusValue == "0") {
                aimStatusValue = "1"
            } else {
                aimStatusValue = "0"
            }

            // val statusValue = likeStatusValue
            val appService = ServiceCreator.create(LikeService::class.java)
            val status = FormBody.Builder().add("status",aimStatusValue).build()
            appService.like(id, Anthorization, status).enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>,
                ) {
                    val back = response.body()
                    val code = back?.code
                    if (code == 200) {
                        // 图标变化 和 数字变化
                        if (aimStatusValue == "1") {
                            // 点赞后的变化
                            binding.articleLike.setImageResource(R.drawable.img_liked)
                            val likes = binding.articleLikes.text.toString().toInt() + 1
                            binding.articleLikes.text = likes.toString()
                            likeStatusValue = "1"
                        } else {
                            // 取消点赞后的变化
                            binding.articleLike.setImageResource(R.drawable.img_unliked)
                            val likes = binding.articleLikes.text.toString().toInt() - 1
                            binding.articleLikes.text = likes.toString()
                            likeStatusValue = "0"
                        }
                        ArticleListBuilder.setAllArticleList(Authorization)
                        HasChanged.setArticlesItemHasChangedValue1(true)
                        HasChanged.setArticlesItemHasChangedValue2(true)
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

        // 收藏
        binding.articleCollect.setOnClickListener {
            val id = articleId
            val Anthorization = Authorization
            val aimStatusValue: String
            if (collectStatusValue == "0") {
                aimStatusValue = "1"
            } else {
                aimStatusValue = "0"
            }
            // val statusValue = collectStatusValue

            val status = FormBody.Builder().add("status", aimStatusValue).build()
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
                            if (aimStatusValue == "1") {
                                binding.articleCollect.setImageResource(R.drawable.img_collected)
                                val collects = binding.articleCollects.text.toString().toInt() + 1
                                binding.articleCollects.text = collects.toString()
                                collectStatusValue = "1"
                            } else {
                                binding.articleCollect.setImageResource(R.drawable.img_uncollected)
                                val collects = binding.articleCollects.text.toString().toInt() - 1
                                binding.articleCollects.text = collects.toString()
                                collectStatusValue = "0"
                            }
                            ArticleListBuilder.setAllArticleList(Authorization)
                            HasChanged.setArticlesItemHasChangedValue1(true)
                            HasChanged.setArticlesItemHasChangedValue2(true)
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
                val time = TimeBuilder.getNowTime()
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
                                    // 重新获取并更新评论列表
                                    CommentsListBuilder.createParentCommentsList(articleId)
                                    // HasChanged.setCommentsItemHasChangedValue(true)
                                    Log.d("change", HasChanged.getCommentsItemHasChangedValue().toString())
                                    Thread.sleep(1000L)
                                    dialog.dismiss()
                                    // 跳转到评论区
                                    toComments()
                                    recreate()

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
//    fun setComments(articleId: String) {
//        ServiceCreator.create(GetCommentsService::class.java)
//            .getCommentService(articleId)
//            .enqueue(object : Callback<GetCommentsResponse> {
//                override fun onResponse(
//                    call: Call<GetCommentsResponse>,
//                    response: Response<GetCommentsResponse>,
//                ) {
//                    val back = response.body()
//                    val code = back?.baseResponse?.code
//                    if (code == 200) {
//                        parentCommentList = back.data
//                    }
//                }
//
//                override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })
//    }

    // 更新一级评论列表hjj1
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateParentComments(articleId: String) {
//        setComments(articleId)
//        // 外部 notify （即一级评论列表notify）
//        commentListRecyclerView1.notifyDataSetChanged()
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    override fun onResume() {
//        super.onResume()
//        Log.d("onResume", "onResume")
//        val commentsItemHasChangedValue = HasChanged.getCommentsItemHasChangedValue()
//        if (commentsItemHasChangedValue) {
//            Log.d("notify", "notify")
//            val newParentCommentList = CommentsList.getParentCommentsList()
//            parentCommentList.clear()
//            parentCommentList.addAll(newParentCommentList)
//            Log.d("newComment", "success")
//        }
//        commentListRecyclerView1.notifyDataSetChanged()
//        HasChanged.setCommentsItemHasChangedValue(false)
//    }
}