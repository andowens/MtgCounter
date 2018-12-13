package com.firerocks.mtgcounter.helpers

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.annotation.AnimatorRes

fun animateView(context: Context, view: View, @AnimatorRes animationRes: Int) {
    val healthAnimator = AnimatorInflater.loadAnimator(context, animationRes) as AnimatorSet
    healthAnimator.setTarget(view)
    healthAnimator.start()
}