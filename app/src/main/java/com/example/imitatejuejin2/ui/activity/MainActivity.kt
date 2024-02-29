package com.example.imitatejuejin2.ui.activity

/**
 *      desc     ： app页面
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.databinding.ActivityMainBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.Flag
import com.example.imitatejuejin2.model.ReLogin
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.end.ExitService
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.model.Exit
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取 Authorization
        val Authorization = AuthorizationBuilder.getAuthorization()

        // 重新标记信息的初始化情况，将其标记为未初始化，是下次新的登录可以正确地初始化信息
        Flag.run {
            setHasSetAuthorBrief(false)
            setHasSetNewList(false)
            setHasSetHotList(false)
            setHasSetMyList(false)
            setHasSetLikeList(false)
            setHasSetCollectList(false)
        }

        // 设置底部总导航栏
        val navigationView: BottomNavigationView = binding.navView
        //1、先拿 NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //2、再拿 NavController
        val navController: NavController = navHostFragment.navController
        navigationView.setupWithNavController(navController)

        // 将 item 背景色调为透明
        navigationView.itemIconTintList = null

        // 创建一个TokenValidityChecker实例，并设置Token过期时的操作
//        val tokenValidityChecker = TokenValidityChecker(this, Authorization) {
//            ServiceCreator.create(RefreshTokenService::class.java)
//                .refreshTokenService(Authorization)
//                .enqueue(object : Callback<AccessToken> {
//                    override fun onResponse(
//                        call: Call<AccessToken>,
//                        response: Response<AccessToken>,
//                    ) {
//                        val back = response.body()
//                        if (back != null) {
//                            // 重新设置 Authorization 的值
//                            AuthorizationBuilder.setAuthorization(back.access_token)
//                        } else {
//                            // finish()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<AccessToken>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//                })
//        }
//
//        // 开始每5分钟检查一次Token的有效性
//        tokenValidityChecker.startCheckingTokenValidity(5 * 60 * 1000) // 5分钟 = 5 * 60 * 1000毫秒

    }

    /**
     * 重写点击 back 键的事件, 来实现退出功能
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        val Authorization = AuthorizationBuilder.getAuthorization()

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("确定退出？")

        alertDialogBuilder.setPositiveButton("确定") { _, _ ->
            ServiceCreator.create(ExitService::class.java)
                .exitService(Authorization)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>,
                    ) {}

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            Exit.setExit(true)
            finish()
        }

        alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.show()
    }

    /**
     * 处理登出操作
     */
    override fun onResume() {
        super.onResume()
        if (ReLogin.getIsReLogin()) {
            finish()
        }
    }
}