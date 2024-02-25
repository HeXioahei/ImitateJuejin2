package com.example.imitatejuejin2.ui.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.databinding.ItemArticleBinding
import com.example.imitatejuejin2.data.basedata.Article
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.CommentsListBuilder
import com.example.imitatejuejin2.model.Flag
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.MarkdownTextBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.article.HitService
import com.example.imitatejuejin2.requestinterface.mine.DeleteArticleService
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.ui.activity.ArticleActivity
import com.example.imitatejuejin2.ui.activity.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleListRecyclerView(
    private val list: MutableList<Article>,
    val activity: MainActivity,
    val myName: String
) : RecyclerView.Adapter<ArticleListRecyclerView.ViewHolder>() {

    inner class ViewHolder(binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {

        val title: TextView = binding.itemTitle
        val headImage: ImageView = binding.itemHeadImage
        val username: TextView = binding.itemUserName
        val time: TextView = binding.itemTime
        val content: TextView = binding.itemContent
        var like: ImageView = binding.itemLike
        val likes: TextView = binding.itemLikes
        var collect: ImageView = binding.itemCollect
        val collects: TextView = binding.itemCollects
        val hits: TextView = binding.itemHits
        val itemArticle: View = binding.itemArticle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("recyclerView2", "recyclerView2")
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(activity), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount() = list.size

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Log.d("recyclerView2", "recyclerView2")
        holder.title.text = item.title

        val decodedBytes = Base64.decode(item.author.head_image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        holder.headImage.setImageBitmap(bitmap)

        holder.username.text = item.author.username
        holder.time.text = item.time
        val contentString = item.content
        MarkdownTextBuilder.setMarkdownText(holder.content, contentString, activity)
        holder.hits.text = item.hits.toString()
        /*处理点赞图标，通过 like_status 来判断要选取那个图标，用 if 判断*/
        if (item.like_status == 1) {
            holder.like.setImageResource(R.drawable.img_liked)
        }
        holder.likes.text = item.likes.toString()
        /*处理收藏图标，通过 collect_status 来判断要选取那个图标，用 if 判断*/
        if (item.collect_status == 1) {
            holder.collect.setImageResource(R.drawable.img_collected)
        }
        holder.collects.text = item.collects.toString()

        // 设置点击事件，进入具体文章内容页
        holder.itemArticle.setOnClickListener {
            val intent = Intent(activity, ArticleActivity::class.java).apply {
                putExtra("title", item.title)
                putExtra("content", item.content)
                putExtra("headImage", item.author.head_image)
                putExtra("username", item.author.username)
                putExtra("like_status", item.like_status.toString())
                putExtra("likes", item.likes.toString())
                putExtra("collect_status", item.collect_status.toString())
                putExtra("collects", item.collects.toString())
                putExtra("comments", item.comments.toString())
                putExtra("id", item.id.toString())
                putExtra("time", item.time)
            }

            // 点击量更新
            ServiceCreator.create(HitService::class.java)
                .hit(item.id.toString(), AuthorizationBuilder.getAuthorization())
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>,
                    ) {
                        val back = response.body()
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })

            // 创建其评论列表
            GlobalScope.launch {
                CommentsListBuilder.createParentCommentsList(item.id.toString())

                while (true) {
                    if (Flag.getHasSetCommentsList()) {
                        activity.startActivity(intent)
                        break
                    }
                    withContext(Dispatchers.IO) {
                        Thread.sleep(500L)
                    }
                }
            }
        }

        // 删除文章
        holder.itemArticle.setOnLongClickListener {
            val alertDialogBuilder = AlertDialog.Builder(activity)
            if (item.author.username == myName) {
                alertDialogBuilder.setMessage("确定要删除你的文章吗？")
                // 设置对话框的按钮
                alertDialogBuilder.setPositiveButton("确定") { dialog, _ ->
                    val Authorization = AuthorizationBuilder.getAuthorization()
                    ServiceCreator.create(DeleteArticleService::class.java)
                        .deleteService(item.id.toString(), Authorization)
                        .enqueue(object : Callback<BaseResponse> {
                            override fun onResponse(
                                call: Call<BaseResponse>,
                                response: Response<BaseResponse>,
                            ) {
                                if (response.body()?.code == 200) {
                                    Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show()
                                    ArticleListBuilder.setAllArticleList(Authorization)
                                    HasChanged.setArticlesItemHasChangedValue1(true)
                                    HasChanged.setArticlesItemHasChangedValue2(true)
                                } else {
                                    Toast.makeText(activity, "删除失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                    dialog.dismiss()
                }
                alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
                    // 用户点击了取消按钮，这里可以不做处理或者执行相应的逻辑
                    dialog.dismiss()
                }
            } else {
                alertDialogBuilder.setTitle("这不是你的文章，不可删除哦！")
            }
            // 显示对话框
            alertDialogBuilder.show()
            true
        }
    }
}