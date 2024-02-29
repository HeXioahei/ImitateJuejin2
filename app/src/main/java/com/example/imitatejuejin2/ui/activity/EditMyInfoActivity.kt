package com.example.imitatejuejin2.ui.activity

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.databinding.ActivityEditMyInfoBinding
import com.example.imitatejuejin2.databinding.EditPasswordBinding
import com.example.imitatejuejin2.model.AuthorBriefBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.ReLogin
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.requestinterface.mine.edit.EditHeadImageService
import com.example.imitatejuejin2.requestinterface.mine.edit.EditPasswordService
import com.example.imitatejuejin2.requestinterface.mine.edit.EditUsernameService
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.model.FileBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditMyInfoActivity : AppCompatActivity() {

    companion object {
        private var username: String = AuthorBriefBuilder.getAuthorBrief().username
        private val Authorization: String = AuthorizationBuilder.getAuthorization()
        private var headImageString: String = AuthorBriefBuilder.getAuthorBrief().head_image
    }

    private lateinit var binding: ActivityEditMyInfoBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("a2", "a2")
        binding = ActivityEditMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = AuthorBriefBuilder.getAuthorBrief().username
        headImageString = AuthorBriefBuilder.getAuthorBrief().head_image

//        username = intent.getStringExtra("username") as String
//        headImageString = intent.getStringExtra("headImage") as String
//        val decodedBytes = Base64.decode(headImageString, Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//        binding.editHeadImageView.setImageBitmap(bitmap)
        Glide.with(this).load(headImageString).into(binding.editHeadImageView)
        binding.editUsernameText.text = username

        // 返回
        binding.editMyInfoReturn.setOnClickListener {
            finish()
        }

        // 更改头像
        // 先注册一个照片选择器
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")

                    val headImageFile = FileBuilder.getImageFileFromUri(this, uri)
                    val requestBody = RequestBody.create(MediaType.parse("head_image"), headImageFile)
                    val multipartBody = MultipartBody.Part.createFormData("head_image", headImageFile.name, requestBody) // 这里的name（”head_image“）必须和接口文档里定义的参数名字一样

                    val appService = ServiceCreator.create(EditHeadImageService::class.java)
                    appService.editHeadImageService(Authorization, multipartBody)
                        .enqueue(object : Callback<BaseResponse> {
                            override fun onResponse(
                                call: Call<BaseResponse>,
                                response: Response<BaseResponse>,
                            ) {
                                val back = response.body()
                                val code = back?.code
                                Log.d("imgcode", code.toString())
                                if (code == 200) {
                                    Toast.makeText(
                                        this@EditMyInfoActivity,
                                        "更换成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Glide.with(this@EditMyInfoActivity).load(uri).into(binding.editHeadImageView)
//                                    AuthorBriefBuilder.setHeadImage(Authorization)
                                    AuthorBriefBuilder.setAuthorBrief(Authorization)
                                    HasChanged.setHeadImageHasChangedValue(true)
                                } else {
                                    Toast.makeText(
                                        this@EditMyInfoActivity,
                                        "更换失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                                t.printStackTrace()
                            }

                        })

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        // 再点击来启动照片选择器
        binding.editHeadImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        // 更改用户名
        binding.editUsername.setOnClickListener {
            showInputUsername(username, Authorization)
        }


        // 更改密码
        binding.editPassword.setOnClickListener {
            showInputPassword(Authorization)
        }
    }



    private fun showInputUsername(initText: String, Authorization: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("修改用户名")

        // 创建一个 EditText 视图
        val input = EditText(this)
        input.setText(initText)
        input.setBackgroundResource(R.drawable.bg_input)
        alertDialogBuilder.setView(input)

        // 设置对话框的按钮
        alertDialogBuilder.setPositiveButton("确定") { dialog, _ ->
            val inputText = input.text.toString()
            // 在这里处理输入框中的文本
            val appService = ServiceCreator.create(EditUsernameService::class.java)
            appService.editUsernameService(inputText, Authorization)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>,
                    ) {
                        val back = response.body()
                        val code = back?.code
                        Log.d("usernameCode", code.toString())
                        if (code == 200) {
                            Toast.makeText(
                                this@EditMyInfoActivity, "更改成功\n请重新登录", Toast.LENGTH_SHORT
                            ).show()
                            ReLogin.setIsReLogin(true)
                            finish()
                        } else {
                            Toast.makeText(
                                this@EditMyInfoActivity, "更改失败", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
            // 用户点击了取消按钮，这里可以不做处理或者执行相应的逻辑
            dialog.dismiss()
        }

        // 显示对话框
        alertDialogBuilder.show()
    }

    private fun showInputPassword(Authorization: String) {
        val binding: EditPasswordBinding = EditPasswordBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.root)

        alertDialogBuilder.setPositiveButton("确定") { dialog, _ ->
            Log.d("old_password", binding.input1.text.toString())
            Log.d("new_password", binding.input2.text.toString())
            val appService = ServiceCreator.create(EditPasswordService::class.java)
            appService.editPasswordService(binding.input1.text.toString(), binding.input2.text.toString(), Authorization)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>,
                    ) {
                        val back = response.body()
                        val code = back?.code
                        Log.d("passcode", code.toString())
                        Log.d("passmsg", back?.msg.toString())
                        if (code == 200) {
                            Toast.makeText(
                                this@EditMyInfoActivity, "更改成功\n请重新登录", Toast.LENGTH_SHORT
                            ).show()
                            ReLogin.setIsReLogin(true)
                            finish()
                        } else {
                            Toast.makeText(
                                this@EditMyInfoActivity, "更改失败，请检查原密码是否正确", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.show()
    }

//    /**
//     * 将图片由 uri 转换为 file
//     */
//    private fun getImageFileFromUri(context: Context, imageUri: Uri): File {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = context.contentResolver.query(imageUri, projection, null, null, null)
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                val imagePath = cursor.getString(columnIndex)
//                cursor.close()
//                return File(imagePath)
//            }
//            cursor.close()
//        }
//
//        return File("")
//    }
}