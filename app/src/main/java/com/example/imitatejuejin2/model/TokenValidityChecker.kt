package com.example.imitatejuejin2.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.imitatejuejin2.requestinterface.mine.GetMyInfoService
import com.example.imitatejuejin2.data.response.GetMyInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenValidityChecker(private val context: Context, private val token: String, private val onTokenExpired: () -> Unit) {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var isTokenValidFlag: Boolean = false

    // 开始定期检查Token的有效性
    fun startCheckingTokenValidity(intervalMillis: Long) {
        stopCheckingTokenValidity() // 停止之前的检查任务（如果有的话）

        runnable = Runnable {
            // 发送请求到服务器检查Token是否有效
            if (!isTokenValid(token)) {
                onTokenExpired.invoke() // 如果Token无效，执行相应的操作（例如刷新Token或跳转到登录页面）
            }

            // 设置下一次检查的时间
            handler.postDelayed(runnable!!, intervalMillis)
        }

        // 第一次检查立即执行
        handler.post(runnable!!)
    }

    // 停止定期检查Token的有效性
    fun stopCheckingTokenValidity() {
        runnable?.let {
            handler.removeCallbacks(it)
            runnable = null
        }
    }

    // 发送请求到服务器检查Token是否有效（这里需要根据实际情况实现与服务器的通信）
    private fun isTokenValid(token: String): Boolean {
        // TODO: 实现与服务器通信来检查Token的有效性
        ServiceCreator.create(GetMyInfoService::class.java)
            .getMyInfoService(token)
            .enqueue(object : Callback<GetMyInfoResponse> {
                override fun onResponse(
                    call: Call<GetMyInfoResponse>,
                    response: Response<GetMyInfoResponse>,
                ) {
                    val back = response.body()
                    isTokenValidFlag = back?.code == 200
                }

                override fun onFailure(call: Call<GetMyInfoResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        // 返回true表示Token有效，返回false表示Token无效
        return isTokenValidFlag // 这里只是一个示例，需要替换为实际的检查逻辑
    }
}