package com.firerocks.mtgcounter.helpers

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.annotation.AnimatorRes

/**
 * Helper function that animates the view passed into it using the animator resource
 *
 * @param context The context to use should probably always you application context
 * @param view The view to animate
 * @param animationRes The animation resource to use to animate the view
 */
fun animateView(context: Context, view: View, @AnimatorRes animationRes: Int) {
    val animator = AnimatorInflater.loadAnimator(context, animationRes) as AnimatorSet
    animator.setTarget(view)
    animator.start()
}