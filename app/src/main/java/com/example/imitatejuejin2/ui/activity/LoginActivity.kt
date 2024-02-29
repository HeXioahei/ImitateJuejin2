package com.example.imitatejuejin2.ui.activity

/**
 *      desc     ： 登录页面
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imitatejuejin2.data.response.LoginResponse
import com.example.imitatejuejin2.databinding.ActivityLoginBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.begin.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

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

            Log.d("loginUsername", loginUsername)
            Log.d("loginPassword", loginPassword)

            // 进行登录的网络请求
            ServiceCreator.create(LoginService::class.java)
                .login(loginUsername, loginPassword)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>,
                    ) {
                        val back = response.body()
                        val code = back?.code
                        // 获取 Authorization
                        val Authorization = back?.access_token
                        val AuthorizationRefresh = back?.refresh_token
                        Log.d("code", "$code")
                        Log.d("Authorization", Authorization.toString())

                        if (code == 200) {
                            val intent = Intent(
                                this@LoginActivity, LoadActivity::class.java)
                            // 初始化 Authorization
                            AuthorizationBuilder.setAuthorization(Authorization)
                            AuthorizationBuilder.setAuthorizationRefresh2(AuthorizationRefresh)
                            Log.d("Authorization", AuthorizationBuilder.getAuthorization())
                            // 跳转到加载页面
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@LoginActivity, "登录失败，请检查用户名和密码是否正确",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }
}