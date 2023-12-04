package com.lihb.baseblock.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.lihb.baseblock.R

/**
 * 可以设置drawable大小的TextView
 */
class DrawableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var mLeftWidth = 0
    private var mLeftHeight = 0
    private var mTopWidth = 0
    private var mTopHeight = 0
    private var mRightWidth = 0
    private var mRightHeight = 0
    private var mBottomWidth = 0
    private var mBottomHeight = 0


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
        mLeftWidth = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableLeftWidth,
            mLeftWidth
        )
        mLeftHeight = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableLeftHeight,
            mLeftHeight
        )
        mTopWidth = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableTopWidth,
            mTopWidth
        )
        mTopHeight = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableTopHeight,
            mTopHeight
        )
        mRightWidth = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableRightWidth,
            mRightWidth
        )
        mRightHeight = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableRightHeight,
            mRightHeight
        )
        mBottomWidth = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableBottomWidth,
            mBottomWidth
        )
        mBottomHeight = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableBottomHeight,
            mBottomHeight
        )
        typedArray.recycle()
        setDrawablesSize()
    }

    private fun setDrawablesSize() {
        val compoundDrawables =
            compoundDrawables
//        LogUtil.d("Drawable size${compoundDrawables.size}value = $compoundDrawables")
        for (i in compoundDrawables.indices) {
            when (i) {
                0 -> setDrawableBounds(compoundDrawables[0], mLeftWidth, mLeftHeight)
                1 -> setDrawableBounds(compoundDrawables[1], mTopWidth, mTopHeight)
                2 -> setDrawableBounds(compoundDrawables[2], mRightWidth, mRightHeight)
                3 -> setDrawableBounds(compoundDrawables[3], mBottomWidth, mBottomHeight)
            }
        }
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            compoundDrawables[2],
            compoundDrawables[3]
        )
    }

    private fun setDrawableBounds(
        drawable: Drawable?,
        width: Int,
        height: Int
    ) {
        if (width <= 0 && height <= 0) return
        if (drawable != null) {
            val scale =
                drawable.intrinsicHeight.toDouble() / drawable.intrinsicWidth.toDouble()
//            LogUtil.d("width/height:${drawable.intrinsicWidth},${drawable.intrinsicHeight}")
//            LogUtil.d("scale = $scale")
            drawable.setBounds(0, 0, width, height)
            val bounds = drawable.bounds
            //高宽只给一个值时，自适应
            if (bounds.right == 0 || bounds.bottom == 0) {
//                LogUtil.d("before:${bounds.right},${bounds.bottom}")
                if (bounds.right == 0) {
                    bounds.right = (bounds.bottom / scale).toInt()
                    drawable.bounds = bounds
                }
                if (bounds.bottom == 0) {
                    bounds.bottom = (bounds.right * scale).toInt()
                    drawable.bounds = bounds
                }
//                LogUtil.d("after:${bounds.right},${bounds.bottom}")
            }
        }
    }
}