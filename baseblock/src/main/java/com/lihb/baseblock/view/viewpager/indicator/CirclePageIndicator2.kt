/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lihb.baseblock.view.viewpager.indicator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.lihb.baseblock.R
import kotlin.math.abs

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 */
class CirclePageIndicator2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), PageIndicator2 {
    private val mPaintPageFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintStroke = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRadius: Float = 3f
    private var mViewPager: ViewPager2? = null
    private val mListener: OnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                mScrollState = state
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                mCurrentPage = position
                mPageOffset = positionOffset
                invalidate()
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (mSnap || mScrollState == ViewPager2.SCROLL_STATE_IDLE) {
                    mCurrentPage = position
                    mSnapPage = position
                    invalidate()
                }
            }
        }
    }
    private var mCurrentPage = 0
    private var mSnapPage = 0
    private var mPageOffset = 0f
    private var mScrollState = 0
    private var mOrientation: Int = LinearLayout.HORIZONTAL
    private var mCentered: Boolean = true
    private var mSnap: Boolean = true
    private var mTouchSlop: Int = 1
    private var mLastMotionX = -1f
    private var mActivePointerId = INVALID_POINTER
    private var mIsDragging = false
    var isCentered: Boolean
        get() = mCentered
        set(centered) {
            mCentered = centered
            invalidate()
        }
    var pageColor: Int
        get() = mPaintPageFill.color
        set(pageColor) {
            mPaintPageFill.color = pageColor
            invalidate()
        }
    var fillColor: Int
        get() = mPaintFill.color
        set(fillColor) {
            mPaintFill.color = fillColor
            invalidate()
        }
    var orientation: Int
        get() = mOrientation
        set(orientation) {
            when (orientation) {
                LinearLayout.HORIZONTAL, LinearLayout.VERTICAL -> {
                    mOrientation = orientation
                    requestLayout()
                }
                else -> throw IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.")
            }
        }
    var strokeColor: Int
        get() = mPaintStroke.color
        set(strokeColor) {
            mPaintStroke.color = strokeColor
            invalidate()
        }
    var strokeWidth: Float
        get() = mPaintStroke.strokeWidth
        set(strokeWidth) {
            mPaintStroke.strokeWidth = strokeWidth
            invalidate()
        }
    var radius: Float
        get() = mRadius
        set(radius) {
            mRadius = radius
            invalidate()
        }
    var isSnap: Boolean
        get() = mSnap
        set(snap) {
            mSnap = snap
            invalidate()
        }

    init {
        if (!isInEditMode) {
            //Load defaults from resources
//        final Resources res = getResources();
            val defaultPageColor = getThemeTextColorHint(context)
            val defaultFillColor = getThemeAccentColor(context)
            val defaultOrientation = LinearLayout.HORIZONTAL
            val defaultStrokeColor = getThemeTextColor(context)
            val defaultStrokeWidth = 0f
            val defaultRadius = 3f
            val defaultCentered = true
            val defaultSnap = false

            //Retrieve styles attributes
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyle, 0)
            mCentered =
                a.getBoolean(R.styleable.CirclePageIndicator_indicator_centered, defaultCentered)
            mOrientation =
                a.getInt(R.styleable.CirclePageIndicator_android_orientation, defaultOrientation)
            mPaintPageFill.style = Paint.Style.FILL
            mPaintPageFill.color =
                a.getColor(R.styleable.CirclePageIndicator_indicator_pageColor, defaultPageColor)
            mPaintStroke.style = Paint.Style.STROKE
            mPaintStroke.color =
                a.getColor(
                    R.styleable.CirclePageIndicator_indicator_strokeColor,
                    defaultStrokeColor
                )
            mPaintStroke.strokeWidth =
                a.getDimension(
                    R.styleable.CirclePageIndicator_indicator_strokeWidth,
                    defaultStrokeWidth
                )
            mPaintFill.style = Paint.Style.FILL
            mPaintFill.color =
                a.getColor(R.styleable.CirclePageIndicator_indicator_fillColor, defaultFillColor)
            mRadius =
                a.getDimension(R.styleable.CirclePageIndicator_indicator_radius, defaultRadius)
            mSnap = a.getBoolean(R.styleable.CirclePageIndicator_indicator_snap, defaultSnap)
            val background = a.getDrawable(R.styleable.CirclePageIndicator_android_background)
            if (background != null) {
                ViewCompat.setBackground(this, background)
            }
            a.recycle()
            val configuration = ViewConfiguration.get(context)
            mTouchSlop =
                configuration.scaledDoubleTapSlop //ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mViewPager == null || mViewPager?.adapter == null) {
            return
        }
        val count = mViewPager?.adapter!!.itemCount
        if (count == 0) {
            return
        }
        if (mCurrentPage >= count) {
            setCurrentItem(mViewPager?.currentItem ?: 0)
            return
        }
        val longSize: Int
        val longPaddingBefore: Int
        val longPaddingAfter: Int
        val shortPaddingBefore: Int
        if (mOrientation == LinearLayout.HORIZONTAL) {
            longSize = width
            longPaddingBefore = paddingLeft
            longPaddingAfter = paddingRight
            shortPaddingBefore = paddingTop
        } else {
            longSize = height
            longPaddingBefore = paddingTop
            longPaddingAfter = paddingBottom
            shortPaddingBefore = paddingLeft
        }
        val threeRadius = mRadius * 3
        val shortOffset = shortPaddingBefore + mRadius
        var longOffset = longPaddingBefore + mRadius
        if (mCentered) {
            longOffset += (longSize - longPaddingBefore - longPaddingAfter - count * 2 * mRadius - (count - 1) * mRadius) / 2.0f
        }
        var dX: Float
        var dY: Float
        var pageFillRadius = mRadius
        if (mPaintStroke.strokeWidth > 0) {
            pageFillRadius -= mPaintStroke.strokeWidth / 2.0f
        }

        //Draw stroked circles
        for (iLoop in 0 until count) {
            val drawLong = longOffset + iLoop * threeRadius
            if (mOrientation == LinearLayout.HORIZONTAL) {
                dX = drawLong
                dY = shortOffset
            } else {
                dX = shortOffset
                dY = drawLong
            }
            // Only paint fill if not completely transparent
            if (mPaintPageFill.alpha > 0) {
                canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill)
            }

            // Only paint stroke if a stroke width was non-zero
            if (pageFillRadius != mRadius) {
                canvas.drawCircle(dX, dY, mRadius, mPaintStroke)
            }
        }

        //Draw the filled circle according to the current scroll
        var cx = (if (mSnap) mSnapPage else mCurrentPage) * threeRadius
        if (!mSnap) {
            cx += mPageOffset * threeRadius
        }
        if (mOrientation == LinearLayout.HORIZONTAL) {
            dX = longOffset + cx
            dY = shortOffset
        } else {
            dX = shortOffset
            dY = longOffset + cx
        }
        canvas.drawCircle(dX, dY, mRadius, mPaintFill)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (super.onTouchEvent(ev)) {
            return true
        }
        if (mViewPager == null || mViewPager?.adapter == null || mViewPager?.adapter!!
                .itemCount == 0
        ) {
            return false
        }
        when (val action = ev.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0) //MotionEventCompat.getPointerId(ev, 0);
                mLastMotionX = ev.x
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex =
                    ev.findPointerIndex(mActivePointerId) //MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                val x =
                    ev.getX(activePointerIndex) //MotionEventCompat.getX(ev, activePointerIndex);
                val deltaX = x - mLastMotionX
                if (!mIsDragging) {
                    if (abs(deltaX) > mTouchSlop) {
                        mIsDragging = true
                    }
                }
                if (mIsDragging) {
                    mLastMotionX = x
                    if (mViewPager?.isFakeDragging == true || mViewPager?.beginFakeDrag() == true) {
                        mViewPager?.fakeDragBy(deltaX)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (!mIsDragging) {
                    val count = mViewPager?.adapter!!.itemCount
                    val width = width
                    val halfWidth = width / 2f
                    val sixthWidth = width / 6f
                    if (mCurrentPage > 0 && ev.x < halfWidth - sixthWidth) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager?.currentItem = mCurrentPage - 1
                        }
                        return true
                    } else if (mCurrentPage < count - 1 && ev.x > halfWidth + sixthWidth) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager?.currentItem = mCurrentPage + 1
                        }
                        return true
                    }
                }
                mIsDragging = false
                mActivePointerId = INVALID_POINTER
                if (mViewPager?.isFakeDragging == true) mViewPager?.endFakeDrag()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = ev.actionIndex //MotionEventCompat.getActionIndex(ev);
                mLastMotionX = ev.getX(index) //MotionEventCompat.getX(ev, index);
                mActivePointerId =
                    ev.getPointerId(index) //MotionEventCompat.getPointerId(ev, index);
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = ev.actionIndex // MotionEventCompat.getActionIndex(ev);
                val pointerId =
                    ev.getPointerId(pointerIndex) //MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mActivePointerId =
                        ev.getPointerId(newPointerIndex) //MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                mLastMotionX =
                    ev.getX(ev.findPointerIndex(mActivePointerId)) //MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
            }
        }
        return true
    }

    override fun setViewPager(view: ViewPager2) {
        if (mViewPager == view) {
            return
        }
        if (mViewPager != null) {
            mViewPager?.unregisterOnPageChangeCallback(mListener)
        }
        checkNotNull(view.adapter) { "ViewPager2 does not have adapter instance." }
        mViewPager = view
        mViewPager?.registerOnPageChangeCallback(mListener)
        invalidate()
    }

    override fun setViewPager(view: ViewPager2, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    override fun setCurrentItem(item: Int) {
        mViewPager?.currentItem = item
        mCurrentPage = item
        invalidate()
    }

    override fun notifyDataSetChanged() {
        mCurrentPage = mViewPager?.currentItem ?: 0
        mSnapPage = mViewPager?.currentItem ?: 0
        invalidate()
    }


    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onMeasure(int, int)
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mOrientation == LinearLayout.HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec))
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec))
        }
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private fun measureLong(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY || mViewPager == null || mViewPager?.adapter == null) {
            //We were told how big to be
            result = specSize
        } else {
            //Calculate the width according the views count
            val count = mViewPager?.adapter!!.itemCount
            result = (paddingLeft + paddingRight
                    + count * 2 * mRadius + (count - 1) * mRadius + 1).toInt()
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureShort(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize
        } else {
            //Measure the height
            result = (2 * mRadius + paddingTop + paddingBottom + 1).toInt()
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mCurrentPage = savedState.currentPage
        mSnapPage = savedState.currentPage
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.currentPage = mCurrentPage
        return savedState
    }

    internal class SavedState : BaseSavedState {
        var currentPage = 0

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            currentPage = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(currentPage)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(`in`: Parcel): SavedState {
                        return SavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    companion object {
        private const val INVALID_POINTER = -1
        private fun getThemeAccentColor(context: Context): Int {
            val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                android.R.attr.colorAccent
            } else {
                //Get colorAccent defined for AppCompat
                context.resources.getIdentifier("colorAccent", "attr", context.packageName)
            }
            val outValue = TypedValue()
            context.theme.resolveAttribute(colorAttr, outValue, true)
            return outValue.data
        }

        private fun getThemeTextColorHint(context: Context): Int {
            //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val colorAttr: Int = android.R.attr.textColorHint
            //        } else {
//            //Get colorAccent defined for AppCompat
//            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
//        }
            val outValue = TypedValue()
            context.theme.resolveAttribute(colorAttr, outValue, true)
            return outValue.data
        }

        private fun getThemeTextColor(context: Context): Int {
            //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val colorAttr: Int = android.R.attr.textColor
            //        } else {
//            //Get colorAccent defined for AppCompat
//            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
//        }
            val outValue = TypedValue()
            context.theme.resolveAttribute(colorAttr, outValue, true)
            return outValue.data
        }
    }
}