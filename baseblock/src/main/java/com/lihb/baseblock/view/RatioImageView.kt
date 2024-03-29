package com.lihb.baseblock.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.lihb.baseblock.R

/**
 * 设定宽高比的ImageView，并可以设置圆角大小，是否圆形边框等
 */
class RatioImageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(
    context!!, attrs, defStyleAttr
) {
    private var isCircle: Boolean = false // 是否显示为圆形，如果为圆形则设置的corner无效
    private var isCoverSrc: Boolean = false // border、inner_border是否覆盖图片
    private var borderWidth: Int = 0  // 边框宽度
    private var borderColor = Color.WHITE // 边框颜色
    private var innerBorderWidth: Int = 0  // 内层边框宽度
    private var innerBorderColor = Color.WHITE // 内层边框充色
    private var cornerRadius: Int = 0// 统一设置圆角半径，优先级高于单独设置每个角的半径
    private var cornerTopLeftRadius: Int = 0 // 左上角圆角半径
    private var cornerTopRightRadius: Int = 0 // 右上角圆角半径
    private var cornerBottomLeftRadius: Int = 0  // 左下角圆角半径
    private var cornerBottomRightRadius: Int = 0 // 右下角圆角半径
    private var maskColor: Int = Color.TRANSPARENT // 遮罩颜色
    private var xfermode: Xfermode? = null

    private var _width = 0
    private var _height = 0
    private var radius = 0f
    private val borderRadii: FloatArray
    private val srcRadii: FloatArray
    private var srcRectF // 图片占的矩形区域
            : RectF
    private val borderRectF // 边框的矩形区域
            : RectF
    private val paint: Paint
    private val path // 用来裁剪图片的ptah
            : Path
    private var srcPath // 图片区域大小的path
            : Path? = null
    private var ratio = -1.0f //控件 _width ／ height的值

    init {
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.RatioImageView, 0, 0)
        isCircle = ta.getBoolean(R.styleable.RatioImageView_is_circle, isCircle)
        isCoverSrc = ta.getBoolean(R.styleable.RatioImageView_is_cover_src, isCoverSrc)
        borderWidth = ta.getDimensionPixelSize(R.styleable.RatioImageView_border_width, borderWidth)
        borderColor = ta.getColor(R.styleable.RatioImageView_border_color, borderColor)
        innerBorderWidth = ta.getDimensionPixelSize(
            R.styleable.RatioImageView_inner_border_width,
            innerBorderWidth
        )
        innerBorderColor =
            ta.getColor(R.styleable.RatioImageView_inner_border_color, innerBorderColor)
        cornerRadius =
            ta.getDimensionPixelSize(R.styleable.RatioImageView_corner_radius, cornerRadius)
        cornerTopLeftRadius = ta.getDimensionPixelSize(
            R.styleable.RatioImageView_corner_top_left_radius,
            cornerTopLeftRadius
        )
        cornerTopRightRadius = ta.getDimensionPixelSize(
            R.styleable.RatioImageView_corner_top_right_radius,
            cornerTopRightRadius
        )
        cornerBottomLeftRadius = ta.getDimensionPixelSize(
            R.styleable.RatioImageView_corner_bottom_left_radius,
            cornerBottomLeftRadius
        )
        cornerBottomRightRadius = ta.getDimensionPixelSize(
            R.styleable.RatioImageView_corner_bottom_right_radius,
            cornerBottomRightRadius
        )
        maskColor = ta.getColor(R.styleable.RatioImageView_mask_color, maskColor)
        ratio = ta.getFloat(R.styleable.RatioImageView_ratio, ratio)
        ta.recycle()
        borderRadii = FloatArray(8)
        srcRadii = FloatArray(8)
        borderRectF = RectF()
        srcRectF = RectF()
        paint = Paint()
        path = Path()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        } else {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            srcPath = Path()
        }
        calculateRadii()
        clearInnerBorderWidth()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var widthMeasureSpec = widthSpec
        var heightMeasureSpec = heightSpec
        if (ratio > 0.0f) {
            setMeasuredDimension(
                getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec)
            )
            var width = measuredWidth
            var height = measuredHeight
            val realWidth: Int
            val realHeight: Int
            if (width == 0 && height != 0) { //高度固定
                if (maxHeight in 1 until _height) {
                    height = maxHeight
                }
                realHeight = height
                realWidth = (realHeight * ratio).toInt()
            } else { //其余情况都视为宽度固定
                if (maxWidth in 1 until width) {
                    width = maxWidth
                }
                realWidth = width
                realHeight = try {
                    (realWidth / ratio).toInt()
                } catch (e: Exception) {
                    realWidth
                }
            }
            if (realHeight < 1073741823 && realWidth < 1073741823) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(realWidth, MeasureSpec.EXACTLY)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(realHeight, MeasureSpec.EXACTLY)
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(1073741823, MeasureSpec.EXACTLY)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(1073741823, MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _width = w
        _height = h
        initBorderRectF()
        initSrcRectF()
    }

    override fun onDraw(canvas: Canvas) {
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(srcRectF, null)
        if (!isCoverSrc) {
            val sx = 1.0f * (_width - 2 * borderWidth - 2 * innerBorderWidth) / _width
            val sy = 1.0f * (_height - 2 * borderWidth - 2 * innerBorderWidth) / _height
            // 缩小画布，使图片内容不被borders覆盖
            canvas.scale(sx, sy, _width / 2.0f, _height / 2.0f)
        }
        super.onDraw(canvas)
        paint.reset()
        path.reset()
        if (isCircle) {
            path.addCircle(_width / 2.0f, _height / 2.0f, radius, Path.Direction.CCW)
        } else {
            path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW)
        }
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.xfermode = xfermode
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(path, paint)
        } else {
            srcPath!!.addRect(srcRectF, Path.Direction.CCW)
            // 计算tempPath和path的差集
            srcPath!!.op(path, Path.Op.DIFFERENCE)
            canvas.drawPath(srcPath!!, paint)
            srcPath!!.reset()
        }
        paint.xfermode = null

        // 绘制遮罩
        if (maskColor != 0) {
            paint.color = maskColor
            canvas.drawPath(path, paint)
        }
        // 恢复画布
        canvas.restore()
        // 绘制边框
        drawBorders(canvas)
    }

    private fun drawBorders(canvas: Canvas) {
        if (isCircle) {
            if (borderWidth > 0) {
                drawCircleBorder(canvas, borderWidth, borderColor, radius - borderWidth / 2.0f)
            }
            if (innerBorderWidth > 0) {
                drawCircleBorder(
                    canvas,
                    innerBorderWidth,
                    innerBorderColor,
                    radius - borderWidth - innerBorderWidth / 2.0f
                )
            }
        } else {
            if (borderWidth > 0) {
                drawRectFBorder(canvas, borderWidth, borderColor, borderRectF, borderRadii)
            }
        }
    }

    private fun drawCircleBorder(
        canvas: Canvas,
        borderWidth: Int,
        borderColor: Int,
        radius: Float
    ) {
        initBorderPaint(borderWidth, borderColor)
        path.addCircle(_width / 2.0f, _height / 2.0f, radius, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }

    private fun drawRectFBorder(
        canvas: Canvas,
        borderWidth: Int,
        borderColor: Int,
        rectF: RectF,
        radii: FloatArray
    ) {
        initBorderPaint(borderWidth, borderColor)
        path.addRoundRect(rectF, radii, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }

    private fun initBorderPaint(borderWidth: Int, borderColor: Int) {
        path.reset()
        paint.strokeWidth = borderWidth.toFloat()
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
    }

    /**
     * 计算外边框的RectF
     */
    private fun initBorderRectF() {
        if (!isCircle) {
            borderRectF[borderWidth / 2.0f, borderWidth / 2.0f, _width - borderWidth / 2.0f] =
                _height - borderWidth / 2.0f
        }
    }

    /**
     * 计算图片原始区域的RectF
     */
    private fun initSrcRectF() {
        if (isCircle) {
            radius = _width.coerceAtMost(_height) / 2.0f
            srcRectF[_width / 2.0f - radius, _height / 2.0f - radius, _width / 2.0f + radius] =
                _height / 2.0f + radius
        } else {
            srcRectF[0f, 0f, _width.toFloat()] = _height.toFloat()
            if (isCoverSrc) {
                srcRectF = borderRectF
            }
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private fun calculateRadii() {
        if (isCircle) {
            return
        }
        if (cornerRadius > 0) {
            for (i in borderRadii.indices) {
                borderRadii[i] = cornerRadius.toFloat()
                srcRadii[i] = cornerRadius - borderWidth / 2.0f
            }
        } else {
            borderRadii[1] = cornerTopLeftRadius.toFloat()
            borderRadii[0] = borderRadii[1]
            borderRadii[3] = cornerTopRightRadius.toFloat()
            borderRadii[2] = borderRadii[3]
            borderRadii[5] = cornerBottomRightRadius.toFloat()
            borderRadii[4] = borderRadii[5]
            borderRadii[7] = cornerBottomLeftRadius.toFloat()
            borderRadii[6] = borderRadii[7]
            srcRadii[1] = cornerTopLeftRadius - borderWidth / 2.0f
            srcRadii[0] = srcRadii[1]
            srcRadii[3] = cornerTopRightRadius - borderWidth / 2.0f
            srcRadii[2] = srcRadii[3]
            srcRadii[5] = cornerBottomRightRadius - borderWidth / 2.0f
            srcRadii[4] = srcRadii[5]
            srcRadii[7] = cornerBottomLeftRadius - borderWidth / 2.0f
            srcRadii[6] = srcRadii[7]
        }
    }

    private fun calculateRadiiAndRectF(reset: Boolean) {
        if (reset) {
            cornerRadius = 0
        }
        calculateRadii()
        initBorderRectF()
        invalidate()
    }

    /**
     * 目前圆角矩形情况下不支持inner_border，需要将其置0
     */
    private fun clearInnerBorderWidth() {
        if (!isCircle) {
            innerBorderWidth = 0
        }
    }

    fun isCoverSrc(isCoverSrc: Boolean) {
        this.isCoverSrc = isCoverSrc
        initSrcRectF()
        invalidate()
    }

    fun isCircle(isCircle: Boolean) {
        this.isCircle = isCircle
        clearInnerBorderWidth()
        initSrcRectF()
        invalidate()
    }

    fun setBorderWidth(borderWidth: Float) {
        this.borderWidth = dp2px(context, borderWidth)
        calculateRadiiAndRectF(false)
    }

    fun setBorderColor(@ColorInt borderColor: Int) {
        this.borderColor = borderColor
        invalidate()
    }

    fun setInnerBorderWidth(innerBorderWidth: Float) {
        this.innerBorderWidth = dp2px(context, innerBorderWidth)
        clearInnerBorderWidth()
        invalidate()
    }

    fun setInnerBorderColor(@ColorInt innerBorderColor: Int) {
        this.innerBorderColor = innerBorderColor
        invalidate()
    }

    fun setCornerRadiusPx(cornerRadius: Int) {
        this.cornerRadius = cornerRadius
        calculateRadiiAndRectF(false)
    }

    fun setCornerRadius(cornerRadius: Int) {
        this.cornerRadius = dp2px(context, cornerRadius.toFloat())
        calculateRadiiAndRectF(false)
    }

    fun setCornerTopLeftRadius(cornerTopLeftRadius: Int) {
        this.cornerTopLeftRadius = dp2px(context, cornerTopLeftRadius.toFloat())
        calculateRadiiAndRectF(true)
    }

    fun setCornerTopRightRadius(cornerTopRightRadius: Int) {
        this.cornerTopRightRadius = dp2px(context, cornerTopRightRadius.toFloat())
        calculateRadiiAndRectF(true)
    }

    fun setCornerBottomLeftRadius(cornerBottomLeftRadius: Int) {
        this.cornerBottomLeftRadius = dp2px(context, cornerBottomLeftRadius.toFloat())
        calculateRadiiAndRectF(true)
    }

    fun setCornerBottomRightRadius(cornerBottomRightRadius: Int) {
        this.cornerBottomRightRadius = dp2px(
            context, cornerBottomRightRadius.toFloat()
        )
        calculateRadiiAndRectF(true)
    }

    fun setMaskColor(@ColorInt maskColor: Int) {
        this.maskColor = maskColor
        invalidate()
    }

    fun getRatio(): Float {
        return ratio
    }

    fun setRatio(ratio: Float) {
        this.ratio = ratio
        invalidate()
    }

    private fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}