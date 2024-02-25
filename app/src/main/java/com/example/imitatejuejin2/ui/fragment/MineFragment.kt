package com.example.imitatejuejin2.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.imitatejuejin2.ui.adapter.ArticleTypeViewPager
import com.example.imitatejuejin2.ui.adapter.NavRecyclerView
import com.example.imitatejuejin2.databinding.FragmentMineBinding
import com.example.imitatejuejin2.data.basedata.Article
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.LittleNavBuilder
import com.example.imitatejuejin2.ui.activity.EditMyInfoActivity
import com.example.imitatejuejin2.ui.activity.MainActivity

/**
 * 个人页
 */
class MineFragment : Fragment() {

    private lateinit var binding: FragmentMineBinding
    private lateinit var navRecyclerView: NavRecyclerView
    private lateinit var articleTypeViewPager: ArticleTypeViewPager
    private var outerList: MutableList<MutableList<Article>> =
        mutableListOf(
            ArticleListBuilder.getMyList(),
            ArticleListBuilder.getLikeList(),
            ArticleListBuilder.getCollectList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList(),
            ArticleListBuilder.getBlankList()
        )

    // 定义一个内部单例类，来控制文章导航栏的当前页面位置
    companion object X {
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
        binding = FragmentMineBinding.inflate(inflater, container, false)

        // 获取 Activity
        val mainActivity: MainActivity
        if (activity != null) {
            mainActivity = activity as MainActivity

            val authorBrief = AuthorBriefBuilder.getAuthorBrief()

            // 设置用户名
            binding.myUsername.text = authorBrief.username
            // 设置头像
            val myHeadImageString = authorBrief.head_image
            val decodedBytes = Base64.decode(myHeadImageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            binding.myHeadImage.setImageBitmap(bitmap)
            // 设置文章导航栏
            navRecyclerView = NavRecyclerView(LittleNavBuilder.createMyInfoNav(), binding.myListContent, this)
            binding.myListGuide.adapter = navRecyclerView
            val layoutInflater = LinearLayoutManager(mainActivity)
            layoutInflater.orientation = LinearLayoutManager.HORIZONTAL
            binding.myListGuide.layoutManager = layoutInflater
            // 设置文章列表
            articleTypeViewPager = ArticleTypeViewPager(outerList, mainActivity, authorBrief.username)
            binding.myListContent.adapter = articleTypeViewPager

            binding.myListContent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @SuppressLint("NotifyDataSetChanged")
                override fun onPageSelected(position: Int) {
                    setCurrentPosition(position)
                    binding.myListGuide.scrollToPosition(position)
                    navRecyclerView.notifyDataSetChanged()
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

    /**
     * 刷新列表与个人信息
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        // 刷新列表
        val articlesItemHasChangedValue2 = HasChanged.getArticlesItemHasChangedValue2()
        if (articlesItemHasChangedValue2) {
            val newOuterList: MutableList<MutableList<Article>> =
                mutableListOf(
                    ArticleListBuilder.getMyList(),
                    ArticleListBuilder.getLikeList(),
                    ArticleListBuilder.getCollectList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList(),
                    ArticleListBuilder.getBlankList()
                )
            outerList.clear()
            outerList.addAll(newOuterList)

            // notify
            articleTypeViewPager.notifyDataSetChanged()
            HasChanged.setArticlesItemHasChangedValue2(false)
        }

        // 刷新个人信息
        val newAuthorBrief = AuthorBriefBuilder.getAuthorBrief()
        if (HasChanged.getHeadImageHasChangedValue()) {
            val headImageString = newAuthorBrief.head_image
            val decodedBytes = Base64.decode(headImageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            binding.myHeadImage.setImageBitmap(bitmap)
            HasChanged.setHeadImageHasChangedValue(false)
        }
    }
}