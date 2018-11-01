package com.firerocks.mtgcounter.helpers

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AnimatorRes
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.views.CustomFontTextView
import java.util.*

fun changeNameDialog(context: Context, okClicked: (String) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.change_name))

    val editText = AppCompatEditText(context)
    editText.inputType = InputType.TYPE_CLASS_TEXT
    builder.setView(editText)

    // Set up the buttons
    builder.setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
        val name = editText.text.toString()
        okClicked(name)
    }

    builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->
        dialog.cancel()
    }
    builder.show()
}

fun animateView(context: Context, view: View, @AnimatorRes animationRes: Int) {
    val healthAnimator = AnimatorInflater.loadAnimator(context, animationRes) as AnimatorSet
    healthAnimator.setTarget(view)
    healthAnimator.start()
}