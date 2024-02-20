package com.example.imitatejuejin2.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.ui.adapter.ArticleTypeViewPager
import com.example.imitatejuejin2.ui.adapter.NavRecyclerView
import com.example.imitatejuejin2.databinding.FragmentHeadBinding
import com.example.imitatejuejin2.model.Article
import com.example.imitatejuejin2.model.ArticleList
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
    override fun onPause() {
        super.onPause()
        hasPaused = true
    }
    // 刷新列表
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val articlesItemHasChangedValue1 = HasChanged.getArticlesItemHasChangedValue1()
        if (articlesItemHasChangedValue1) {
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
            outerList.addAll(newOuterList)

            // 外部 notify
            articleTypeViewPager.notifyDataSetChanged()
            HasChanged.setArticlesItemHasChangedValue1(false)
        }
    }
}