package com.example.imitatejuejin2.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.ui.adapter.ArticleTypeViewPager
import com.example.imitatejuejin2.ui.adapter.NavRecyclerView
import com.example.imitatejuejin2.databinding.FragmentHeadBinding
import com.example.imitatejuejin2.model.Article
import com.example.imitatejuejin2.model.ArticleList
import com.example.imitatejuejin2.model.AuthorBrief
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.LittleNav
import com.example.imitatejuejin2.ui.activity.MainActivity

class HeadFragment : Fragment() {

    private var hasPaused = false
    private lateinit var binding: FragmentHeadBinding
    private lateinit var navRecyclerView: NavRecyclerView
    private lateinit var articleTypeViewPager: ArticleTypeViewPager
    private val Authorization = AuthorizationBuilder.getAuthorization()
    private val authorBrief = AuthorBriefBuilder.getAuthorBrief()
    private var outerList: MutableList<MutableList<Article>> =
        mutableListOf(
            ArticleList.getNewList(),
            ArticleList.getHotList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList()
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        Log.d("newListSize", outerList.size.toString())
        // Log.d("newList1111", outerList[1][1].author.username)


        binding = FragmentHeadBinding.inflate(inflater, container, false)

        Log.d("Authorization", Authorization)
        // 获取 Activity
        val mainActivity: MainActivity
        if (activity != null) {
            mainActivity = activity as MainActivity
            //val authorBrief: AuthorBrief = AuthorBriefBuilder.getAuthorBrief()
            Log.d("fragment","fragment")

            // 设置右上角的头像
            val myHeadImageUriString = authorBrief.head_image
            Log.d("myHeadImageUriString", myHeadImageUriString)
            val myHeadImageUri = myHeadImageUriString.toUri()
            Glide.with(this).load(myHeadImageUri).into(binding.headMyHeadImage)

            // 获取列表
//            val outerList: MutableList<MutableList<Article>> =
//                mutableListOf(
//                    ArticleList.createNewArticleList(Authorization),
//                    ArticleList.createHotArticleList(Authorization),
//                    ArticleList.createBlankList(),
//                    ArticleList.createBlankList(),
//                    ArticleList.createBlankList(),
//                    ArticleList.createBlankList(),
//                    ArticleList.createBlankList(),
//                )

            val articleTypeNav = LittleNav.createArticleTypeNav()
            navRecyclerView = NavRecyclerView(articleTypeNav, binding.listContent)
            binding.articlesGuide.adapter = navRecyclerView

            articleTypeViewPager = ArticleTypeViewPager(outerList, mainActivity, authorBrief.username)
            binding.listContent.adapter = articleTypeViewPager

            val layoutInflater = LinearLayoutManager(activity)
            layoutInflater.orientation = LinearLayoutManager.HORIZONTAL
            binding.articlesGuide.layoutManager = layoutInflater

            binding.listContent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.articlesGuide.scrollToPosition(position)
                }
            })
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = FragmentHeadBinding.inflate(layoutInflater)
//
//        // 获取 Activity
//        val mainActivity: MainActivity
//        if (activity != null) {
//            mainActivity = activity as MainActivity
//            val authorBrief: AuthorBrief = AuthorBriefBuilder.getAuthorBrief(Authorization)
//            Log.d("fragment","fragment")
//
//            // 设置右上角的头像
//            val myHeadImageUriString = authorBrief.head_image
//            val myHeadImageUri = myHeadImageUriString.toUri()
//            Glide.with(this).load(myHeadImageUri).into(binding.headMyHeadImage)
//
//            // 获取列表
////            val outerList: MutableList<MutableList<Article>> =
////                mutableListOf(
////                    ArticleList.createNewArticleList(Authorization),
////                    ArticleList.createHotArticleList(Authorization),
////                    ArticleList.createBlankList(),
////                    ArticleList.createBlankList(),
////                    ArticleList.createBlankList(),
////                    ArticleList.createBlankList(),
////                    ArticleList.createBlankList(),
////                )
//
//            val articleTypeNav = LittleNav.createArticleTypeNav()
//            navRecyclerView = NavRecyclerView(articleTypeNav, binding.listContent)
//            binding.articlesGuide.adapter = navRecyclerView
//
//            articleTypeViewPager = ArticleTypeViewPager(outerList, mainActivity, authorBrief.username)
//            binding.listContent.adapter = articleTypeViewPager
//
//            val layoutInflater = LinearLayoutManager(activity)
//            layoutInflater.orientation = LinearLayoutManager.HORIZONTAL
//            binding.articlesGuide.layoutManager = layoutInflater
//
//            binding.listContent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    binding.articlesGuide.scrollToPosition(position)
//                }
//            })
//        }
    }

    override fun onPause() {
        super.onPause()
        hasPaused = true
    }

    // 刷新列表
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val articlesItemHasChangedValue = HasChanged.getArticlesItemHasChangedValue()
        if (articlesItemHasChangedValue) {
            val newOuterList: MutableList<MutableList<Article>> =
                mutableListOf(
                    ArticleList.getNewList(),
                    ArticleList.getHotList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList()
                )
            outerList.clear()
            newOuterList.addAll(outerList)
            // 外部 notify
            articleTypeViewPager.notifyDataSetChanged()
        }
        val mainActivity: MainActivity
        if (activity != null) {
            val mainActivity = activity as MainActivity
            articleTypeViewPager =
                ArticleTypeViewPager(outerList, mainActivity, authorBrief.username)
            binding.listContent.adapter = articleTypeViewPager

            val layoutInflater = LinearLayoutManager(mainActivity)
            layoutInflater.orientation = LinearLayoutManager.HORIZONTAL
            binding.articlesGuide.layoutManager = layoutInflater
        }
    }
}