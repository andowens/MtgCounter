package com.firerocks.mtgcounter.counter

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.InputType
import android.util.Log
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.root.App
import kotlinx.android.synthetic.main.counter_view.*
import javax.inject.Inject

class CounterActivity : AppCompatActivity(), CounterMVP.View {

    @Inject lateinit var presenter: CounterMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter_view)

        (application as App).appComponent.inject(this)
        presenter.setView(this)

        presenter.addPlayer {
            player_one_health.text = it.health.toString()
            player_one_name.text = it.name
        }

        presenter.addPlayer {
            player_two_health.text = it.health.toString()
            player_two_name.text = it.name
        }
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

    fun updatePlayerHealth(view: View) {
        val tag = view.tag.toString()
        val splitTag = tag.split("_")
        presenter.updatePlayerHealth(splitTag[0].toInt(), splitTag[1]) {
            when (splitTag[0].toInt()) {
                1 -> {
                    player_one_health.text = it.toString()
                }
                2 -> player_two_health.text = it.toString()

            }
        }
    }

    override fun getDefaultHealth() : Int {
        return resources.getInteger(R.integer.default_player_health)
    }

    override fun getPlayerDefaultName(playerNum: Int): String {
        return when (playerNum) {
            1 -> resources.getString(R.string.player_one_default_name)
            2 -> resources.getString(R.string.player_two_default_name)
            3 -> resources.getString(R.string.player_three_default_name)
            4 -> resources.getString(R.string.player_four_default_name)
            else -> ""
        }
    }

    override fun launchPlayerDeadSnackBar(deadPlayer: String) {
        Snackbar.make(main_view
                , resources.getString(R.string.player_dead, deadPlayer)
                , Snackbar.LENGTH_INDEFINITE)
                .setAction(resources.getString(R.string.new_game)) {
                    presenter.resetPlayerHealth {
                        player_one_health.text = it.toString()
                        player_two_health.text = it.toString()
                    }
                }.show()
    }
}
