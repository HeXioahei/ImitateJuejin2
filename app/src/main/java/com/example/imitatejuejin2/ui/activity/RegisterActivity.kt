package com.example.imitatejuejin2.ui.activity

/**
 *      desc     ： 注册页面
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.databinding.ActivityRegisterBinding
import com.example.imitatejuejin2.model.FileBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.begin.RegisterService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerUsername: String
    private lateinit var confirmPassword: String
    private lateinit var registerPassword: String
    private lateinit var headImageFile: File

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 直接返回到登录页面
        binding.registerReturn.setOnClickListener {
            finish()
        }

        // 注册头像
        // 注册一个照片选择器
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    Glide.with(this).load(uri).into(binding.registerHeadImage)
                    // 由 uri 转化为 file
                    headImageFile = FileBuilder.getImageFileFromUri(this, uri)
                    Log.d("file1", headImageFile.name.toString())
                    Log.d("file2", headImageFile.toString())
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        // 点击注册头像时打开照片选择器
        binding.registerHeadImage.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // 完成注册
        binding.registerBtn.setOnClickListener {
            registerUsername = binding.registerUsername.text.toString()
            confirmPassword = binding.confirmPassword.text.toString()
            registerPassword = binding.registerPassword.text.toString()
            Log.d("registerUsername", registerUsername)
            Log.d("registerPassword", registerPassword)

            // 将 file 封装为 Body
            val requestBody = headImageFile.asRequestBody("head_image".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part
                .createFormData("head_image", headImageFile.name, requestBody)

            // 确认两次密码是否一致
            if (confirmPassword == registerPassword) {

                ServiceCreator.create(RegisterService::class.java)
                    .register(registerUsername, registerPassword, multipartBody)
                    .enqueue(object : Callback<BaseResponse> {
                        override fun onResponse(
                            call: Call<BaseResponse>,
                            response: Response<BaseResponse>,
                        ) {
                            val back = response.body()
                            val code = back?.code
                            Log.d("code", code.toString())
                            Log.d("msg", back?.msg.toString())
                            if (code == 200) {
                                Toast.makeText(this@RegisterActivity,
                                    "注册成功", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity,
                                    "注册失败", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })

            } else {
                Toast.makeText(this, "密码不一致，请重新确认密码", Toast.LENGTH_SHORT).show()
            }
        }
    }
}