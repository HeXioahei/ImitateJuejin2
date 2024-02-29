package com.example.imitatejuejin2.ui.adapter

/**
 *      desc     ： 文章类型viewPager的适配器
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitatejuejin2.databinding.ViewpagerHeadBinding
import com.example.imitatejuejin2.data.basedata.Article
import com.example.imitatejuejin2.ui.activity.MainActivity

/**
 * @param list ：文章列表
 * @param activity ：该适配器所在的Activity
 * @param myName ： 我的用户名
 */
class ArticleTypeViewPager(
    val list: MutableList<MutableList<Article>>,
    val activity: MainActivity,
    val myName: String
) : RecyclerView.Adapter<ArticleTypeViewPager.ViewHolder>() {

    inner class ViewHolder(binding: ViewpagerHeadBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewPagerItem = binding.viewpagerHead
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewpagerHeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Log.d("recyclerView", "recyclerView")

        // 创建文章列表 recyclerView
        val adapter = ArticleListRecyclerView(item, activity, myName)
        holder.viewPagerItem.adapter = adapter
        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.VERTICAL
        holder.viewPagerItem.layoutManager = layoutManager2
    }
}