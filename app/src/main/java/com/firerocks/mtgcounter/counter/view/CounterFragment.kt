package com.firerocks.mtgcounter.counter.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterMVP
import com.firerocks.mtgcounter.helpers.*
import com.firerocks.mtgcounter.views.CustomFontTextView
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CounterFragment : DaggerFragment(), CounterMVP.View {

    private val TAG = "CounterFragment"

    @Inject lateinit var presenter: CounterMVP.Presenter
    private lateinit var mMainView: ConstraintLayout

    companion object {
        fun newInstance() : CounterFragment = CounterFragment()
    }

    private val mPlayerLifeIDs = listOf(R.id.player_one_health,
            R.id.player_two_health,
            R.id.three_player_one_health,
            R.id.three_player_two_health,
            R.id.three_player_three_health,
            R.id.four_player_one_health,
            R.id.four_player_two_health,
            R.id.four_player_three_health,
            R.id.four_player_four_health)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the vew
        val view = inflater.inflate(R.layout.counter_view, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTwoPlayerGame()
    }

    /**
     * Helper function used to set all the main views that are part of this fragment invisible
     */
    private fun hideAllViews() {
        view?.let {
            it.findViewById<View>(R.id.two_main).visibility = View.GONE
            it.findViewById<View>(R.id.three_main).visibility = View.GONE
            it.findViewById<View>(R.id.four_main).visibility = View.GONE
        }
    }

    /**
     * Long helper function used to setup the two player game sets all the callbacks for
     * all the views and sets the two player view to visible. (Could maybe be done better
     * some how... but I'm not sure how maybe by iterating through the list of views or something)
     */
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

    /**
     * Long helper function used to setup the three player game sets all the callbacks for
     * all the views and sets the three player view to visible. (Could maybe be done better
     * some how... but I'm not sure how maybe by iterating through the list of views or something)
     */
    private fun setupThreePlayerGame() {
        activity?.let { act ->

            mMainView = act.findViewById(R.id.three_main)
            mMainView.visibility = View.VISIBLE

            mMainView.findViewById<AppCompatImageButton>(R.id.three_player_one_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.three_player_one_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.three_player_two_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.three_player_two_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.three_player_three_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.three_player_three_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.three_player_one_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.three_player_two_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.three_player_three_name).setOnClickListener {
                changeName(it)
            }
        }
    }

    /**
     * Long helper function used to setup the Four player game sets all the callbacks for
     * all the views and sets the four player view to visible. (Could maybe be done better
     * some how... but I'm not sure how maybe by iterating through the list of views or something)
     */
    private fun setupFourPlayerGame() {
        activity?.let { act ->

            mMainView = act.findViewById(R.id.four_main)
            mMainView.visibility = View.VISIBLE

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_one_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_one_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_two_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_two_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_three_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_three_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_four_down_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<AppCompatImageButton>(R.id.four_player_four_up_arrow).setOnClickListener {
                updatePlayerHealth(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.four_player_one_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.four_player_two_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.four_player_three_name).setOnClickListener {
                changeName(it)
            }

            mMainView.findViewById<CustomFontTextView>(R.id.four_player_four_name).setOnClickListener {
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

    /**
     * Callback used when a players name is clicked so that the user can change their name.
     *
     * @param view The view that is clicked.
     */
    private fun changeName(view: View) {

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

    /**
     * Callback for the life up and life down arrows. This could probably be handled in a more
     * elegant way but I as of yet haven't thought of anything.
     *
     * @param view The view that was clicked in this case an up or down arrow.
     */
    private fun updatePlayerHealth(view: View) {

        // Since the arrow was clicked animate it
        appContext { context ->
            animateView(context, view, R.animator.chevron_animation)
        }

        // We need to get the tag and then use it to figure out which arrow corresponds to which
        // life total.
        val tag = view.tag.toString()
        val splitTag = tag.split("_")
        val playerId: PlayerID = PlayerID.valueOf(splitTag[0])
        val operator: Operator = Operator.valueOf(splitTag[1])

        // Since this is "MVP lite" we need to tell the presenter that the button was clicked
        // so it can update it's model and then take actions accordingly.
        presenter.updatePlayerHealth(playerId, operator) { playerID, health ->
            var healthView: CustomFontTextView? = null
            val gameType = presenter.getGameType()
            if (gameType == GameType.FOUR_PLAYER) {
                when (playerID) {
                    PlayerID.ONE -> {
                        healthView = activity?.findViewById(R.id.four_player_one_health)
                        healthView?.text = health.toString()
                    }
                    PlayerID.TWO -> {
                        healthView = activity?.findViewById(R.id.four_player_two_health)
                        healthView?.text = health.toString()
                    }
                    PlayerID.THREE -> {
                        healthView = activity?.findViewById(R.id.four_player_three_health)
                        healthView?.text = health.toString()
                    }
                    PlayerID.FOUR -> {
                        healthView = activity?.findViewById(R.id.four_player_four_health)
                        healthView?.text = health.toString()
                    }
                }
            } else if (gameType == GameType.TWO_PLAYER || gameType == GameType.TWO_HEADED_GIANT) {
                when (playerID) {
                    PlayerID.ONE -> {
                        healthView = activity?.findViewById(R.id.player_one_health)
                        healthView?.text = health.toString()
                    }
                    PlayerID.TWO -> {
                        healthView = activity?.findViewById(R.id.player_two_health)
                        healthView?.text = health.toString()
                    }
                }
            } else if (gameType == GameType.THREE_PLAYER) {
                when (playerID) {
                    PlayerID.ONE -> {
                        healthView = activity?.findViewById(R.id.three_player_one_health)
                        healthView?.text = health.toString()
                    }
                    PlayerID.TWO -> {
                        healthView = activity?.findViewById(R.id.three_player_two_health)
                        healthView?.text = health.toString()
                    }
                    PlayerID.THREE -> {
                        healthView = activity?.findViewById(R.id.three_player_three_health)
                        healthView?.text = health.toString()
                    }
                }
            }

            // Now that the life is updated animate it.
            appContext { context ->
                healthView?.let {
                    animateView(context, healthView, R.animator.health_animation)
                }
            }
        }
    }

    /**
     * Helper function that is used to check if the app context is null and if it isn't then
     * call the lambda function and pass it in.
     *
     * @param lambda Function that uses the non-null app context.
     */
    private fun appContext (lambda: (Context) -> Unit) {
        activity?.applicationContext?.let {
            lambda(it)
        }
    }

    override fun getDefaultHealth(gameType: GameType) : Int {
        return if (gameType == GameType.TWO_HEADED_GIANT) {
            resources.getInteger(R.integer.two_headed_giant)
        } else {
            resources.getInteger(R.integer.default_player_health)
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
                         resetAllPlayersHealth(health)
                    }
                }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            R.id.menu_new_game -> {
                presenter.resetAllPlayersHealth { health, size ->
                    resetAllPlayersHealth(health)
                }
                return true
            }
            R.id.roll_die -> {
                presenter.rollDieClicked()
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
        resetAllPlayersHealth()
        setupThreePlayerGame()
    }

    override fun fourPlayerGame() {
        resetAllPlayersHealth()
        setupFourPlayerGame()
    }

    override fun twoPlayerGame() {
        resetAllPlayersHealth()
        setupTwoPlayerGame()
    }

    /**
     * Used to reset all players health across all views inside of the counter fragments
     * Note: This will reset all views.
     *
     * @param healthValue The value to set all players health to.
     */
    private fun resetAllPlayersHealth(healthValue: Int = 20) {

        if (presenter.getGameType() == GameType.TWO_HEADED_GIANT) {
            activity?.findViewById<CustomFontTextView>(R.id.player_one_health)
                    ?.text = healthValue.toString()
            activity?.findViewById<CustomFontTextView>(R.id.player_two_health)
                    ?.text = healthValue.toString()
        } else {
            for (i in 0..(mPlayerLifeIDs.size - 1)) {
                activity?.findViewById<CustomFontTextView>(mPlayerLifeIDs[i])
                        ?.text = healthValue.toString()
            }
        }
    }

    override fun twoHeadedGiantGame(health: Int) {
        setupTwoPlayerGame()
        resetAllPlayersHealth(health)
    }

    override fun showDieRolled(roll: String) {
        Snackbar.make(mMainView, "Die roll: $roll", Snackbar.LENGTH_LONG).show()
    }
}
