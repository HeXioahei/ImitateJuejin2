package com.example.imitatejuejin2.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.databinding.ActivityMainBinding
import com.example.imitatejuejin2.model.AuthorBrief
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.FlagBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.end.ExitService
import com.example.imitatejuejin2.requestinterface.mine.EditUsernameService
import com.example.imitatejuejin2.requestinterface.mine.GetMyInfoService
import com.example.imitatejuejin2.response.BaseResponse
import com.example.imitatejuejin2.response.GetMyInfoResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val Authorization = AuthorizationBuilder.getAuthorization()

//    companion object X {
//        private var Authorization: String = "init"
//        private var authorBrief: AuthorBrief = AuthorBrief("init1", "init2")
//
////        fun setAuthorization(Authorization2: String) {
////            Authorization = Authorization2
////        }
////        fun setAuthorBrief(authorBrief2: AuthorBrief) {
////            authorBrief = authorBrief2
////        }
////
////        fun getAuthorization() : String {
////            return Authorization
////        }
////        fun getAuthorBrief() : AuthorBrief {
////            return authorBrief
////        }
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FlagBuilder.run {
            setHasSetNewList(false)
            setHasSetHotList(false)
            setHasSetMyList(false)
            setHasSetLikeList(false)
            setHasSetCollectList(false)
        }

//        Authorization = "test "
//        authorBrief = AuthorBrief("test1","test2")
//        Log.d("activity","activity")

        // 设置底部总导航栏
        val navigationView : BottomNavigationView = binding.navView
        //1、先拿 NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //2、再拿 NavController
        val navController : NavController = navHostFragment.navController
        navigationView.setupWithNavController(navController)

        navigationView.itemIconTintList = null
        navigationView.setOnItemReselectedListener {

        }

        // 获取 Authorization
//        val Authorization = AuthorizationBuilder.getAuthorization()
        // Authorization = intent.getStringExtra("Authorization") as String
//        Authorization = " "
//        setAuthorization(Authorization)
//        authorBrief = AuthorBrief("","")
//        setAuthorBrief(authorBrief)

//        // 获取用户信息
//        ServiceCreator.create(GetMyInfoService::class.java)
//            .getMyInfoService(Authorization)
//            .enqueue(object : Callback<GetMyInfoResponse> {
//            override fun onResponse(
//                call: Call<GetMyInfoResponse>,
//                response: Response<GetMyInfoResponse>,
//            ) {
//                val back = response.body()
//                val authorBrief = back?.data?.author as AuthorBrief
//                // authorBrief = AuthorBrief("","")
//                AuthorBriefBuilder.setAuthorBrief(authorBrief)
//            }
//
//            override fun onFailure(call: Call<GetMyInfoResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//
//        })

//        // 传递给 headFragment
//        // 先创建其实例对象
//        val headFragment = HeadFragment()
//        headFragment.arguments?.apply {
//            putString("Authorization", Authorization)
//        }
    }

//    fun getAuthorization() : String {
//        return Authorization
//    }
//
//    fun getAuthorBrief() : AuthorBrief {
//        return authorBrief
//    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("确定退出？")

        // 设置对话框的按钮
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
            finish()
        }
        alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
            // 用户点击了取消按钮，这里可以不做处理或者执行相应的逻辑
            dialog.dismiss()
        }

        // 显示对话框
        alertDialogBuilder.show()
    }
}