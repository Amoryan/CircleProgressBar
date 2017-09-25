package com.amoryan.demo.kotlin

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @author: Domino
 * @className: CircleProgressBar
 * @description:
 * @createTime: 11/09/2017 10:24 AM
 */
class CircleProgressBar : View {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var strokeWidth: Float = 0f
    private var capStyle: Int = 0

    private var radius: Float = 0f
    private var startDegree: Float = 0f
    private var rotateDegree: Float = 0f
    private var sweepDegree: Float = 360f

    private var backColor: Int = 0
    private var useGradient: Boolean = false
    private var progressColor: Int = 0
    private var startColor: Int = 0
    private var endColor: Int = 0

    private var openAnimation: Boolean = false
    private var duration: Long = 500

    private var maxProgress: Float = 1f
    private var progress: Float = 0f

    private var progressDegree: Float = 0f
    private var drawDegree: Float = 0f

    private lateinit var onProgressChangeListener: OnProgressChangedListener
    private var animate: ObjectAnimator? = null

    constructor(context: Context) : super(context) {
        initData(null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initData(attributeSet)
    }

    private fun initData(attributeSet: AttributeSet?) {
        var attrs = context.obtainStyledAttributes(attributeSet, R.styleable.CircleProgressBar)

        strokeWidth = attrs.getDimension(R.styleable.CircleProgressBar_strokeWidth, 0f)
        capStyle = attrs.getInt(R.styleable.CircleProgressBar_capStyle, 0)

        radius = attrs.getDimension(R.styleable.CircleProgressBar_radius, 0f)
        startDegree = attrs.getFloat(R.styleable.CircleProgressBar_startDegree, 0f)
        rotateDegree = attrs.getFloat(R.styleable.CircleProgressBar_rotateDegree, 0f)
        sweepDegree = attrs.getFloat(R.styleable.CircleProgressBar_sweepDegree, 360f)

        backColor = attrs.getColor(R.styleable.CircleProgressBar_backColor, Color.parseColor("#e6eef6"))
        useGradient = attrs.getBoolean(R.styleable.CircleProgressBar_useGradient, false)
        progressColor = attrs.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#41a9f8"))
        startColor = attrs.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#21ADF1"))
        endColor = attrs.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#2287EE"))

        openAnimation = attrs.getBoolean(R.styleable.CircleProgressBar_openAnimation, false)
        duration = attrs.getInt(R.styleable.CircleProgressBar_duration, 500).toLong()

        maxProgress = attrs.getFloat(R.styleable.CircleProgressBar_maxProgress, 1f)
        progress = attrs.getFloat(R.styleable.CircleProgressBar_progress, 0f)

        setPaint()
        update()
    }

    private fun setPaint() {
        paint.strokeWidth = strokeWidth
        when (capStyle) {
            1 -> {
                paint.strokeCap = Paint.Cap.ROUND
            }
            2 -> {
                paint.strokeCap = Paint.Cap.SQUARE
            }
            else -> {
                paint.strokeCap = Paint.Cap.BUTT
            }
        }
        paint.style = Paint.Style.STROKE
    }

    fun setOnProgressChangeListener(listener: OnProgressChangedListener) {
        onProgressChangeListener = listener
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        setPaint()
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun setStartDegree(degree: Float) {
        startDegree = degree
    }

    fun setRotateDegree(degree: Float) {
        rotateDegree = degree
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun setSweepDegree(degree: Float) {
        sweepDegree = degree
    }

    fun setBackColor(color: Int) {
        backColor = color
    }

    fun setUseGradient(flag: Boolean) {
        useGradient = flag
    }

    fun setStartColor(color: Int) {
        startColor = color
    }

    fun setEndColor(color: Int) {
        endColor = color
    }

    fun openAnimation(flag: Boolean) {
        openAnimation = flag
    }

    fun setMaxProgress(max: Float) {
        maxProgress = max
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        update()
    }

    fun setDrawDegree(degree: Float) {
        drawDegree = degree
        invalidate()
    }

    fun update() {
        progress = Math.min(progress, maxProgress)
        progressDegree = (progress / maxProgress * sweepDegree)
        drawDegree = 0f

        if (openAnimation) {
            if (animate != null) {
                animate!!.cancel()
            }
            animate = ObjectAnimator.ofFloat(this, "drawDegree", progressDegree)
            animate!!.duration = duration
            animate!!.start()
        }

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getMeasureSize(widthMeasureSpec), getMeasureSize(heightMeasureSpec))
    }

    private fun getMeasureSize(measureSpec: Int): Int {
        var specMode = MeasureSpec.getMode(measureSpec)
        var specSize = MeasureSpec.getSize(measureSpec)
        var result: Int
        when (specMode) {
            MeasureSpec.EXACTLY -> {/*如果是MATCH_PARENT或者固定的值，最终的大小就是specSize*/
                result = specSize
            }
            MeasureSpec.AT_MOST -> {/*如果是WRAP_CONTENT，最终的大小不会超过specSize*/
                var temp = (Math.ceil((radius.toDouble() + strokeWidth) * 2) + paddingLeft + paddingRight).toInt()
                result = Math.min(temp, specSize)
            }
            else -> {
                result = specSize
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return

        var centerX = width / 2
        var centerY = height / 2

        var useWidth = width - paddingLeft - paddingRight - strokeWidth * 2
        var useHeight = height - paddingTop - paddingBottom - strokeWidth * 2
        var temp = Math.floor((Math.min(useWidth, useHeight) / 2).toDouble()).toFloat()
        radius = Math.min(temp, radius)

        canvas.save()
        if (rotateDegree != 0f) {
            canvas.rotate(rotateDegree, centerX.toFloat(), centerY.toFloat())
        }
        var rect = RectF(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
        )
        drawBack(canvas, rect)
        drawProgress(canvas, centerX, centerY, rect)
        canvas.restore()
    }

    private fun drawBack(canvas: Canvas, rect: RectF) {
        paint.shader = null
        paint.color = backColor
        canvas.drawArc(rect, startDegree, sweepDegree, false, paint)
    }

    private fun drawProgress(canvas: Canvas, centerX: Int, centerY: Int, rect: RectF) {
        paint.color = progressColor
        if (useGradient) {
            paint.shader = SweepGradient(centerX.toFloat(), centerY.toFloat(), startColor, endColor)
        }
        if (openAnimation) {
            canvas.drawArc(rect, startDegree, drawDegree, false, paint)
            if (onProgressChangeListener != null) {
                var temp: Float
                if (drawDegree == progressDegree) {
                    temp = progress
                } else {
                    temp = drawDegree * maxProgress / sweepDegree
                }
                onProgressChangeListener.onProgressChanged(temp)
            }
        } else {
            canvas.drawArc(rect, startDegree, progressDegree, false, paint)
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onProgressChanged(progress)
            }
        }
    }

    interface OnProgressChangedListener {
        fun onProgressChanged(progress: Float)
    }
}