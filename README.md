# 登录页面

* 点击**注册按钮**跳转到**注册页面**，点击**登录按钮**跳转到**加载页面**

# 注册页面

## 注册头像

* 头像的显示需为圆形，不用`ImageView`，用`de.hdodenhof.circleimageview.CircleImageView`，需要引入第三方依赖库`implementation("de.hdodenhof:circleimageview:3.1.0")`
  
* 使用**照片选择器**，[谷歌官网教程](https://developer.android.com/training/data-storage/shared/photopicker?hl=zh-cn)，照片选择器要在onCreate中视图刚创建好的时候就注册，不能放在某个点击事件里，然后可以在点击事件里打开照片选择器
  
* 先将头像图片转化为`file`类型（由从照片选择器获得的uri来转化），用一个类来封装 转化 的方法（此方法是网上找来的，目前并没有很懂其中的逻辑【揣手手】）
  
      package com.example.imitatejuejin2.model
      
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
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                  return getImageFileFromUriAboveQ(context, imageUri)
              } else {
                  // 对于旧版本Android，可以使用之前的方法获取文件路径
                  // ...
                  return getImageFileFromUriUnderQ(context, imageUri)
              }
          }
      
          private fun getImageFileFromUriAboveQ(context: Context, imageUri: Uri): File {
              val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
              if (inputStream == null) {
                  return File("")
              }
      
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
              // val projection = arrayOf(MediaStore.Images.Media.DATA)
              val projection = arrayOf(MediaStore.Images.Media.DATA)
              val cursor = context.contentResolver.query(imageUri, projection, null, null, null)
      
              if (cursor != null) {
                  if (cursor.moveToFirst()) {
                      //val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
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
  
* 再将`file`打包为`Body`类型上传到后端服务器
  
      // 将 file 封装为 Body
      val requestBody = RequestBody.create("head_image".toMediaTypeOrNull(), headImageFile)
      val multipartBody = MultipartBody.Part.createFormData("head_image", headImageFile.name, requestBody) // 这里的name（”head_image“）必须和接口文档里定义的参数名字一样
      
  

# 加载页面（LoadingActivity）

用来进行“获取个人信息”、”获取列表信息“等网络请求，并将获取的数据**存在专门的单例类**中。这样初始化数据成功后，进入主页面。为了确保在**数据初始化完毕**后才能打开主页面，我设置了一个`GlobalScope`来同时进行 **获取并初始化数据** 和 **while循环检查是否初始化完毕** 这两个操作。

# 主页面（MainActivity）

采用`BottomNavigationView`与`navigation`结合的方式来设置**导航栏**。

`com.google.android.material.bottomnavigation.BottomNavigationView`

`androidx.fragment.app.FragmentContainerView`

需引入**依赖**：

`implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")`

`implementation("androidx.navigation:navigation-ui-ktx:2.5.3")`

用到了`NavHostFragment`还有`menu`和 `fragment`等。

`menu`中定义导航栏中的`item`，`navigation`中将`menu`中的`item`和`fragment`关联起来。然后还要在java代码中注册并关联

[关于BottomNavigationView的使用姿势都在这里了_bottomnavigationview用法-CSDN博客](https://blog.csdn.net/BigBoySunshine/article/details/105774561)

[Android：安卓学习笔记之navigation的简单理解和使用_android navigation-CSDN博客](https://blog.csdn.net/JMW1407/article/details/125714708)

[Android Navigation 报错does not have a NavController set on xxxxx 解决方案-CSDN博客](https://blog.csdn.net/linminghuo/article/details/119000601)

## 首页（HeadFragment）

顶部一个`recyclerView`做的可滑动导航栏，下面是一个`viewPager2`，`viewPager2`中又是`recyclerView`，用来呈现文章列表。

这里用到了**嵌套列表结构**。

点击**文章item**可以跳转到**文章具体内容页**。为了确保在评论列表初始化完毕后才能打开内容页，我设置了一个`GlobalScope`来同时进行 **获取并初始化文章** 和 **while循环检查是否初始化完毕** 这两个操作。这与加载页面即将跳转到主页面的设置方式一样。

## 写文章页（WriteFragment）

上半部分为**编辑文本框**，引入了`markwon`来进行**markdown渲染**，并通过`addTextChangedListener`设置**监听事件**，使下半部分`TextView`可以**实时显示**渲染后的文本。

点击**发表按钮**后就会发表文章，但是页面**不会自动跳转**到首页。因为进度原因，来不及想好此处的 fragment 要怎么切换。这是一个不足之处。

## 个人主页（Myfragment）

与首页的布局类似，点击**编辑资料按钮**跳转到**编辑资料页**

# 编辑个人资料页（EditMyInfoActivity）

* 修改头像部分的代码和注册时的基本类似。
  
* 修改用户名和密码时会**跳出一个文本框**来进行编辑。点击确认后会**自动跳转到登录页面**要求进行**重新登录**。
  

# 文章具体内容页（ArticleActivity）

* 接收`MainActivity`通过`Intent`传过来的数据，然后呈现出来。其中，**评论列表**需要事先根据**文章id**进行网络请求来获取。
  
* **二级评论**的功能是用**嵌套的recyclerView**完成的
  
* 点击**评论图案**会**自动滚动到评论区**。点击**写评论框**会跳出一个**文本框**来进行输入，点击确认后也会**自动滚动到评论区**。而这个滚动功能是用`getLocationOnScreen`实现的
  

# 网络请求接口（requestinterface）

最开始看书学这部分内容的时候，书中都是一个类只有一个fun的例子，于是我把每个功能都独立写了接口类，但后来学的更深入些发现，其实完全没有这样的必要，有些fun是可以封装在同一个类里的，但是意识到这个时调用接口的代码基本已经定型，修改起来较繁琐，就没有改。下次我会按照更合理的方式来写接口。

# 模型（model）

里面基本是一些**单例类**，用来封装存放**频繁使用的数据**的（如 Authorization 和 我的用户信息）。

主要分为两类：

1. **数据类**：如：ArticleListBuilder，**对各种文章列表进行初始化**，并且支持 `get` 。
  
2. **标志类**：如：Flag、ReLogin，用来标志某些**数据的变化**，以方便进行**页面跳转和刷新**等操作，也都支持 `get` 和 `set`。
  

#

# 数据（data）

定义各种**数据类型**，主要用于接口**网络请求的返回数据**。

# 其他

本来和后端商量要弄个**双token机制**，来延长登录的时长。后端的代码写好了，但是我前端没搞明白具体如何实现，因此代码中MainAcitivity中有一段注释掉的与实现双token机制相关的代码，model里面也有一个没有的TokenMenager类， 登录接口的返回数据中也有两个token。

本来还想做个**删除自己文章**的功能，但是因为进度原因，后端的代码还没写好，但前端可以有此功能的体验，长按文章即会跳出一个文本框来让你确定删除，只是点击确认后无法删除成功，也会提示删除失败。

# 问题与缺陷

**点击量**的**刷新**还存在**延迟**的问题。

实现某些功能所用的一些**方法比较低级**，对高级方法的理解能力和应用能力还较差。

还不太理解**框架**的概念。

和我们合作的美术的学姐设计了很nice的页面，但是，我研究了好久，还是不太懂要如何更好地导出学姐设计的页面，后来就一个图案一个图案地导出，**布局的呈现与学姐设计的页面具有不小的差距**。**用户体验感较差**。

# 亮点

似乎无，呜呜呜

# 收获

虽然成果还是较为一般的，但是，寒假确实投入了很多的时间和精力。

我**对项目开发的认识**进一步加深，也收获了不少**新知识**。

而且合作考核还锻炼了与他人**合作的能力**。
