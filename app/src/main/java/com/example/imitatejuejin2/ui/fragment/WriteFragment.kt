package com.example.imitatejuejin2.ui.fragment

/**
 *      desc     ： 写文章页
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.imitatejuejin2.data.response.BaseResponse
import com.example.imitatejuejin2.databinding.FragmentWriteBinding
import com.example.imitatejuejin2.model.ArticleListBuilder
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.HasChanged
import com.example.imitatejuejin2.model.MarkdownTextBuilder
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.model.TimeBuilder
import com.example.imitatejuejin2.requestinterface.write.WriteArticleService
import com.example.imitatejuejin2.ui.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteFragment : Fragment() {

    private lateinit var binding: FragmentWriteBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentWriteBinding.inflate(inflater, container, false)

        val mainActivity: MainActivity
        if (activity != null) {
            mainActivity = activity as MainActivity
            val Anthorization = AuthorizationBuilder.getAuthorization()

            // markdown 编辑
            binding.writeContent.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 这里可以添加实时渲染的逻辑
                    MarkdownTextBuilder.setMarkdownText(
                        binding.showContent, s.toString(), mainActivity
                    )
                }

                override fun afterTextChanged(s: Editable?) { }
            })

            // 发表
            binding.publish.setOnClickListener {
                val title = binding.writeTitle.text.toString()
                val content = binding.writeContent.text.toString()
                val time = TimeBuilder.getNowTime()
                val appService = ServiceCreator.create(WriteArticleService::class.java)

                appService.writeArticleService(
                    title,
                    content,
                    time,
                    Anthorization
                ).enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>,
                    ) {
                        val back = response.body()
                        val code = back?.code
                        Log.d("code", "$code")
                        if (code == 200) {
                            Toast.makeText(mainActivity, "发表成功", Toast.LENGTH_SHORT).show()
                            // 重新获取列表
                            ArticleListBuilder.setAllArticleList(
                                AuthorizationBuilder.getAuthorization()
                            )
                            // 通知列表已改变
                            HasChanged.setArticlesItemHasChangedValue1(true)
                            HasChanged.setArticlesItemHasChangedValue2(true)

                        } else {
                            Toast.makeText(mainActivity, "发表失败", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }
        return binding.root
    }

}