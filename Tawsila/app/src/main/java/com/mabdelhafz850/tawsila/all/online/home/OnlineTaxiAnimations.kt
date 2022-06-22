package com.mabdelhafz850.tawsila.ui.activity.all.online.home

import android.animation.ObjectAnimator
import android.view.View

object OnlineTaxiAnimations {
    @JvmStatic
    fun animationTranslationY(view: View?, value: Int) {
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f, value.toFloat())
        scaleYAnimator.duration = 800
        scaleYAnimator.start()
    }

    @JvmStatic
    fun animationTranslationYAfterOtherAnimate(view: View?, value: Int) {
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "translationY", value.toFloat())
        scaleYAnimator.duration = 600
        scaleYAnimator.startDelay = 600
        scaleYAnimator.start()
    }
}