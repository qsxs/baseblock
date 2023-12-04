package com.lihb.baseblock.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.lihb.baseblock.base.BaseBindingFragment
import com.lihb.baseblock.databinding.FragmentWebViewBinding
import com.lihb.baseblock.util.FileProviderHelper
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.*
import java.io.File


/**
 * 网页
 * Created by lhb on 2017/06/27.
 */
class WebFragment : BaseBindingFragment<FragmentWebViewBinding>() {
    private var needLoadingOnInit = false
    private var onWebListener: OnWebListener? = null
    private var url: String? = null
    private var mUploadMessage: ValueCallback<Uri?>? = null //文件上传
    private var mUploadCallbackAboveL: ValueCallback<Array<Uri?>>? = null//文件上传 for android5.0
    private var mCameraFileUri: Uri? = null

    private val webview by lazy(LazyThreadSafetyMode.NONE) { binding.webview }
    private val pb by lazy(LazyThreadSafetyMode.NONE) { binding.pb }

    private val videoStatus: HashMap<String/*视频id*/, Boolean/*是否正在播放*/> = hashMapOf()//视频播放状态

    companion object {
        private const val TAG = "WebFragment"
        private const val REQUEST_PERMISSION = 220
        private const val REQUEST_CODE_ASK_PERMISSIONS = 0x003
        private const val FILE_CHOOSER_RESULT_CODE = 0

        @JvmOverloads
        fun newInstance(
            url: String?,
            loadingOnInit: Boolean = true,
            mListener: OnWebListener? = null
        ): WebFragment {
            val args = Bundle()
            args.putString("webUrl", url)
            args.putBoolean("loadingOnInit", loadingOnInit)
            val fragment = WebFragment()
            fragment.arguments = args
            fragment.onWebListener = mListener
            return fragment
        }
    }

    fun setWebListener(listener: OnWebListener?) {
        this.onWebListener = listener
    }

    /**
     * 载入新的url
     *
     * @param url 要载入的新url
     */
    fun loadNewUrl(url: String?, showLoading: Boolean = true) {
        webview.clearSslPreferences()
        webview.clearCache(true)
        webview.clearMatches()
        webview.clearHistory()
        webview.clearFormData()
        if (!url.isNullOrEmpty()) {
            webview.loadUrl(url)
            if (showLoading) {
                pb.visibility = View.VISIBLE
                onWebListener?.onShowLoading(webview)
            }
        }
    }

    var userAgent: String?
        get() = webview.settings?.userAgentString
        set(value) {
            webview.settings?.userAgentString = value
        }

    fun addJavascriptInterface(obj: Any, name: String?) {
        webview.addJavascriptInterface(obj, name)
    }

    fun removeJavascriptInterface(name: String) {
        webview.removeJavascriptInterface(name)
    }

    /**
     * 是否有视频正在播放
     */
    fun hasVideoPlaying(): Boolean {
        videoStatus.forEach { entry ->
            if (entry.value) {
                return true
            }
        }
        return false
    }

    fun loadData(html: String?) {
        webview.loadData(html, "text/html", "utf-8")
    }

    fun loadDataWithBaseURL(baseUrl: String?, data: String?) {
        webview.loadDataWithBaseURL(
            baseUrl,
            data,
            "text/html",
            "utf-8",
            null
        )
    }

    fun getUrl(): String? {
        return webview.url
    }

    fun getOriginalUrl(): String? {
        return webview.originalUrl
    }

    fun getWebTitle(): String? {
        return webview.title
    }

    fun getWebFavicon(): Bitmap? {
        return webview.favicon
    }

    override fun onViewReady() {
        super.onViewReady()
        initWebView()
        if (mBundle != null) {
            url = mBundle!!.getString("webUrl")
            needLoadingOnInit = mBundle!!.getBoolean("loadingOnInit")
            loadNewUrl(url, needLoadingOnInit)
        }
    }

//    fun capture(): Bitmap {
//        /*
//         * 截屏，截取webview可视区域
//         * bitmap 绘制用的bitmap
//         * drawCursor 是否画光标---保留暂未使用
//         * drawScrollbar是否截取滚动条---保留暂未使用.
//         * drawTitleBar是否截取标题栏---保留暂未使用
//         * drawWithBuffer是否使用buffer---保留暂未使用
//         * scaleX----x方向缩放比例
//         * scaleY----y方向缩放比例
//         * callback截图完成后的回调，如果设置为null将为同步调用，否则是异步调用
//         */
//        val bitmap = Bitmap.createBitmap(
//            DeviceHelper.getScreenWidth(),
//            DeviceHelper.getScreenHeight(),
//            Bitmap.Config.ARGB_8888
//        )
//        webview.x5WebViewExtension?.snapshotVisible(
//            Canvas(bitmap),
//            true,
//            true,
//            false,
//            false
//        )
//        return bitmap
//    }

    @JavascriptInterface
    fun onWebVideoPlay(videoId: String?) {
        videoStatus[videoId ?: "unKnowVideoId"] = true
    }

    @JavascriptInterface
    fun onWebVideoPause(videoId: String?) {
        videoStatus[videoId ?: "unKnowVideoId"] = false
    }

    @JavascriptInterface
    fun onWebVideoEnd(videoId: String?) {
        videoStatus.remove(videoId ?: "unKnowVideoId")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webview.addJavascriptInterface(this, "newera")
//webview.x5WebViewExtension?.video
        webview.settingsExtension?.setContentCacheEnable(true)//X5开启前进后退缓存
        //自适应
        webview.settings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webview.settings?.useWideViewPort = true
        webview.settings?.loadWithOverviewMode = true

        webview.settings?.displayZoomControls = false//不显示缩放按钮缩放
//        webview.settings?.supportZoom()
        webview.settings?.builtInZoomControls = false
        //自适应
        webview.settings?.useWideViewPort = true
        webview.settings?.loadWithOverviewMode = true
        //支持js
        webview.settings?.javaScriptEnabled = true
        //提高渲染的优先级
//        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持DOM
        webview.settings?.domStorageEnabled = true
        //不使用缓存
//        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //启用数据库
        webview.settings?.databaseEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.settings?.mixedContentMode =
                android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (Build.VERSION.SDK_INT >= 23) { //所需要申请的权限数组
            val permissionsArray = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            //还需申请的权限列表
            val permissionsList: MutableList<String> =
                ArrayList()
            //申请权限后的返回码
            for (permission in permissionsArray) {
                if (ContextCompat.checkSelfPermission(
                        mContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsList.add(permission)
                }
            }
            if (permissionsList.size > 0) {
                requestPermissions(
                    permissionsList.toTypedArray(),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
            }
        }
        webview.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(Intent.createChooser(intent, "下载"))
        }
        webview.setWebChromeClient(object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissionsCallback?
            ) {
                callback?.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }

            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)
                onWebListener?.onCloseWindow(window)
            }

            override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                return super.onConsoleMessage(p0)
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                //                Log.i(TAG, "onReceivedTitle: " + title + "," + view.getUrl());
                onWebListener?.onReceivedTitle(view, title)
            }

            override fun onProgressChanged(view: WebView, progress: Int) {
                onWebListener?.onProgressChanged(view, progress)
                if (progress >= 80) {
                    pb.visibility = View.GONE

                }
                if (progress >= 100) { //加载完成，取消加载框
                    view.setInitialScale(100) //强制缩放100%
                    onWebListener?.onDismissLoading(view)
//                    if (view.url.contains("wmapi")) {
//                        LogHelper.i("onConsoleMessage:${view.url}")
//                        view.evaluateJavascript(
////                        "console.log(\" vs size\")"
//                            StringBuilder("console.log(\" vs size\");")
//                                .append("console.log(\" vs size\");")
//                                .append("var vs=document.getElementsByTagName('video');")
//                                .append("console.log(\" vs size\"+vs.length);")
////                            .append("for (var i=0; i<vs.length; i++){")
//                                .append("var eleVideo = vs[i];")
////                            .append("eleVideo.addEventListener(\" play \",function(){console.log(\" 开始播放 \");window.android.onWebVideoPlay(eleVideo.id);});")
////                            .append("eleVideo.addEventListener(\" pause \",function(){console.log(\" 暂停播放 \");window.android.onWebVideoPlay(eleVideo.id);});")
////                            .append("eleVideo.addEventListener(\" ended \",function(){console.log(\" 播放结束 \";window.android.onWebVideoPlay(eleVideo.id);)});")
////                            .append("};")
//                                .toString()
//                        )
//                        {
//                            LogHelper.i("evaluateJavascript return:$it")
//                        }
//                    }
//        视频播放监听
//        var eleVideo=document.getElementsByTagName('video')[0];
//        eleVideo.addEventListener("play",function(){alert("开始播放");});
//        eleVideo.addEventListener("pause",function(){alert("暂停播放");});
//        eleVideo.addEventListener("ended",function(){alert("播放结束")});
                }
            }

//            /***************** android中使用WebView来打开本机的文件选择器  */ // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
//// For Android  4.1.1+
//            fun openFileChooser(
//                uploadMsg: ValueCallback<Uri?>?,
//                acceptType: String?, capture: String?
//            ) {
//                openFileChooser(uploadMsg)
//            }

            // For 3.0 +
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri?>?,
                acceptType: String?
            ) {
                if (mUploadMessage != null) {
                    mUploadMessage?.onReceiveValue(null)
                    mUploadMessage = null
                }
                mUploadMessage = uploadMsg
                chooseFile(acceptType)
            }

            // For Android < 3.0
            fun openFileChooser(uploadMsg: ValueCallback<Uri?>?) {
                openFileChooser(uploadMsg, "*/*")
            }

            // For Android 5.0+
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri?>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (mUploadCallbackAboveL != null) {
                    mUploadCallbackAboveL?.onReceiveValue(null)
                    mUploadCallbackAboveL = null
                }
                mUploadCallbackAboveL = filePathCallback
                chooseFile(fileChooserParams)
                return true
            }
            /************* end  */
        })
        webview.webViewClient = object : WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (onWebListener?.shouldOverrideUrlLoading(view, request) == true) return true
                val url = request.url
                if ("tel" == url.scheme) {
                    goCall(url)
                    return true
                } else if ("http" != url.scheme && "https" != url.scheme) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, url)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request.url.toString())
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean { //                Log.i("用户单击超连接", url);
//判断用户单击的是那个超连接
                if (onWebListener?.shouldOverrideUrlLoading(view, url) == true) return true
                if (url.startsWith("tel:")) {
                    val uri = Uri.parse(url)
                    goCall(uri)
                    //这个超连接,java已经处理了，webview不要处理了
                    return true
                } else if (!url.startsWith("http")) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Log.e(TAG, "onReceivedError: ${error.description}")
                //发生错误
//                if (mListener != null) {
//                    mListener.onToast(webview, "请安装微信最新版");
//                }
            }
        }
    }

    private fun goCall(url: Uri?) {
        if (url != null) {
            val intent = Intent(Intent.ACTION_DIAL, url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onPause() {
        webview.onPause()
        super.onPause()
    }

    override fun onResume() {
        webview.onResume()
        super.onResume()
    }

    /**
     * 判断webview能否后端
     *
     * @return boolean
     */
    fun webviewCanGoBack(): Boolean {
        return webview.canGoBack()
    }

    /**
     * webview后退处理
     */
    fun webviewGoBack() {
        if (webviewCanGoBack()) {
            webview.goBack()
        }
    }

//    private fun createWebview() {
//        //手动创建WebView，显示到容器中，这样就能保证WebView一定是在X5内核准备好后创建的
//        val webView = WebView(App.context())
//        val css = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        (findViewById(R.id.webviewBox) as LinearLayout).addView(webView, css)
//
//        //...其他代码
//    }

    private fun chooseFile(fileParams: WebChromeClient.FileChooserParams) {
        if (fileParams.acceptTypes.isNotEmpty()) {
            chooseFile(fileParams.acceptTypes.getOrNull(0))
            return
        }
        chooseFile("*/*")
    }

    private fun chooseFile(fileType: String? = "*/*") {
        startActivityForResult(createMultiIntent(fileType), FILE_CHOOSER_RESULT_CODE)
    }

    private fun createMultiIntent(fileType: String?): Intent { // Create and return a chooser with the default OPENABLE
// actions including the camera, camcorder and sound
// recorder where available.
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = fileType ?: "image/*"
        val chooser = createChooserIntent(createCameraIntent())
        chooser.putExtra(Intent.EXTRA_INTENT, i)
        return chooser
    }

    private fun createChooserIntent(vararg intents: Intent): Intent {
        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents)
        chooser.putExtra(Intent.EXTRA_TITLE, "选择文件")
        return chooser
    }

    private fun createCameraIntent(): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(mContext.cacheDir, "${System.currentTimeMillis()}.jpg")
        mCameraFileUri = FileProviderHelper.getUriForFile(mContext, file)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri)
        return cameraIntent
    }

    /**
     * 文件选择返回
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        intent: Intent?
    ) {
        /** attention to this below ,must add this */
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return
            var result: Uri? = null
            if (null != mUploadCallbackAboveL) { //For Android5.0+
                var results: Array<Uri?>? = null
                if (resultCode == Activity.RESULT_OK) {
                    if (intent == null) { //拍照
                        results = arrayOf(mCameraFileUri)
                    } else { //图片
                        val dataString = intent.dataString
                        val clipData = intent.clipData
                        if (clipData != null) {
                            results = arrayOfNulls(clipData.itemCount)
                            for (i in 0 until clipData.itemCount) {
                                val item = clipData.getItemAt(i)
                                results[i] = item.uri
                            }
                        } else if (dataString != null && results.isNullOrEmpty()) {
                            results = arrayOf(Uri.parse(dataString))
                        } else if (mCameraFileUri != null && results.isNullOrEmpty()) {
                            results = arrayOf(mCameraFileUri)
                        }
                    }
                }
                mUploadCallbackAboveL?.onReceiveValue(results)
                mUploadCallbackAboveL = null
                mCameraFileUri = null
            } else {
                if (resultCode == Activity.RESULT_OK) {
                    result = if (intent == null) { //拍照
                        mCameraFileUri
                    } else { //图片
                        intent.data
                    }
                }
                mUploadMessage?.onReceiveValue(result)
                mUploadMessage = null
                mCameraFileUri = null
            }
        }
    }

    interface OnWebListener {
        fun onReceivedTitle(webView: WebView?, title: String?)
        fun onProgressChanged(webView: WebView?, progress: Int)
        fun onShowLoading(webView: WebView?)
        fun onDismissLoading(webView: WebView?)
        fun onToast(webView: WebView?, msg: String?)
        fun onCloseWindow(window: WebView?)
        fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean

        fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean
    }
}