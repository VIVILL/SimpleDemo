package com.example.banner.indicator

import androidx.annotation.AnimatorRes
import androidx.annotation.DrawableRes
import android.widget.LinearLayout
import android.view.Gravity
import com.example.banner.R

class IndicatorConfig internal constructor() {
    var width = -1
    var height = -1
    var margin = -1

    @AnimatorRes
    var animatorResId = R.animator.scale_with_alpha

    @AnimatorRes
    var animatorReverseResId = 0

    @DrawableRes
    var backgroundResId = R.drawable.white_radius

    @DrawableRes
    var unselectedBackgroundId = 0
    var orientation = LinearLayout.HORIZONTAL
    var gravity = Gravity.CENTER

    class Builder {
        private val mIndicatorConfig: IndicatorConfig = IndicatorConfig()

        fun width(width: Int): Builder {
            mIndicatorConfig.width = width
            return this
        }

        fun height(height: Int): Builder {
            mIndicatorConfig.height = height
            return this
        }

        fun margin(margin: Int): Builder {
            mIndicatorConfig.margin = margin
            return this
        }

        fun animator(@AnimatorRes animatorResId: Int): Builder {
            mIndicatorConfig.animatorResId = animatorResId
            return this
        }

        fun animatorReverse(@AnimatorRes animatorReverseResId: Int): Builder {
            mIndicatorConfig.animatorReverseResId = animatorReverseResId
            return this
        }

        fun drawable(@DrawableRes backgroundResId: Int): Builder {
            mIndicatorConfig.backgroundResId = backgroundResId
            return this
        }

        fun drawableUnselected(@DrawableRes unselectedBackgroundId: Int): Builder {
            mIndicatorConfig.unselectedBackgroundId = unselectedBackgroundId
            return this
        }

        fun orientation(orientation: Int): Builder {
            mIndicatorConfig.orientation = orientation
            return this
        }

        fun gravity(gravity: Int): Builder {
            mIndicatorConfig.gravity = gravity
            return this
        }

        fun build(): IndicatorConfig {
            return mIndicatorConfig
        }
    }
}