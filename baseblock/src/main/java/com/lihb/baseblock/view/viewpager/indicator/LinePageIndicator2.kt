/*
 * Copyright (C) 2012 Jake Wharton
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
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.lihb.baseblock.R
import kotlin.math.abs

/**
 * Draws a line for each page. The current page line is colored differently
 * than the unselected page lines.
 */
class LinePageIndicator2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), PageIndicator2 {
    private val INVALID_POINTER = -1
    private val mPaintUnselected = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintSelected = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mViewPager: ViewPager2? = null
    private val mListener: ViewPager2.OnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mCurrentPage = position
                invalidate()
            }
        }
    }
    private var mCurrentPage = 0
    private var mCentered: Boolean = true
    private var mLineWidth: Float = dp2px(context, 12F).toFloat()
    private var mGapWidth: Float = dp2px(context, 4F).toFloat()
    private var mTouchSlop: Int = 0
    private var mLastMotionX = -1f
    private var mActivePointerId = INVALID_POINTER
    private var mIsDragging = false
    var isCentered: Boolean
        get() = mCentered
        set(centered) {
            mCentered = centered
            invalidate()
        }
    var unselectedColor: Int
        get() = mPaintUnselected.color
        set(unselectedColor) {
            mPaintUnselected.color = unselectedColor
            invalidate()
        }
    var selectedColor: Int
        get() = mPaintSelected.color
        set(selectedColor) {
            mPaintSelected.color = selectedColor
            invalidate()
        }
    var lineWidth: Float
        get() = mLineWidth
        set(lineWidth) {
            mLineWidth = lineWidth
            invalidate()
        }
    var strokeWidth: Float
        get() = mPaintSelected.strokeWidth
        set(value) {
            mPaintSelected.strokeWidth = value
            mPaintUnselected.strokeWidth = value
            invalidate()
        }
    var gapWidth: Float
        get() = mGapWidth
        set(gapWidth) {
            mGapWidth = gapWidth
            invalidate()
        }

    init {
        if (!isInEditMode) {
            //Load defaults from resources
//        final int defaultPageColor = getThemeTextColorHint(context);
//        final int defaultFillColor = getThemeAccentColor(context);
//        final int defaultOrientation = HORIZONTAL;
//        final int defaultStrokeColor = getThemeTextColor(context);
//        final float defaultStrokeWidth = 0;
//        final float defaultRadius = 3;
//        final boolean defaultCentered = true;
//        final boolean defaultSnap = false;
            val defaultSelectedColor =
                Color.parseColor("#FF33B5E5")// res.getColor(R.color.default_line_indicator_selected_color)
            val defaultUnselectedColor =
                Color.parseColor("#FFBBBBBB")// res.getColor(R.color.default_line_indicator_unselected_color)
            val defaultLineWidth = dp2px(
                context,
                12f
            ).toFloat()//res.getDimension(R.dimen.default_line_indicator_line_width)
            val defaultGapWidth = dp2px(
                context,
                4f
            ).toFloat()//res.getDimension(R.dimen.default_line_indicator_gap_width)
            val defaultStrokeWidth = dp2px(
                context,
                1f
            ).toFloat()//res.getDimension(R.dimen.default_line_indicator_stroke_width)
            val defaultCentered = true// res.getBoolean(R.bool.default_line_indicator_centered)

            //Retrieve styles attributes
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator2, defStyle, 0)
            mCentered =
                a.getBoolean(R.styleable.LinePageIndicator_indicator_centered, defaultCentered)
            mLineWidth =
                a.getDimension(R.styleable.LinePageIndicator_indicator_lineWidth, defaultLineWidth)
            mGapWidth =
                a.getDimension(R.styleable.LinePageIndicator_indicator_gapWidth, defaultGapWidth)
            strokeWidth = a.getDimension(
                R.styleable.LinePageIndicator_indicator_strokeWidth,
                defaultStrokeWidth
            )
            mPaintUnselected.color = a.getColor(
                R.styleable.LinePageIndicator_indicator_unselectedColor,
                defaultUnselectedColor
            )
            mPaintSelected.color = a.getColor(
                R.styleable.LinePageIndicator_indicator_selectedColor,
                defaultSelectedColor
            )
            val background = a.getDrawable(R.styleable.LinePageIndicator_android_background)
            if (background != null) {
                ViewCompat.setBackground(this, background)
            }
            a.recycle()
            val configuration = ViewConfiguration.get(context)
            //        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
            mTouchSlop = configuration.scaledPagingTouchSlop
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mViewPager == null || mViewPager!!.adapter == null) {
            return
        }
        val count = mViewPager!!.adapter!!.itemCount
        if (count == 0) {
            return
        }
        if (mCurrentPage >= count) {
            setCurrentItem(count - 1)
            return
        }
        val lineWidthAndGap = mLineWidth + mGapWidth
        val indicatorWidth = count * lineWidthAndGap - mGapWidth
        val paddingTop = paddingTop.toFloat()
        val paddingLeft = paddingLeft.toFloat()
        val paddingRight = paddingRight.toFloat()
        val verticalOffset = paddingTop + (height - paddingTop - paddingBottom) / 2.0f
        var horizontalOffset = paddingLeft
        if (mCentered) {
            horizontalOffset += (width - paddingLeft - paddingRight) / 2.0f - indicatorWidth / 2.0f
        }

        //Draw stroked circles
        for (i in 0 until count) {
            val dx1 = horizontalOffset + i * lineWidthAndGap
            val dx2 = dx1 + mLineWidth
            canvas.drawLine(
                dx1,
                verticalOffset,
                dx2,
                verticalOffset,
                if (i == mCurrentPage) mPaintSelected else mPaintUnselected
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (super.onTouchEvent(ev)) {
            return true
        }
        if (mViewPager == null || mViewPager!!.adapter == null || mViewPager!!.adapter!!
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
                    ev.findPointerIndex(mActivePointerId) // MotionEventCompat.findPointerIndex(ev, mActivePointerId);
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
                    if (mViewPager!!.isFakeDragging || mViewPager!!.beginFakeDrag()) {
                        mViewPager!!.fakeDragBy(deltaX)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (!mIsDragging) {
                    val count = mViewPager!!.adapter!!.itemCount
                    val width = width
                    val halfWidth = width / 2f
                    val sixthWidth = width / 6f
                    if (mCurrentPage > 0 && ev.x < halfWidth - sixthWidth) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager!!.currentItem = mCurrentPage - 1
                        }
                        return true
                    } else if (mCurrentPage < count - 1 && ev.x > halfWidth + sixthWidth) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager!!.currentItem = mCurrentPage + 1
                        }
                        return true
                    }
                }
                mIsDragging = false
                mActivePointerId = INVALID_POINTER
                if (mViewPager!!.isFakeDragging) mViewPager!!.endFakeDrag()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = ev.actionIndex
                mLastMotionX = ev.getX(index)
                //                MotionEventCompat.getX(ev, index);
                mActivePointerId =
                    ev.getPointerId(index) //MotionEventCompat.getPointerId(ev, index);
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = ev.actionIndex
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

    override fun setViewPager(viewPager: ViewPager2) {
        if (mViewPager == viewPager) {
            return
        }
        if (mViewPager != null) {
            //Clear us from the old pager.
            mViewPager!!.unregisterOnPageChangeCallback(mListener)
        }
        checkNotNull(viewPager.adapter) { "ViewPager does not have adapter instance." }
        mViewPager = viewPager
        mViewPager!!.registerOnPageChangeCallback(mListener)
        invalidate()
    }

    override fun setViewPager(view: ViewPager2, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    override fun setCurrentItem(item: Int) {
        checkNotNull(mViewPager) { "ViewPager has not been bound." }
        mViewPager!!.currentItem = item
        mCurrentPage = item
        invalidate()
    }

    override fun notifyDataSetChanged() {
        invalidate()
    }

//    override fun setOnPageChangeListener(listener: OnPageChangeCallback) {
//        mListener = listener
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private fun measureWidth(measureSpec: Int): Int {
        var result: Float
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY || mViewPager == null || mViewPager!!.adapter == null) {
            //We were told how big to be
            result = specSize.toFloat()
        } else {
            //Calculate the width according the views count
            val count = mViewPager!!.adapter!!.itemCount
            result = paddingLeft + paddingRight + count * mLineWidth + (count - 1) * mGapWidth
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize.toFloat())
            }
        }
        return Math.ceil(result.toDouble()).toInt()
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureHeight(measureSpec: Int): Int {
        var result: Float
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize.toFloat()
        } else {
            //Measure the height
            result = mPaintSelected.strokeWidth + paddingTop + paddingBottom
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize.toFloat())
            }
        }
        return Math.ceil(result.toDouble()).toInt()
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mCurrentPage = savedState.currentPage
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.currentPage = mCurrentPage
        return savedState
    }

    internal class SavedState : BaseSavedState {
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

        var currentPage = 0

        constructor(superState: Parcelable?) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            currentPage = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(currentPage)
        }

        override fun describeContents(): Int {
            return 0
        }

    } //

    private fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
    //    private static int getThemeAccentColor(Context context) {
    //        int colorAttr;
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //            colorAttr = android.R.attr.colorAccent;
    //        } else {
    //            //Get colorAccent defined for AppCompat
    //            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
    //        }
    //        TypedValue outValue = new TypedValue();
    //        context.getTheme().resolveAttribute(colorAttr, outValue, true);
    //        return outValue.data;
    //    }
    //
    //    private static int getThemeTextColorHint(Context context) {
    //        int colorAttr;
    ////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //        colorAttr = android.R.attr.textColorHint;
    ////        } else {
    ////            //Get colorAccent defined for AppCompat
    ////            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
    ////        }
    //        TypedValue outValue = new TypedValue();
    //        context.getTheme().resolveAttribute(colorAttr, outValue, true);
    //        return outValue.data;
    //    }
    //
    //    private static int getThemeTextColor(Context context) {
    //        int colorAttr;
    ////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //        colorAttr = android.R.attr.textColor;
    ////        } else {
    ////            //Get colorAccent defined for AppCompat
    ////            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
    ////        }
    //        TypedValue outValue = new TypedValue();
    //        context.getTheme().resolveAttribute(colorAttr, outValue, true);
    //        return outValue.data;
    //    }

}