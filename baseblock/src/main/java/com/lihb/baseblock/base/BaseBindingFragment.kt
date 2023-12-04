package com.lihb.baseblock.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.inflateBindingWithGeneric

/**
 * fragment基类，所有 fragment 应该继承该类
 */
open class BaseBindingFragment<VB : ViewBinding> : Fragment(), View.OnClickListener {
    protected lateinit var mContext: Context private set
    protected var mBundle: Bundle? = null  //arguments 统一处理
        private set
    protected var rootView: View? = null
        private set

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBindingWithGeneric(layoutInflater, container, false)
        assignViews(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mBundle = arguments
    }

    override fun onClick(v: View?) {}

    /**
     * 所有的 view 赋值应该在此进行，使用 kotlinx 时可以忽略这个方法
     * onCreateView时会执行此方法
     * @param rootView 当前 fragment 的 rootview
     */
    protected open fun assignViews(rootView: View) {}

    /**
     * 子类初始化方法
     * onActivityCreated时执行
     */
    protected open fun onViewReady() {}

    override fun onResume() {
        super.onResume()
        //友盟统计
//        MobclickAgent.onPageStart(this::class.java.simpleName)
    }

    override fun onPause() {
        super.onPause()
        //友盟统计
//        MobclickAgent.onPageEnd(this::class.java.simpleName)
    }
}