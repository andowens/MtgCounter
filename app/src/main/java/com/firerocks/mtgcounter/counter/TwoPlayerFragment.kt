package com.firerocks.mtgcounter.counter

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.Visibility
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.helpers.*
import com.firerocks.mtgcounter.views.CustomFontTextView
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TwoPlayerFragment : DaggerFragment(), CounterMVP.View {

    private val TAG = "TwoPlayerFragment"

    @Inject lateinit var presenter: CounterMVP.Presenter
    private lateinit var mMainView: ConstraintLayout

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

        setupTwoPlayerGame()
    }

    private fun hideAllViews() {
        view?.let {
            it.findViewById<View>(R.id.two_main).visibility = View.GONE
            it.findViewById<View>(R.id.three_main).visibility = View.GONE
            it.findViewById<View>(R.id.four_main).visibility = View.GONE
        }
    }

    private fun setupTwoPlayerGame() {
        view?.let { act ->
            mMainView = act.findViewById(R.id.two_main)
            mMainView.visibility = View.VISIBLE

            mMainView.findViewById<AppCompatImageButton>(R.id.player_one_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_one_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_two_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_two_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_one_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_two_name).setOnClickListener {
                changeName(it)
            }
        }
    }

    private fun setupThreePlayerGame() {
        activity?.let { act ->

            mMainView = act.findViewById(R.id.three_main)
            mMainView.visibility = View.VISIBLE

            mMainView.findViewById<AppCompatImageButton>(R.id.player_one_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_one_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_two_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_two_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_three_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_three_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_one_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_two_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_three_name).setOnClickListener {
                changeName(it)
            }
        }
    }

    private fun setupFourPlayerGame() {
        activity?.let { act ->

            mMainView = act.findViewById(R.id.four_main)
            mMainView.visibility = View.VISIBLE

            mMainView.findViewById<AppCompatImageButton>(R.id.player_one_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_one_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_two_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_two_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_three_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_three_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_four_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.player_four_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_one_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_two_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_three_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.player_four_name).setOnClickListener {
                changeName(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_counter, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
            val healthView: CustomFontTextView?
            when (playerID) {
                PlayerID.ONE -> {
                        healthView = activity?.findViewById(R.id.player_one_health)
                        healthView?.text = health.toString()
                }
                PlayerID.TWO -> {
                    healthView = activity?.findViewById(R.id.player_two_health)
                    healthView?.text = health.toString()
                }
                PlayerID.THREE -> {
                    healthView = activity?.findViewById(R.id.player_three_health)
                    healthView?.text = health.toString()
                }
                PlayerID.FOUR -> {
                    healthView = activity?.findViewById(R.id.player_four_health)
                    healthView?.text = health.toString()
                }
            }
            appContext { context ->
                healthView?.let {
                    animateView(context, healthView, R.animator.health_animation)
                }
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
        Snackbar.make(mMainView
                , resources.getString(R.string.player_dead, deadPlayer)
                , Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.new_game)) {
                    presenter.resetAllPlayersHealth { health, size ->
                        //resetAllPlayersHealth(health, size)
                    }
                }.show()
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
            R.id.two_player_game -> {
                hideAllViews()
                presenter.twoPlayerGame()
                return true
            }
            R.id.three_player_game -> {
                hideAllViews()
                presenter.threePlayerGame()
                return true
            }
            R.id.four_player_game -> {
                hideAllViews()
                presenter.fourPlayerGame()
                return true
            }
            R.id.two_headed_giant_game -> {
                hideAllViews()
                presenter.twoHeadedGiantGame()
                return true
            }
        }

        return false
    }

    override fun threePlayerGame() {
        setupThreePlayerGame()
    }

    override fun fourPlayerGame() {
        setupFourPlayerGame()
    }

    override fun twoPlayerGame() {
        setupTwoPlayerGame()
    }

    private fun resetAllPlayersHealth(healthValue: Int, numPlayers: Int) {
//        for (i in 0..(numPlayers -1)) {
//            findViewById<CustomFontTextView>(mPlayerLifeIDs[i]).text = healthValue.toString()
//        }
    }

    override fun twoHeadedGiantGame(health: Int) {
        setupTwoPlayerGame()
//        resetAllPlayersHealth(health, 2)
    }

    override fun showDieRolled(roll: String) {
        Snackbar.make(mMainView, "Die roll: $roll", Snackbar.LENGTH_LONG).show()
    }
}
