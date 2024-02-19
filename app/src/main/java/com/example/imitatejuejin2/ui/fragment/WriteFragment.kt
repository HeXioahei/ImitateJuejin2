package com.example.imitatejuejin2.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.imitatejuejin2.databinding.FragmentWriteBinding
import com.example.imitatejuejin2.model.AuthorizationBuilder
import com.example.imitatejuejin2.model.MarkdownText
import com.example.imitatejuejin2.model.ServiceCreator
import com.example.imitatejuejin2.model.Time
import com.example.imitatejuejin2.requestinterface.write.WriteArticleService
import com.example.imitatejuejin2.response.BaseResponse
import com.example.imitatejuejin2.ui.activity.MainActivity
import io.noties.markwon.Markwon
import org.commonmark.renderer.html.HtmlRenderer
import org.commonmark.renderer.html.HtmlRenderer.HtmlRendererExtension
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class WriteFragment : Fragment() {

    private lateinit var binding: FragmentWriteBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWriteBinding.inflate(layoutInflater)

//        val mainActivity: MainActivity
//        if (activity != null) {
//            mainActivity = activity as MainActivity
//            val Anthorization = AuthorizationBuilder.getAuthorization()
//
//            // markdown 编辑
//            val markwon = Markwon.create(mainActivity)
//            binding.writeContent.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    // 这里可以添加实时渲染的逻辑，但注意性能影响
//                    // 可以考虑使用延迟渲染或debounce机制来优化性能
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//                    // 渲染Markdown文本到TextView中显示
//                    s?.let { markwon.setMarkdown(binding.showContent, it.toString()) }
//                }
//            })
//
//            binding.publish.setOnClickListener {
//                val title = binding.writeTitle.text.toString()
//                val content = binding.writeContent.text.toString()
//                val time = Time.getNowTime()
//                val appService = ServiceCreator.create(WriteArticleService::class.java)
//                appService.writeArticleService(
//                    title, content,
//                    time,
//                    Anthorization
//                ).enqueue(object : Callback<BaseResponse> {
//                    override fun onResponse(
//                        call: Call<BaseResponse>,
//                        response: Response<BaseResponse>,
//                    ) {
//                        val back = response.body()
//                        val code = back?.code
//                        if (code == 200) {
//                            Toast.makeText(mainActivity, "发表成功", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(mainActivity, "发表失败", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//                })
//            }
//        }
    }

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
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 这里可以添加实时渲染的逻辑，但注意性能影响
                    // 可以考虑使用延迟渲染或debounce机制来优化性能
//                    val markwonString = markwon.toMarkdown(binding.writeContent.text.toString())
//                    binding.showContent.text = markwonString
                    MarkdownText.setMarkdownText(binding.showContent, s.toString(), mainActivity)
                }

                override fun afterTextChanged(s: Editable?) {
                    // 渲染Markdown文本到TextView中显示
//                    s?.let { markwon.setMarkdown(binding.showContent, it.toString()) }
                }
            })

            binding.publish.setOnClickListener {
                val title = binding.writeTitle.text.toString()
                val content = binding.writeContent.text.toString()
                val time = Time.getNowTime()
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