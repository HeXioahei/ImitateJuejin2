package com.example.imitatejuejin2.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.imitatejuejin2.databinding.ActivityWriteArticleBinding
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.write.WriteArticleService
import com.example.imitatejuejin2.response.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class WriteArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteArticleBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 返回
        binding.writeArticleReturn.setOnClickListener {
            finish()
        }

        // 发表
        binding.publish.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val content = binding.writeContent.text.toString()
            val year = LocalDateTime.now().year
            val month = LocalDateTime.now().monthValue
            val day = LocalDateTime.now().dayOfMonth
            val hour = LocalDateTime.now().hour
            val minute = LocalDateTime.now().minute
            val second = LocalDateTime.now().second
            val time = "$year/$month/$day $hour:$minute:$second"
            val Anthorization = ""
            val appService = ServiceCreator.create(WriteArticleService::class.java)
            appService.writeArticleService(
                title, content,
                time,
                Anthorization
            ).enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>,
                ) {
                    val back = response.body()
                    val code = back?.code
                    if (code == 200) {
                        Toast.makeText(this@WriteArticleActivity, "发表成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@WriteArticleActivity, "发表失败", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}