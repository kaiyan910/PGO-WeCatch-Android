package com.kennah.wecatch.local.utils

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

object AnimateUtils {

    fun rotateAnimate(): RotateAnimation {

        val anim = RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        anim.apply {
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            duration = 700
        }

        return anim
    }
}