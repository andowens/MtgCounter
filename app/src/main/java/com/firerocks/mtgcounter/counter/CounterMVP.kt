package com.firerocks.mtgcounter.counter

import com.firerocks.mtgcounter.helpers.GameType
import com.firerocks.mtgcounter.helpers.Operator
import com.firerocks.mtgcounter.helpers.PlayerID

interface CounterMVP {
    interface Presenter {

        /**
         * Function used to set the vew in the presenter.
         *
         * @param counterView The counter view
         */
        fun setView(counterView: CounterMVP.View)

        /**
         * Adds a player to the the model
         */
        fun addPlayer()

        /**
         * Removes a player from the model
         */
        fun removePlayer()

        /**
         * Resets all of the player's Health
         *
         * @param onResult Lambda used in the presenter to update thu UI after the model is
         *                 updated.
         */
        fun resetAllPlayersHealth(onResult: (Int, Int) -> Unit)

        /**
         * Resets a single player's health
         *
         * @param playerID The id of the player who's health needs to be reset.
         * @param onResult Lambda used to update the UI after the model is updated.
         */
        fun resetPlayerHealth(playerID: PlayerID, onResult: (PlayerID, Int) -> Unit)

        /**
         * Updates a single players health either up or down depending on the operator that is passed
         * in.
         *
         * @param playerID The id of the player to update.
         * @param operator The operator to use either plus or minus.
         * @param onResult Lambda used to update the UI once the model is updated.
         */
        fun updatePlayerHealth(playerID: PlayerID, operator: Operator, onResult: (PlayerID, Int) -> Unit)

        /**
         * Updates a players name.
         *
         * @param playerID The id of the player to update.
         * @param name The new name of the player.
         * @param onResult Lambda used to updated the UI once the model is updated
         */
        fun updatePlayerName(playerID: PlayerID, name: String, onResult: (String) -> Unit)

        /**
         * Sets up a three player game
         */
        fun threePlayerGame()

        /**
         * Sets up a four player game
         */
        fun fourPlayerGame()

        /**
         * Sets up a two player game
         */
        fun twoPlayerGame()

        /**
         * sSts up two headed giant game
         */
        fun twoHeadedGiantGame()

        /**
         * Handles when the roll die is clicked. Calculated then the roll die option is
         * clicked in the menu
         */
        fun rollDieClicked()

        /**
         * Gets the current game type.
         *
         * @return Returns the current gametype
         */
        fun getGameType() : GameType
    }

    interface View {

        /**
         * Since android is fun and we can't get strings or ints that are stored in
         * resource files if we want to keep android things out of the presenter,
         * so we have to have the view get it.
         *
         * @param gameType The game type that is currently set
         */
        fun getDefaultHealth(gameType: GameType) : Int

        /**
         * Uses the string resource file to get the default player name
         */
        fun getPlayerDefaultName(playerNum: Int): String

        /**
         * If a player dies then we inform the user with a snackbar
         */
        fun launchPlayerDeadSnackBar(deadPlayer: String)

        /**
         * Sets up a three player game.
         */
        fun threePlayerGame()

        /**
         * Sets up four player game.
         */
        fun fourPlayerGame()

        /**
         * Sets up two player game.
         */
        fun twoPlayerGame()

        /**
         * Sets up a two headed giant game
         */
        fun twoHeadedGiantGame(health: Int)

        /**
         * Rolls a die and displays the result to the user
         */
        fun showDieRolled(roll: String)
    }
}