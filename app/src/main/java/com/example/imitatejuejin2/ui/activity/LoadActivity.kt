package com.example.imitatejuejin2.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.databinding.ActivityLoadBinding
import com.example.imitatejuejin2.databinding.ActivityLoginBinding
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.Flag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        val Authorization = AuthorizationBuilder.getAuthorization()

        GlobalScope.launch {
            try {
                // 同时执行多个网络请求
                ArticleListBuilder.createNewArticleList(Authorization)
                ArticleListBuilder.createHotArticleList(Authorization)
                ArticleListBuilder.createMyArticleList(Authorization)
                ArticleListBuilder.createLikesArticleList(Authorization)
                ArticleListBuilder.createCollectArticleList(Authorization)
//                AuthorBriefBuilder.setUsername(Authorization)
//                AuthorBriefBuilder.setHeadImage(Authorization)
                AuthorBriefBuilder.setAuthorBrief(Authorization)

                // 检查是否数据都初始化完毕
                while (true) {
//                    Log.d("FlagBuilder.getHasSetUsername()", Flag.getHasSetUsername().toString())
//                    Log.d("FlagBuilder.getHasSetHeadImage()", Flag.getHasSetHeadImage().toString())
                    Log.d("FlagBuilder.getHasSetAuthorBrief()", Flag.getHasSetAuthorBrief().toString())
                    Log.d("FlagBuilder.getHasSetNewList()", Flag.getHasSetNewList().toString())
                    Log.d("FlagBuilder.getHasSetHotList()", Flag.getHasSetHotList().toString())
                    Log.d("FlagBuilder.getHasSetMyList()", Flag.getHasSetMyList().toString())
                    Log.d("FlagBuilder.getHasSetLikeList()", Flag.getHasSetLikeList().toString())
                    Log.d("FlagBuilder.getHasSetCollectList()", Flag.getHasSetCollectList().toString())

                    // 直到所有数据都初始化完毕，再进入首页
                    if (
//                        Flag.getHasSetUsername()
//                        && Flag.getHasSetHeadImage()
                        Flag.getHasSetAuthorBrief()
                        && Flag.getHasSetNewList()
                        && Flag.getHasSetHotList()
                        && Flag.getHasSetMyList()
                        && Flag.getHasSetLikeList()
                        && Flag.getHasSetCollectList()
                    ) {
                        // 跳转到首页
                        Toast.makeText(
                            this@LoadActivity,
                            "登录成功",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@LoadActivity, MainActivity::class.java)
                        startActivity(intent)
                        break
                    }
                    // 设置每次检查的时间间隔
                    withContext(Dispatchers.IO) {
                        Thread.sleep(500L)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}