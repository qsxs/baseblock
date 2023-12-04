package com.lihb.baseblock.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.inflateBindingWithGeneric

abstract class BaseBindingDialog<VB : ViewBinding>(context: Context, themeResId: Int = 0) :
    Dialog(context, themeResId), View.OnClickListener {

    lateinit var binding: VB private set

    override fun onCreate(savedInstanceState: Bundle?) {
        doBeforeSuperCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        binding = inflateBindingWithGeneric(layoutInflater)
        setContentView(binding.root)
        assignViews()
        onViewReady()
    }

    protected open fun doBeforeSuperCreate(savedInstanceState: Bundle?) {}

    protected open fun assignViews() {
//        setTouchTimeout(TOUCH_TIMEOUT_SECOND)
    }

    /**
     * 子类初始化方法
     */
    protected open fun onViewReady() {}

    override fun onClick(v: View?) {

    }
}