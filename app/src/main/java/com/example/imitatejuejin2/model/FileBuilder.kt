package com.example.imitatejuejin2.model

/**
 *      desc     ： 将图片由uri转化为file类型的单例类
 *      author   ： hexiaohei
 *      time     ： 2024/2/29
 */

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object FileBuilder {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun getImageFileFromUri(context: Context, imageUri: Uri): File {
        // 检查Android版本，对于Android 10及以上版本，使用新的MediaStore API
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getImageFileFromUriAboveQ(context, imageUri)
        } else {
            getImageFileFromUriUnderQ(context, imageUri)
        }
    }

    private fun getImageFileFromUriAboveQ(context: Context, imageUri: Uri): File {
        val inputStream: InputStream = context.contentResolver.openInputStream(imageUri)
            ?: return File("")

        // 创建一个临时文件来保存图片内容
        val tempFile = File(context.cacheDir, "temp_image.jpg")
        val outputStream: OutputStream = FileOutputStream(tempFile)

        // 将图片内容从输入流复制到输出流（临时文件）
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var bytesRead: Int
        try {
            bytesRead = inputStream.read(buffer)
            while (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            inputStream.close()
            outputStream.close()
            return File("")
        } finally {
            inputStream.close()
            outputStream.close()
        }

        return tempFile
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getImageFileFromUriUnderQ(context: Context, imageUri: Uri): File {

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(imageUri, projection, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val imagePath = cursor.getString(columnIndex)
                Log.d("pathname", imagePath.toString())
                return File(imagePath)
            }
            cursor.close()
        }

        return File("")
    }
}