package com.example.banner.indicator

import android.animation.Animator
import android.widget.LinearLayout
import android.content.res.ColorStateList
import android.annotation.TargetApi
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import kotlin.jvm.JvmOverloads
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.example.banner.R
import kotlin.math.abs

open class BaseCircleIndicator : LinearLayout {
    protected var mIndicatorMargin = -1
    protected var mIndicatorWidth = -1
    protected var mIndicatorHeight = -1
    protected var mIndicatorBackgroundResId = 0
    protected var mIndicatorUnselectedBackgroundResId = 0
    protected var mIndicatorTintColor: ColorStateList? = null
    protected var mIndicatorTintUnselectedColor: ColorStateList? = null
    protected var mAnimatorOut: Animator? = null
    protected var mAnimatorIn: Animator? = null
    protected var mImmediateAnimatorOut: Animator? = null
    protected var mImmediateAnimatorIn: Animator? = null
    @JvmField
    protected var mLastPosition = -1
    private var mIndicatorCreatedListener: IndicatorCreatedListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val config = handleTypedArray(context, attrs)
        initialize(config)
        if (isInEditMode) {
            createIndicators(3, 1)
        }
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?): IndicatorConfig {
        val indicatorConfig = IndicatorConfig()
        if (attrs == null) {
            return indicatorConfig
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseCircleIndicator)
        indicatorConfig.width =
            typedArray.getDimensionPixelSize(R.styleable.BaseCircleIndicator_ci_width, -1)
        indicatorConfig.height =
            typedArray.getDimensionPixelSize(R.styleable.BaseCircleIndicator_ci_height, -1)
        indicatorConfig.margin =
            typedArray.getDimensionPixelSize(R.styleable.BaseCircleIndicator_ci_margin, -1)
        indicatorConfig.animatorResId = typedArray.getResourceId(
            R.styleable.BaseCircleIndicator_ci_animator,
            R.animator.scale_with_alpha
        )
        indicatorConfig.animatorReverseResId =
            typedArray.getResourceId(R.styleable.BaseCircleIndicator_ci_animator_reverse, 0)
        indicatorConfig.backgroundResId = typedArray.getResourceId(
            R.styleable.BaseCircleIndicator_ci_drawable,
            R.drawable.white_radius
        )
        indicatorConfig.unselectedBackgroundId = typedArray.getResourceId(
            R.styleable.BaseCircleIndicator_ci_drawable_unselected,
            indicatorConfig.backgroundResId
        )
        indicatorConfig.orientation = typedArray.getInt(R.styleable.BaseCircleIndicator_ci_orientation, -1)
        indicatorConfig.gravity = typedArray.getInt(R.styleable.BaseCircleIndicator_ci_gravity, -1)
        typedArray.recycle()
        return indicatorConfig
    }

    fun initialize(indicatorConfig: IndicatorConfig) {
        val miniSize = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_INDICATOR_WIDTH.toFloat(), resources.displayMetrics
        ) + 0.5f).toInt()
        mIndicatorWidth = if (indicatorConfig.width < 0) miniSize else indicatorConfig.width
        mIndicatorHeight = if (indicatorConfig.height < 0) miniSize else indicatorConfig.height
        mIndicatorMargin = if (indicatorConfig.margin < 0) miniSize else indicatorConfig.margin
        mAnimatorOut = createAnimatorOut(indicatorConfig)
        mImmediateAnimatorOut = createAnimatorOut(indicatorConfig)
        mImmediateAnimatorOut!!.duration = 0
        mAnimatorIn = createAnimatorIn(indicatorConfig)
        mImmediateAnimatorIn = createAnimatorIn(indicatorConfig)
        mImmediateAnimatorIn!!.duration = 0
        mIndicatorBackgroundResId =
            if (indicatorConfig.backgroundResId == 0) R.drawable.white_radius else indicatorConfig.backgroundResId
        mIndicatorUnselectedBackgroundResId =
            if (indicatorConfig.unselectedBackgroundId == 0) indicatorConfig.backgroundResId else indicatorConfig.unselectedBackgroundId
        orientation =
            if (indicatorConfig.orientation == VERTICAL) VERTICAL else HORIZONTAL
        gravity = if (indicatorConfig.gravity >= 0) indicatorConfig.gravity else Gravity.CENTER
    }

    @JvmOverloads
    fun tintIndicator(
        @ColorInt indicatorColor: Int,
        @ColorInt unselectedIndicatorColor: Int = indicatorColor
    ) {
        mIndicatorTintColor = ColorStateList.valueOf(indicatorColor)
        mIndicatorTintUnselectedColor = ColorStateList.valueOf(unselectedIndicatorColor)
        changeIndicatorBackground()
    }

    @JvmOverloads
    fun changeIndicatorResource(
        @DrawableRes indicatorResId: Int,
        @DrawableRes indicatorUnselectedResId: Int = indicatorResId
    ) {
        mIndicatorBackgroundResId = indicatorResId
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedResId
        changeIndicatorBackground()
    }

    interface IndicatorCreatedListener {
        /**
         * IndicatorCreatedListener
         *
         * @param view internal indicator view
         * @param position position
         */
        fun onIndicatorCreated(view: View?, position: Int)
    }

    fun setIndicatorCreatedListener(
        indicatorCreatedListener: IndicatorCreatedListener?
    ) {
        mIndicatorCreatedListener = indicatorCreatedListener
    }

    protected fun createAnimatorOut(indicatorConfig: IndicatorConfig): Animator {
        return AnimatorInflater.loadAnimator(context, indicatorConfig.animatorResId)
    }

    protected fun createAnimatorIn(indicatorConfig: IndicatorConfig): Animator {
        val animatorIn: Animator
        if (indicatorConfig.animatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, indicatorConfig.animatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, indicatorConfig.animatorReverseResId)
        }
        return animatorIn
    }

    fun createIndicators(count: Int, currentPosition: Int) {
        if (mImmediateAnimatorOut!!.isRunning) {
            mImmediateAnimatorOut!!.end()
            mImmediateAnimatorOut!!.cancel()
        }
        if (mImmediateAnimatorIn!!.isRunning) {
            mImmediateAnimatorIn!!.end()
            mImmediateAnimatorIn!!.cancel()
        }

        // Diff View
        val childViewCount = childCount
        if (count < childViewCount) {
            removeViews(count, childViewCount - count)
        } else if (count > childViewCount) {
            val addCount = count - childViewCount
            val orientation = orientation
            for (i in 0 until addCount) {
                addIndicator(orientation)
            }
        }

        // Bind Style
        var indicator: View
        for (i in 0 until count) {
            indicator = getChildAt(i)
            if (currentPosition == i) {
                bindIndicatorBackground(indicator, mIndicatorBackgroundResId, mIndicatorTintColor)
                mImmediateAnimatorOut!!.setTarget(indicator)
                mImmediateAnimatorOut!!.start()
                mImmediateAnimatorOut!!.end()
            } else {
                bindIndicatorBackground(
                    indicator, mIndicatorUnselectedBackgroundResId,
                    mIndicatorTintUnselectedColor
                )
                mImmediateAnimatorIn!!.setTarget(indicator)
                mImmediateAnimatorIn!!.start()
                mImmediateAnimatorIn!!.end()
            }
            if (mIndicatorCreatedListener != null) {
                mIndicatorCreatedListener!!.onIndicatorCreated(indicator, i)
            }
        }
        mLastPosition = currentPosition
    }

    protected fun addIndicator(orientation: Int) {
        val indicator = View(context)
        val params = generateDefaultLayoutParams()
        params.width = mIndicatorWidth
        params.height = mIndicatorHeight
        if (orientation == HORIZONTAL) {
            params.leftMargin = mIndicatorMargin
            params.rightMargin = mIndicatorMargin
        } else {
            params.topMargin = mIndicatorMargin
            params.bottomMargin = mIndicatorMargin
        }
        addView(indicator, params)
    }

    fun animatePageSelected(position: Int) {
        if (mLastPosition == position) {
            return
        }
        if (mAnimatorIn!!.isRunning) {
            mAnimatorIn!!.end()
            mAnimatorIn!!.cancel()
        }
        if (mAnimatorOut!!.isRunning) {
            mAnimatorOut!!.end()
            mAnimatorOut!!.cancel()
        }
        lateinit var currentIndicator: View
        if (mLastPosition >= 0 && getChildAt(mLastPosition).also {
                currentIndicator = it
            } != null) {
            bindIndicatorBackground(
                currentIndicator, mIndicatorUnselectedBackgroundResId,
                mIndicatorTintUnselectedColor
            )
            mAnimatorIn!!.setTarget(currentIndicator)
            mAnimatorIn!!.start()
        }
        val selectedIndicator = getChildAt(position)
        if (selectedIndicator != null) {
            bindIndicatorBackground(
                selectedIndicator, mIndicatorBackgroundResId,
                mIndicatorTintColor
            )
            mAnimatorOut!!.setTarget(selectedIndicator)
            mAnimatorOut!!.start()
        }
        mLastPosition = position
    }

    protected fun changeIndicatorBackground() {
        val count = childCount
        if (count <= 0) {
            return
        }
        var currentIndicator: View
        for (i in 0 until count) {
            currentIndicator = getChildAt(i)
            if (i == mLastPosition) {
                bindIndicatorBackground(
                    currentIndicator, mIndicatorBackgroundResId,
                    mIndicatorTintColor
                )
            } else {
                bindIndicatorBackground(
                    currentIndicator, mIndicatorUnselectedBackgroundResId,
                    mIndicatorTintUnselectedColor
                )
            }
        }
    }

    private fun bindIndicatorBackground(
        view: View, @DrawableRes drawableRes: Int,
        tintColor: ColorStateList?
    ) {
        if (tintColor != null) {
            val indicatorDrawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(context, drawableRes)!!.mutate()
            )
            DrawableCompat.setTintList(indicatorDrawable, tintColor)
            ViewCompat.setBackground(view, indicatorDrawable)
        } else {
            view.setBackgroundResource(drawableRes)
        }
    }

    protected class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return abs(1.0f - value)
        }
    }

    companion object {
        private const val DEFAULT_INDICATOR_WIDTH = 5
    }
}