package com.example.imitatejuejin2.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imitatejuejin2.databinding.ActivityLoginBinding
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.Flag
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

        // Glide.with(this).load("http://47.115.212.55:5000//testheadimages").into(binding.appIcon)

        // 去注册
        binding.goRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 完成登录
        binding.loginButton.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            Log.d("loginUsername", loginUsername)
            Log.d("loginPassword", loginPassword)
            Toast.makeText(this, "正在登录...", Toast.LENGTH_SHORT).show()

            // 进行登录的网络请求，得到两个 token
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
                            val intent = Intent(this@LoginActivity, LoadActivity::class.java)
                            // 初始化 Authorization
                            AuthorizationBuilder.setAuthorization(Authorization)
                            AuthorizationBuilder.setAuthorizationRefresh2(AuthorizationRefresh)
                            Log.d("Authorization", AuthorizationBuilder.getAuthorization())
                            startActivity(intent)
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