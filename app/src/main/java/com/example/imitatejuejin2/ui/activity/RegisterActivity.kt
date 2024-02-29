package com.example.imitatejuejin2.ui.activity

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.databinding.ActivityRegisterBinding
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.begin.RegisterService
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.model.FileBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

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

                    headImageFile = FileBuilder.getImageFileFromUri(this, uri)
                    Log.d("file1", headImageFile.name.toString())
                    Log.d("file2", headImageFile.toString())
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        // 点击注册头像时打开照片选择器
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

            // 将 file 封装为 Body
            val requestBody = RequestBody.create(MediaType.parse("head_image"), headImageFile)
            val multipartBody = MultipartBody.Part.createFormData("head_image", headImageFile.name, requestBody) // 这里的name（”head_image“）必须和接口文档里定义的参数名字一样

            // 确认两次密码是否一致
            if (confirmPassword == registerPassword) {

                Toast.makeText(this, "正在注册...", Toast.LENGTH_LONG).show()
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

//    /**
//     * 将图片由 uri 转换为 file
//     */
//
//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun getImageFileFromUri(context: Context, imageUri: Uri): File {
//        // 检查Android版本，对于Android 10及以上版本，使用新的MediaStore API
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            return getImageFileFromUriAboveQ(context, imageUri)
//        } else {
//            // 对于旧版本Android，可以使用之前的方法获取文件路径
//            // ...
//            return getImageFileFromUriUnderQ(context, imageUri)
//        }
//    }
//
//    private fun getImageFileFromUriAboveQ(context: Context, imageUri: Uri): File {
//        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
//        if (inputStream == null) {
//            return File("")
//        }
//
//        // 创建一个临时文件来保存图片内容
//        val tempFile = File(context.cacheDir, "temp_image.jpg")
//        val outputStream: OutputStream = FileOutputStream(tempFile)
//
//        // 将图片内容从输入流复制到输出流（临时文件）
//        val bufferSize = 1024
//        val buffer = ByteArray(bufferSize)
//        var bytesRead: Int
//        try {
//            bytesRead = inputStream.read(buffer)
//            while (bytesRead != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//                bytesRead = inputStream.read(buffer)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            inputStream.close()
//            outputStream.close()
//            return File("")
//        } finally {
//            inputStream.close()
//            outputStream.close()
//        }
//
//        return tempFile
//    }
//
//    @RequiresApi(Build.VERSION_CODES.Q)
//    private fun getImageFileFromUriUnderQ(context: Context, imageUri: Uri): File {
//        // val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = context.contentResolver.query(imageUri, projection, null, null, null)
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                //val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                val imagePath = cursor.getString(columnIndex)
//                Log.d("pathname", imagePath.toString())
//                return File(imagePath)
//            }
//            cursor.close()
//        }
//
//        return File("")
//    }

}