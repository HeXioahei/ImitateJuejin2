package com.example.imitatejuejin2.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imitatejuejin2.databinding.ActivityLoginBinding
import com.example.imitatejuejin2.model.ArticleList
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.FlagBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.begin.LoginService
import com.example.imitatejuejin2.data.response.LoginResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 去注册
        binding.goRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 完成登录
        binding.loginButton.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            // 进行登录的网络请求，得到两个token
            ServiceCreator.create(LoginService::class.java)
                .login(loginUsername, loginPassword)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>,
                    ) {
                        val back = response.body()
                        val code = back?.code
                        val Authorization = back?.access_token
                        val AuthorizationRefresh = back?.refresh_token
                        Log.d("code", "$code")
                        Log.d("Authorization", Authorization.toString())
                        if (code == 200 && Authorization != null) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            // 初始化 Authorization
                            AuthorizationBuilder.setAuthorization(Authorization)
                            AuthorizationBuilder.setAuthorizationRefresh2(AuthorizationRefresh)
                            Log.d("Authorization2", AuthorizationBuilder.getAuthorization())

                            GlobalScope.launch {
                                try {
                                    // 同时执行多个网络请求
                                    ArticleList.createNewArticleList(Authorization)
                                    ArticleList.createHotArticleList(Authorization)
                                    ArticleList.createMyArticleList(Authorization)
                                    ArticleList.createLikesArticleList(Authorization)
                                    ArticleList.createCollectArticleList(Authorization)
                                    AuthorBriefBuilder.setAuthorBrief(Authorization)

                                    // 检查是否数据都初始化完毕
                                    while (true) {
                                        Log.d("FlagBuilder.getHasSetAuthorBrief()", FlagBuilder.getHasSetAuthorBrief().toString())
                                        Log.d("FlagBuilder.getHasSetNewList()", FlagBuilder.getHasSetNewList().toString())
                                        Log.d("FlagBuilder.getHasSetHotList()", FlagBuilder.getHasSetHotList().toString())
                                        Log.d("FlagBuilder.getHasSetMyList()", FlagBuilder.getHasSetMyList().toString())
                                        Log.d("FlagBuilder.getHasSetLikeList()", FlagBuilder.getHasSetLikeList().toString())
                                        Log.d("FlagBuilder.getHasSetCollectList()", FlagBuilder.getHasSetCollectList().toString())

                                        // 直到所有数据都初始化完毕，再进入首页
                                        if (
                                            FlagBuilder.getHasSetAuthorBrief()
                                            && FlagBuilder.getHasSetNewList()
                                            && FlagBuilder.getHasSetHotList()
                                            && FlagBuilder.getHasSetMyList()
                                            && FlagBuilder.getHasSetLikeList()
                                            && FlagBuilder.getHasSetCollectList()
                                        ) {
                                            Log.d("intent", "intent")
                                            // 跳转到首页
                                            startActivity(intent)
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "登录成功",
                                                Toast.LENGTH_SHORT
                                            ).show()
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
                        } else {
                            Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }
}