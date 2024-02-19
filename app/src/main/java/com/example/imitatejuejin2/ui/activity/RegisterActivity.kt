package com.example.imitatejuejin2.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.databinding.ActivityRegisterBinding
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.begin.RegisterService
import com.example.imitatejuejin2.response.BaseResponse
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    companion object {
        private lateinit var registerUsername: String
        private lateinit var confirmPassword: String
        private lateinit var registerPassword: String
        private lateinit var headImagePath: String
        private lateinit var file: File
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 直接返回
        binding.registerReturn.setOnClickListener {
            finish()
        }

        // 注册头像
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    Glide.with(this).load(uri).into(binding.registerHeadImage)
                    headImagePath = uri.path.toString()
                    Log.d("path", headImagePath)
                    Log.d("path2", uri.encodedPath.toString())
                    Log.d("path3", uri.lastPathSegment.toString())
                    Log.d("path4", uri.pathSegments.toString())
                    file = File(uri.path.toString())
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.registerHeadImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // 完成注册
        binding.registerBtn.setOnClickListener {
            registerUsername = binding.registerUsername.text.toString()
            confirmPassword = binding.confirmPassword.text.toString()
            registerPassword = binding.registerPassword.text.toString()
            Log.d("registerUsername", registerUsername)
            Log.d("registerPassword", registerPassword)
            Log.d("headImagePath", headImagePath)

            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)

            // 确认两次密码是否一致
            if (confirmPassword == registerPassword) {
                val appService = ServiceCreator.create(RegisterService::class.java)

                appService.register(registerUsername, registerPassword, multipartBody)
                    .enqueue(object : Callback<BaseResponse> {
                        override fun onResponse(
                            call: Call<BaseResponse>,
                            response: Response<BaseResponse>,
                        ) {
                            val back = response.body()
                            val code = back?.code
                            if (code == 200) {
                                Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "注册失败", Toast.LENGTH_SHORT).show()
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