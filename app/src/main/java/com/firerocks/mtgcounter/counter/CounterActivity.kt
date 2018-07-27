package com.firerocks.mtgcounter.counter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.helpers.GameType
import com.firerocks.mtgcounter.helpers.Operator
import com.firerocks.mtgcounter.helpers.PlayerID
import com.firerocks.mtgcounter.root.App
import com.firerocks.mtgcounter.views.CustomFontTextView
import javax.inject.Inject

class CounterActivity : AppCompatActivity(), CounterMVP.View {

    private val TAG = "CounterActivity"

    @Inject lateinit var presenter: CounterMVP.Presenter
    private lateinit var mMainView: ConstraintLayout

    private val mPlayerLifeIDs = listOf(R.id.player_one_health,
            R.id.player_two_health,
            R.id.player_three_health,
            R.id.player_four_health)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)
        presenter.setView(this)

        presenter.twoPlayerGame()
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
            val playerID = PlayerID.valueOf(view.tag.toString())
            presenter.updatePlayerName(playerID, name) {
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
        val playerId: PlayerID = PlayerID.valueOf(splitTag[0])
        val operator: Operator = Operator.valueOf(splitTag[1])
        presenter.updatePlayerHealth(playerId, operator) { playerID, health ->
            val healthView: CustomFontTextView
            when (playerID) {
                PlayerID.ONE -> {
                    healthView = findViewById(R.id.player_one_health)
                    healthView.text = health.toString()
                }
                PlayerID.TWO -> {
                    healthView = findViewById(R.id.player_two_health)
                    healthView.text = health.toString()
                }
                PlayerID.THREE -> {
                    healthView = findViewById(R.id.player_three_health)
                    healthView.text = health.toString()
                }
                PlayerID.FOUR -> {
                    healthView = findViewById(R.id.player_four_health)
                    healthView.text = health.toString()
                }
            }
        }
    }

    override fun getDefaultHealth(gameType: GameType) : Int {
        return if (gameType == GameType.NORMAL) {
            resources.getInteger(R.integer.default_player_health)
        } else {
            resources.getInteger(R.integer.two_headed_giant)
        }
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
        Snackbar.make(mMainView
                , resources.getString(R.string.player_dead, deadPlayer)
                , Snackbar.LENGTH_INDEFINITE)
                .setAction(resources.getString(R.string.new_game)) {
                    presenter.resetAllPlayersHealth { health, size ->
                        resetAllPlayersHealth(health, size)
                    }
                }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            R.id.menu_new_game -> {
                presenter.resetAllPlayersHealth { health, size ->
                    resetAllPlayersHealth(health, size)
                }
                return true
            }
            R.id.menu_two_players -> {
                presenter.twoPlayerGame()
            }
            R.id.menu_three_players -> {
                presenter.threePlayerGame()
            }
            R.id.menu_four_players -> {
                presenter.fourPlayerGame()
            }
            R.id.menu_two_headed_giant -> {
                presenter.twoHeadedGiantGame()
            }
            R.id.menu_bluetooth -> {

            }
        }

        return false
    }

    override fun threePlayerGame() {
        setContentView(R.layout.counter_view_three_player)
        mMainView = findViewById(R.id.main_view)
    }

    override fun fourPlayerGame() {
        setContentView(R.layout.counter_view_four_player)
        mMainView = findViewById(R.id.main_view)
    }

    override fun twoPlayerGame() {
        setContentView(R.layout.counter_view)
        mMainView = findViewById(R.id.main_view)
    }

    private fun resetAllPlayersHealth(healthValue: Int, numPlayers: Int) {
        for (i in 0..(numPlayers -1)) {
            findViewById<CustomFontTextView>(mPlayerLifeIDs[i]).text = healthValue.toString()
        }
    }

    override fun twoHeadedGiantGame(health: Int) {
        setContentView(R.layout.counter_view)
        mMainView = findViewById(R.id.main_view)
        resetAllPlayersHealth(health, 2)
    }
}
