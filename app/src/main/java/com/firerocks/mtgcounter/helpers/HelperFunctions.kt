package com.firerocks.mtgcounter.helpers

import android.content.Context
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import com.firerocks.mtgcounter.R

/**
 * For a lack of knowing were to put this I put it here just helps in
 * building dialogs used in multiple fragments probably could find a
 * better home for it
 *
 * @param context The context used in building the dialog box should use application context
 * @param okClicked Lambda used to notify the user that the ok button was clicked and they should
 *                  take action.
 */
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