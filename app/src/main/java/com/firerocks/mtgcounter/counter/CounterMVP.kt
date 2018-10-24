package com.firerocks.mtgcounter.counter

import com.firerocks.mtgcounter.helpers.GameType
import com.firerocks.mtgcounter.helpers.Operator
import com.firerocks.mtgcounter.helpers.PlayerID

interface CounterMVP {
    interface Presenter {
        fun setView(counterView: CounterMVP.View)

        fun addPlayer()

        fun removePlayer()

        fun resetAllPlayersHealth(onResult: (Int, Int) -> Unit)

        fun resetPlayerHealth(playerID: PlayerID, onResult: (PlayerID, Int) -> Unit)

        fun updatePlayerHealth(playerID: PlayerID, operator: Operator, onResult: (PlayerID, Int) -> Unit)

        fun updatePlayerName(playerID: PlayerID, name: String, onResult: (String) -> Unit)

        fun threePlayerGame()

        fun fourPlayerGame()

        fun twoPlayerGame()

        fun twoHeadedGiantGame()

        fun rollDieClicked()
    }

    interface View {
        fun getDefaultHealth(gameType: GameType) : Int

        fun getPlayerDefaultName(playerNum: Int): String

        fun launchPlayerDeadSnackBar(deadPlayer: String)

        fun threePlayerGame()

        fun fourPlayerGame()

        fun twoPlayerGame()

        fun twoHeadedGiantGame(health: Int)

        fun showDieRolled(roll: String)
    }
}