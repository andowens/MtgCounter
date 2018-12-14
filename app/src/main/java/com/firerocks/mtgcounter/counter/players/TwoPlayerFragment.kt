package com.firerocks.mtgcounter.counter.players

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.widget.AppCompatTextView
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterMVP
import com.firerocks.mtgcounter.helpers.*
import com.firerocks.mtgcounter.views.CustomFontTextView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.counter_view.*
import javax.inject.Inject

class TwoPlayerFragment : DaggerFragment(), CounterMVP.View {

    private val TAG = "TwoPlayerFragment"

    @Inject lateinit var presenter: CounterMVP.Presenter

    companion object {

        fun newInstance() : TwoPlayerFragment = TwoPlayerFragment()
    }

    private val mPlayerLifeIDs = listOf(R.id.player_one_health,
            R.id.player_two_health,
            R.id.player_three_health,
            R.id.player_four_health)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the vew
        val view = inflater.inflate(R.layout.counter_view, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player_one_down_arrow.setOnClickListener {
            updatePlayerHealth(it)
        }

        player_one_up_arrow.setOnClickListener {
            updatePlayerHealth(it)
        }

        player_two_down_arrow.setOnClickListener{
            updatePlayerHealth(it)
        }

        player_two_up_arrow.setOnClickListener {
            updatePlayerHealth(it)
        }

        player_one_name.setOnClickListener {
            changeName(it)
        }

        player_two_name.setOnClickListener {
            changeName(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.setView(this)

        presenter.twoPlayerGame()
    }

    fun changeName(view: View) {

        activity?.let { activity ->
            changeNameDialog(activity) { name ->
                val playerID = PlayerID.valueOf(view.tag.toString())
                presenter.updatePlayerName(playerID, name) {
                    (view as AppCompatTextView).text = it
                }
            }
        }
    }

    fun updatePlayerHealth(view: View) {
        appContext { context ->
            animateView(context, view, R.animator.chevron_animation)
        }
        val tag = view.tag.toString()
        val splitTag = tag.split("_")
        val playerId: PlayerID = PlayerID.valueOf(splitTag[0])
        val operator: Operator = Operator.valueOf(splitTag[1])
        presenter.updatePlayerHealth(playerId, operator) { playerID, health ->
            val healthView: CustomFontTextView
            when (playerID) {
                PlayerID.ONE -> {
                    healthView = player_one_health
                    healthView.text = health.toString()
                }
                PlayerID.TWO -> {
                    healthView = player_two_health
                    healthView.text = health.toString()
                }
            }
            appContext { context ->
                animateView(context, healthView, R.animator.health_animation)
            }
        }
    }

    private fun appContext (lambda: (Context) -> Unit) {
        activity?.applicationContext?.let {
            lambda(it)
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
        Snackbar.make(main_view
                , resources.getString(R.string.player_dead, deadPlayer)
                , Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.new_game)) {
                    presenter.resetAllPlayersHealth { health, size ->
                        //resetAllPlayersHealth(health, size)
                    }
                }.show()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.main_menu, menu)
//        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        super.onOptionsItemSelected(item)
//
//        when (item?.itemId) {
//            R.id.menu_new_game -> {
//                presenter.resetAllPlayersHealth { health, size ->
//                    resetAllPlayersHealth(health, size)
//                }
//                return true
//            }
//            R.id.menu_search -> {
//                val intent = Intent(this, CardSearchActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menu_roll -> {
//                presenter.rollDieClicked()
//                return true
//            }
//            R.id.menu_two_players -> {
//                presenter.twoPlayerGame()
//                return true
//            }
//            R.id.menu_three_players -> {
//                presenter.threePlayerGame()
//                return true
//            }
//            R.id.menu_four_players -> {
//                presenter.fourPlayerGame()
//                return true
//            }
//            R.id.menu_two_headed_giant -> {
//                presenter.twoHeadedGiantGame()
//                return true
//            }
//            R.id.menu_bluetooth -> {
//                val intent = Intent(this, BluetoothActivity::class.java)
//                startActivity(intent)
//                return true
//            }
//        }
//
//        return false
//    }
//
    override fun threePlayerGame() {
//        setContentView(R.layout.counter_view_three_player)
//        mMainView = findViewById(R.id.main_view)
    }

    override fun fourPlayerGame() {
//        setContentView(R.layout.counter_view_four_player)
//        mMainView = findViewById(R.id.main_view)
    }

    override fun twoPlayerGame() {
//        setContentView(R.layout.counter_view)
//        mMainView = findViewById(R.id.main_view)
    }

    private fun resetAllPlayersHealth(healthValue: Int, numPlayers: Int) {
//        for (i in 0..(numPlayers -1)) {
//            findViewById<CustomFontTextView>(mPlayerLifeIDs[i]).text = healthValue.toString()
//        }
    }

    override fun twoHeadedGiantGame(health: Int) {
//        setContentView(R.layout.counter_view)
//        mMainView = findViewById(R.id.main_view)
//        resetAllPlayersHealth(health, 2)
    }

    override fun showDieRolled(roll: String) {
        Snackbar.make(main_view, "Die roll: $roll", Snackbar.LENGTH_LONG).show()
    }
}
