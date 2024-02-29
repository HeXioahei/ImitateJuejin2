package com.example.imitatejuejin2.ui.fragment

/**
 *      desc     ： 首页
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.imitatejuejin2.data.basedata.Article
import com.example.imitatejuejin2.databinding.FragmentHeadBinding
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.LittleNavBuilder
import com.example.imitatejuejin2.ui.activity.MainActivity
import com.example.imitatejuejin2.ui.adapter.ArticleTypeViewPager
import com.example.imitatejuejin2.ui.adapter.NavRecyclerView

class HeadFragment : Fragment() {

    private lateinit var binding: FragmentHeadBinding
    private lateinit var navRecyclerView: NavRecyclerView
    private lateinit var articleTypeViewPager: ArticleTypeViewPager
    private var outerList: MutableList<MutableList<Article>> =
        mutableListOf(
            ArticleListBuilder.getNewList(),
            ArticleListBuilder.getHotList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList()
        )

    // 定义一个内部单例类，来控制文章导航栏的当前页面位置，方便处理光标地变化
    companion object {
        private var currentPosition = 0
            fun getCurrentPosition(): Int {
                return currentPosition
            }

            fun setCurrentPosition(value: Int) {
                currentPosition = value
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        Log.d("onCreateView", "onCreateView")
        binding = FragmentHeadBinding.inflate(inflater, container, false)

        Log.d("newListSize", outerList.size.toString())
        Log.d("newList1111", outerList[1][1].author.username)

        // 获取 Activity
        val mainActivity: MainActivity
        if (activity != null) {
            mainActivity = activity as MainActivity
            Log.d("fragment","fragment")

            val authorBrief = AuthorBriefBuilder.getAuthorBrief()
            val Authorization = AuthorizationBuilder.getAuthorization()

            // 设置右上角的头像
            val myHeadImageString = authorBrief.head_image
           // 创建带有headers的GlideUrl
            val glideUrl = GlideUrl(
                myHeadImageString,
                LazyHeaders.Builder()
                    .addHeader("Authorization", Authorization)
                    .build()
            )
            Glide.with(this).load(glideUrl).into(binding.headMyHeadImage)

            // 设置文章类型的导航栏
            val articleTypeNav = LittleNavBuilder.createArticleTypeNav()
            navRecyclerView = NavRecyclerView(articleTypeNav, binding.listContent, this)
            binding.articlesGuide.adapter = navRecyclerView
            val layoutInflater = LinearLayoutManager(activity)
            layoutInflater.orientation = LinearLayoutManager.HORIZONTAL
            binding.articlesGuide.layoutManager = layoutInflater

            // 用 ViewPager2 展示文章列表
            articleTypeViewPager = ArticleTypeViewPager(outerList, mainActivity, authorBrief.username)
            binding.listContent.adapter = articleTypeViewPager

            // ViewPager2 的 item 变化，导航栏跟着变化，导航栏的光标也跟着变化
            binding.listContent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @SuppressLint("NotifyDataSetChanged")
                override fun onPageSelected(position: Int) {
                    setCurrentPosition(position)
                    binding.articlesGuide.scrollToPosition(position)
                    navRecyclerView.notifyDataSetChanged()
                }
            })
        }
        return binding.root
    }

    /**
     * 重写 onResume 方法，来实现刷新的列表的功能
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        // 用一个变量来表示列表是否已经发生改变
        val articlesItemHasChangedValue1 = HasChanged.getArticlesItemHasChangedValue1()
        // 若已经改变，就进行刷新
        if (articlesItemHasChangedValue1) {
            val newOuterList: MutableList<MutableList<Article>> =
                mutableListOf(
                    ArticleListBuilder.getNewList(),
                    ArticleListBuilder.getHotList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList()
                )
            outerList.clear()
            outerList.addAll(newOuterList)

            // notify
            articleTypeViewPager.notifyDataSetChanged()
            // 将表示列表是否已经发生改变的值设置会 false，表示未改变，方便下次更新
            HasChanged.setArticlesItemHasChangedValue1(false)
        }
    }
}