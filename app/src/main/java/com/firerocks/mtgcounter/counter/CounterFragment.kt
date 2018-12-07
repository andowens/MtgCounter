package com.firerocks.mtgcounter.counter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.widget.AppCompatTextView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.bluetooth.BluetoothActivity
import com.firerocks.mtgcounter.helpers.*
import com.firerocks.mtgcounter.root.App
import com.firerocks.mtgcounter.search.ui.CardSearchActivity
import com.firerocks.mtgcounter.views.CustomFontTextView
import javax.inject.Inject

class CounterFragment : Fragment(), CounterMVP.View {

    private val TAG = "CounterFragment"

    @Inject lateinit var presenter: CounterMVP.Presenter
    private lateinit var mMainView: androidx.constraintlayout.widget.ConstraintLayout

    private val mPlayerLifeIDs = listOf(R.id.player_one_health,
            R.id.player_two_health,
            R.id.player_three_health,
            R.id.player_four_health)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as App).appComponent.inject(this)
        presenter.setView(this)

        presenter.twoPlayerGame()
    }

    fun changeName(view: View) {

        changeNameDialog(this) { name ->
            val playerID = PlayerID.valueOf(view.tag.toString())
            presenter.updatePlayerName(playerID, name) {
                (view as AppCompatTextView).text = it
            }
        }
    }

    fun updatePlayerHealth(view: View) {
        animateView(activity?.applicationContext, view, R.animator.chevron_animation)

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
            animateView(applicationContext, healthView, R.animator.health_animation)
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
        com.google.android.material.snackbar.Snackbar.make(mMainView
                , resources.getString(R.string.player_dead, deadPlayer)
                , com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
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
            R.id.menu_search -> {
                val intent = Intent(this, CardSearchActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_roll -> {
                presenter.rollDieClicked()
                return true
            }
            R.id.menu_two_players -> {
                presenter.twoPlayerGame()
                return true
            }
            R.id.menu_three_players -> {
                presenter.threePlayerGame()
                return true
            }
            R.id.menu_four_players -> {
                presenter.fourPlayerGame()
                return true
            }
            R.id.menu_two_headed_giant -> {
                presenter.twoHeadedGiantGame()
                return true
            }
            R.id.menu_bluetooth -> {
                val intent = Intent(this, BluetoothActivity::class.java)
                startActivity(intent)
                return true
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

    override fun showDieRolled(roll: String) {
        Snackbar.make(mMainView, "Die roll: $roll", Snackbar.LENGTH_LONG).show()
    }
}
