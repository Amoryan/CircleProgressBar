package com.amoryan.demo.kotlin

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

    private lateinit var mContext: Context

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mStrokeWidth: Float = 0f
    private var mCapStyle: Int = 0

    private var mRadius: Float = 0f
    private var mStartDegree: Float = 0f
    private var mRotateDegree: Float = 0f
    private var mSweepDegree: Float = 360f

    private var mBackColor: Int = 0
    private var useGradient: Boolean = false
    private var mProgressColor: Int = 0
    private var mStartColor: Int = 0
    private var mEndColor: Int = 0

    private var openAnimation: Boolean = false
    private var animVelocity: Int = 1

    private var mMaxProgress: Float = 1f
    private var mProgress: Float = 0f

    private var mProgressDegree: Float = 0f
    private var mDrawDegree: Float = 0f

    private lateinit var onProgressChangeListener: OnProgressChangedListener

    constructor(context: Context) : super(context) {
        initData(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initData(context, attributeSet)
    }

    private fun initData(context: Context, attributeSet: AttributeSet?) {
        mContext = context

        var attrs = mContext.obtainStyledAttributes(attributeSet, R.styleable.CircleProgressBar)

        mStrokeWidth = attrs.getDimension(R.styleable.CircleProgressBar_strokeWidth, 0f)
        mCapStyle = attrs.getInt(R.styleable.CircleProgressBar_capStyle, 0)

        mRadius = attrs.getDimension(R.styleable.CircleProgressBar_radius, 0f)
        mStartDegree = attrs.getFloat(R.styleable.CircleProgressBar_startDegree, 0f)
        mRotateDegree = attrs.getFloat(R.styleable.CircleProgressBar_rotateDegree, 0f)
        mSweepDegree = attrs.getFloat(R.styleable.CircleProgressBar_sweepDegree, 360f)

        mBackColor = attrs.getColor(R.styleable.CircleProgressBar_backColor, Color.parseColor("#e6eef6"))
        useGradient = attrs.getBoolean(R.styleable.CircleProgressBar_useGradient, false)
        mProgressColor = attrs.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#41a9f8"))
        mStartColor = attrs.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#21ADF1"))
        mEndColor = attrs.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#2287EE"))

        openAnimation = attrs.getBoolean(R.styleable.CircleProgressBar_openAnimation, false)
        animVelocity = attrs.getInt(R.styleable.CircleProgressBar_animVelocity, 1)

        mMaxProgress = attrs.getFloat(R.styleable.CircleProgressBar_maxProgress, 1f)
        mProgress = attrs.getFloat(R.styleable.CircleProgressBar_progress, 0f)

        setPaint()
        mProgress = Math.min(mMaxProgress, mProgress)
        mProgressDegree = mProgress / mMaxProgress * mSweepDegree
        mDrawDegree = 0f
    }

    private fun setPaint() {
        mPaint.strokeWidth = mStrokeWidth
        when (mCapStyle) {
            1 -> {
                mPaint.strokeCap = Paint.Cap.ROUND
            }
            2 -> {
                mPaint.strokeCap = Paint.Cap.SQUARE
            }
            else -> {
                mPaint.strokeCap = Paint.Cap.BUTT
            }
        }
        mPaint.style = Paint.Style.STROKE
    }

    fun setOnProgressChangeListener(listener: OnProgressChangedListener) {
        onProgressChangeListener = listener
    }

    fun setStrokeWidth(width: Float) {
        mStrokeWidth = width
        setPaint()
    }

    fun setRadius(radius: Float) {
        mRadius = radius
    }

    fun setStartDegree(degree: Float) {
        mStartDegree = degree
    }

    fun setRotateDegree(degree: Float) {
        mRotateDegree = degree
    }

    fun setSweepDegree(degree: Float) {
        mSweepDegree = degree
    }

    fun setBackColor(color: Int) {
        mBackColor = color
    }

    fun setUseGradient(flag: Boolean) {
        useGradient = flag
    }

    fun setStartColor(color: Int) {
        mStartColor = color
    }

    fun setEndColor(color: Int) {
        mEndColor = color
    }

    fun openAnimation(flag: Boolean) {
        openAnimation = flag
    }

    fun setMaxProgress(max: Float) {
        mMaxProgress = max
    }

    fun setProgress(progress: Float) {
        mProgress = progress
    }

    fun update() {
        mProgress = Math.min(mProgress, mMaxProgress)
        mProgressDegree = (mProgress / mMaxProgress * mSweepDegree)
        mDrawDegree = 0f
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
                var temp = (Math.ceil((mRadius.toDouble() + mStrokeWidth) * 2) + paddingLeft + paddingRight).toInt()
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

        mWidth = measuredWidth
        mHeight = measuredHeight

        var centerX = mWidth / 2
        var centerY = mHeight / 2

        var useWidth = mWidth - paddingLeft - paddingRight - mStrokeWidth * 2
        var useHeight = mHeight - paddingTop - paddingBottom - mStrokeWidth * 2
        var temp = Math.floor((Math.min(useWidth, useHeight) / 2).toDouble()).toFloat()
        mRadius = Math.min(temp, mRadius)

        canvas.save()
        if (mRotateDegree != 0f) {
            canvas.rotate(mRotateDegree, centerX.toFloat(), centerY.toFloat())
        }
        var rect = RectF(
                centerX - mRadius,
                centerY - mRadius,
                centerX + mRadius,
                centerY + mRadius
        )
        drawBack(canvas, rect)
        drawProgress(canvas, centerX, centerY, rect)
        canvas.restore()
    }

    private fun drawBack(canvas: Canvas, rect: RectF) {
        mPaint.shader = null
        mPaint.color = mBackColor
        canvas.drawArc(rect, mStartDegree, mSweepDegree, false, mPaint)
    }

    private fun drawProgress(canvas: Canvas, centerX: Int, centerY: Int, rect: RectF) {
        mPaint.color = mProgressColor
        if (useGradient) {
            mPaint.shader = SweepGradient(centerX.toFloat(), centerY.toFloat(), mStartColor, mEndColor)
        }
        if (openAnimation) {
            canvas.drawArc(rect, mStartDegree, mDrawDegree, false, mPaint)
            if (onProgressChangeListener != null) {
                var temp: Float
                if (mDrawDegree == mProgressDegree) {
                    temp = mProgress
                } else {
                    temp = mDrawDegree * mMaxProgress / mSweepDegree
                }
                onProgressChangeListener.onProgressChanged(temp)
            }
            if (mDrawDegree < mProgress) {
                mDrawDegree += animVelocity
                mDrawDegree = Math.min(mDrawDegree, mProgress)
                invalidate()
            }
        } else {
            canvas.drawArc(rect, mStartDegree, mProgressDegree, false, mPaint)
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onProgressChanged(mProgress)
            }
        }
    }

    interface OnProgressChangedListener {
        fun onProgressChanged(progress: Float)
    }
}