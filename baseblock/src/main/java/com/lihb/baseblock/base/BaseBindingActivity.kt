package com.lihb.baseblock.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.inflateBindingWithGeneric

/**
 * activity基类，所有 activity 应该继承该类
 */
@SuppressLint("Registered")
open class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity(), View.OnClickListener {
    private val TAG = "BaseBindingActivity"
    var isResume = false
        private set
    protected val mContext: Context by lazy { this }
    protected var mBundle: Bundle? = null //intent.extras 统一处理
        private set
    protected val root: ViewGroup by lazy(LazyThreadSafetyMode.NONE) { findViewById(android.R.id.content) }
    lateinit var binding: VB private set

    override fun onCreate(savedInstanceState: Bundle?) {
//        LogHelper.sv("${this.javaClass.simpleName}onCreate")
        //所有edittext点击才弹输入法
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        doBeforeSuperCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        binding = inflateBindingWithGeneric(layoutInflater)
        setContentView(binding.root)
//        EventLiveData.getInstant().observe(this) { event ->
//            onEvent(event)
//        }
        mBundle = intent.extras
        val sb = StringBuilder()
        mBundle?.keySet()?.forEach {
            sb.append(it).append(":").append(mBundle?.get(it)).append("\n")
        }
//        LogHelper.d("${this.javaClass.simpleName} mBundle:\n $sb")
        if (autoStatusBarColor()) {
            setStatusBarColor(getThemeBackgroundColor(), true)
        }
        setNavigationColor(getThemeBackgroundColor(), true)


        assignViews()
        onViewReady()
    }

    private var isOnFiveTouching = false//防止多次move导致多次回调


    override fun onResume() {
        super.onResume()
        isResume = true
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }

    protected open fun doBeforeSuperCreate(savedInstanceState: Bundle?) {}

    /**
     * 子类初始化方法
     */
    protected open fun onViewReady() {}

    /**
     * 所有的 view 赋值应该在此进行，使用 kotlinx 时可以忽略这个方法
     */
    protected open fun assignViews() {}

    protected fun setStatusBarColor(@ColorInt color: Int, lightStatusBar: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
            //最近任务
//            ActManager.TaskDescription tDesc = new ActManager.TaskDescription(
//                    getString(R.string.app_name),
//                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round),
//                    color);
//            setTaskDescription(tDesc);
//底部三大金刚导航栏
//            getWindow().setNavigationBarColor(color);
//如果6.0或者以上为亮色主题或者状态栏为白色
            WindowCompat.getInsetsController(
                window,
                window.decorView
            )?.isAppearanceLightStatusBars = lightStatusBar
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val decor = window.decorView
//                var ui = decor.systemUiVisibility
//                ui = if (lightStatusBar) {
//                    ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                } else {
//                    ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//                }
//                decor.systemUiVisibility = ui
//            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    protected fun exitFullscreen() {
//        NewEraApplication.fullscreen = false
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView)
            ?.show(WindowInsetsCompat.Type.systemBars())
        window.decorView.updatePadding(bottom = 0)
//        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
//            Log.i(
//                TAG,
//                "setOnApplyWindowInsetsListener: ${insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom}"
//            )
//            v.updatePadding(
//                bottom = insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom
//                    ?: 0
//            )
//            return@setOnApplyWindowInsetsListener insets
//        }
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//        sendBroadcast(Intent(ADBroadcastIntent.SHOW_NAVIGATION_BAR))//广告机显示导航栏的方法
    }

    /**
     * 设置全屏
     * @param isShowStatusBar 是否显示状态栏
     * @param isShowNavigationBar 是否显示底部导航栏
     * @param isLayoutNavigation 是否把布局延伸到导航栏
     */
    protected fun setFullscreen(
        isShowStatusBar: Boolean = true,
        isShowNavigationBar: Boolean = true,
        isLayoutNavigation: Boolean = false
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (!isLayoutNavigation) {
            ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
                Log.i(
                    TAG,
                    "setOnApplyWindowInsetsListener:2 ${insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom}"
                )
                v.updatePadding(
                    bottom = insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom
                        ?: 0
                )

                return@setOnApplyWindowInsetsListener insets
            }
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(window.decorView, null)
            window.decorView.updatePadding(bottom = 0)
        }


//        NewEraApplication.fullscreen = true
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            this.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            if (isShowStatusBar) {
                show(WindowInsetsCompat.Type.statusBars())
            } else {
                hide(WindowInsetsCompat.Type.statusBars())
            }
            if (isShowNavigationBar) {
                show(WindowInsetsCompat.Type.navigationBars())
            } else {
                hide(WindowInsetsCompat.Type.navigationBars())
            }
        }
//        sendBroadcast(Intent(ADBroadcastIntent.HIDE_NAVIGATION_BAR))//广告机隐藏导航栏的方法
    }

    protected fun setNavigationColor(color: Int, lightBar: Boolean) {
        WindowCompat.getInsetsController(
            window,
            window.decorView
        )?.isAppearanceLightNavigationBars = lightBar
        window.navigationBarColor = color
    }

    /**
     * 是否自动上色状态栏，true 时，自动设置为白色状态栏
     */
    protected open fun autoStatusBarColor(): Boolean = true

    override fun onClick(v: View?) {}

    private fun getThemeBackgroundColor(): Int {
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val colorAttr: Int = android.R.attr.colorBackground
        //        } else {
//            //Get colorAccent defined for AppCompat
//            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
//        }
        val outValue = TypedValue()
        theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }
}