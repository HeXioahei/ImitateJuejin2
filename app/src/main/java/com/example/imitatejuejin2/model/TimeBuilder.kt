package com.example.imitatejuejin2.model

/**
 *      desc     ： 创建时间的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object TimeBuilder {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowTime(): String {
        val year = LocalDateTime.now().year
        val month = LocalDateTime.now().monthValue
        val day = LocalDateTime.now().dayOfMonth
        val hour = LocalDateTime.now().hour
        val minute = LocalDateTime.now().minute
        val second = LocalDateTime.now().second
        val time = "$year/$month/$day $hour:$minute:$second"
        return time
    }
}