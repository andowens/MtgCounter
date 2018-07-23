package com.firerocks.mtgcounter.counter

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.InputType
import android.util.Log
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.root.App
import javax.inject.Inject

class CounterActivity : AppCompatActivity(), CounterMVP.View {

    @Inject lateinit var presenter: CounterMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter_view)

        (application as App).appComponent.inject(this)
        presenter.setView(this)
    }

    fun changeName(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.change_name))

        val editText = AppCompatEditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(editText)

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            val name = editText.text.toString()
            presenter.updatePlayerName(view.tag.toString().toInt(), name) {
                (view as AppCompatTextView).text = it
            }
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun changePlayerName(name: String) {
        Log.i("Tag", name)
    }
}
