package com.firerocks.mtgcounter.counter

interface CounterMVP {
    interface Presenter {
        fun setView(counterView: CounterMVP.View)

        fun addPlayer(onResult: (Player) -> Unit)

        fun removePlayer()

        fun resetPlayerHealth(onResult: (Int) -> Unit)

        fun updatePlayerHealth(player: Int, update: String, onResult: (Int) -> Unit)

        fun updatePlayerName(player: Int, name: String, onResult: (String) -> Unit)

        fun threePlayerGame()

        fun fourPlayerGame()

        fun twoHeadedGiantGame()
    }

    interface View {
        fun getDefaultHealth() : Int

        fun getPlayerDefaultName(playerNum: Int): String

        fun launchPlayerDeadSnackBar(deadPlayer: String)
    }
}