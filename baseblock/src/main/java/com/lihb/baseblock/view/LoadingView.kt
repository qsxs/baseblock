package com.lihb.baseblock.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.lihb.baseblock.R
import com.lihb.baseblock.databinding.ViewLoadingBinding
import com.lihb.baseblock.util.ActManager

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var mCancelable: Boolean = false
    private var mMessage: CharSequence? = null
        set(value) {
            binding.loadingViewMessage.text = value
            field = value
        }

    private var mOnClickListener: OnClickListener? = null
    private var mOnCancelListener: ((LoadingView) -> Unit)? = null
    private var mOnDismissListener: ((LoadingView) -> Unit)? = null


    private val binding = ViewLoadingBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.dim))
        isClickable = true
        super.setOnClickListener(this)
//        super.setOnClickListener {
//            if (mCancelable) {
//                dismiss()
//                mOnCancelListener?.invoke(this)
//            }
//        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

    fun setMessage(@StringRes messageId: Int): LoadingView {
        mMessage = context.getText(messageId)
        return this
    }

    fun setMessage(message: CharSequence?): LoadingView {
        mMessage = message
        return this
    }

    fun setCancelable(cancelable: Boolean): LoadingView {
        mCancelable = cancelable
        return this
    }

    fun setOnCancelListener(listener: ((LoadingView) -> Unit)): LoadingView {
        mOnCancelListener = listener
        return this
    }

    fun setOnDismissListener(listener: ((LoadingView) -> Unit)): LoadingView {
        mOnDismissListener = listener
        return this
    }

    override fun onClick(v: View?) {
        when (v) {
            this -> {
                mOnClickListener?.onClick(this)
                if (mCancelable) {
                    dismiss()
                    mOnCancelListener?.invoke(this)
                }
            }
        }
    }

    fun show(message: CharSequence? = mMessage): LoadingView {
        if (message != mMessage) {
            mMessage = message
        }
        ActManager.currentActivity()?.let { act ->
            val group = act.findViewById<ViewGroup>(android.R.id.content)
            group.addView(this)
            bringToFront()
        }
        return this
    }

    fun dismiss() {
        if (parent is ViewGroup) {
            (parent as ViewGroup).removeView(this)
        }
        mOnDismissListener?.invoke(this)
    }


}