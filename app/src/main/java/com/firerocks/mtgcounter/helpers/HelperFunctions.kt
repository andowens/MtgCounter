package com.firerocks.mtgcounter.helpers

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
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

fun rollDiceDialog(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.roll_dice))

    val textView = CustomFontTextView(context)
    textView.gravity = Gravity.CENTER_HORIZONTAL
    textView.textSize = 30f
    builder.setView(textView)

    // Set up buttons
    builder.setNeutralButton(context.getString(R.string.roll), null)
    builder.setNegativeButton(context.getString(R.string.cancel), null)

    val alertDialog = builder.create()
    alertDialog.setOnShowListener {
        val neutralButton = (it as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
        neutralButton.setOnClickListener {
            textView.text = Random().nextInt(20).toString()
        }
    }

    alertDialog.show()
}