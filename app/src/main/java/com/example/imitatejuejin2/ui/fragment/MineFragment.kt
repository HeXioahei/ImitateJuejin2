package com.example.imitatejuejin2.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.ui.adapter.ArticleTypeViewPager
import com.example.imitatejuejin2.ui.adapter.NavRecyclerView
import com.example.imitatejuejin2.databinding.FragmentMineBinding
import com.example.imitatejuejin2.model.Article
import com.example.imitatejuejin2.model.ArticleList
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.LittleNav
import com.example.imitatejuejin2.ui.activity.EditMyInfoActivity
import com.example.imitatejuejin2.ui.activity.MainActivity

class MineFragment : Fragment() {

    private lateinit var binding: FragmentMineBinding
    private lateinit var navRecyclerView: NavRecyclerView
    private lateinit var articleTypeViewPager: ArticleTypeViewPager
    private val Authorization = AuthorizationBuilder.getAuthorization()
    private var outerList: MutableList<MutableList<Article>> =
        mutableListOf(
            ArticleList.getMyList(),
            ArticleList.getLikeList(),
            ArticleList.getCollectList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList(),
            ArticleList.getBlankList()
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMineBinding.inflate(inflater, container, false)
        // 获取 Activity
        val mainActivity: MainActivity
        if (activity != null) {
            mainActivity = activity as MainActivity
            // val Authorization = AuthorizationBuilder.getAuthorization()
            val authorBrief = AuthorBriefBuilder.getAuthorBrief()

            binding.myUsername.text = authorBrief.username

            // 设置头像
            val headImageUri = authorBrief.head_image.toUri()
            Glide.with(this).load(headImageUri).into(binding.myHeadImage)

            navRecyclerView = NavRecyclerView(LittleNav.createMyInfoNav(), binding.myListContent)
            binding.myListGuide.adapter = navRecyclerView
            articleTypeViewPager = ArticleTypeViewPager(outerList, mainActivity, authorBrief.username)
            binding.myListContent.adapter = articleTypeViewPager

            val layoutInflater = LinearLayoutManager(mainActivity)
            layoutInflater.orientation = LinearLayoutManager.HORIZONTAL
            binding.myListGuide.layoutManager = layoutInflater




            binding.myListContent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.myListGuide.scrollToPosition(position)
                }
            })

            // 跳转到编辑资料页面
            binding.updateInfo.setOnClickListener {
                val intent = Intent(mainActivity, EditMyInfoActivity::class.java)
                intent.putExtra("username", authorBrief.username)
                intent.putExtra("headImage", authorBrief.head_image)
                startActivity(intent)
            }
        }
        return binding.root
    }

    // 刷新列表
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val articlesItemHasChangedValue2 = HasChanged.getArticlesItemHasChangedValue2()
        if (articlesItemHasChangedValue2) {
            val newOuterList: MutableList<MutableList<Article>> =
                mutableListOf(
                    ArticleList.getMyList(),
                    ArticleList.getLikeList(),
                    ArticleList.getCollectList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList(),
                    ArticleList.getBlankList()
                )
            outerList.clear()
            outerList.addAll(newOuterList)

            // 外部 notify
            articleTypeViewPager.notifyDataSetChanged()
            HasChanged.setArticlesItemHasChangedValue2(false)
        }
        val newAuthorBrief = AuthorBriefBuilder.getAuthorBrief()
        if (HasChanged.getUsernameHasChangedValue()) {
            binding.myUsername.text = newAuthorBrief.username
            HasChanged.setUsernameHasChangedValue(false)
        }
        if (HasChanged.getHeadImageHasChangedValue()) {
            val headImageUri = newAuthorBrief.head_image.toUri()
            Glide.with(this).load(headImageUri).into(binding.myHeadImage)
            HasChanged.setHeadImageHasChangedValue(false)
        }
    }
}