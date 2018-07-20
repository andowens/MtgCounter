package com.firerocks.mtgcounter.counter
import android.view.View
import com.firerocks.mtgcounter.data.Player

interface CounterMVP {
    interface Presenter {
        fun setView(counterView: CounterMVP.View)

        fun onChangeName(view: String)
    }

    interface View {
        fun changePlayerName(name: String)
    }

    interface Model {
        fun addPlayer(onResult: (Player) -> Unit)

        fun removePlayer()

        fun resetPlayerHealth(onResult: (Int) -> Int)

        fun updatePlayerHealth(player: Int, update: String, onResult: (Int) -> Int)

        fun updatePlayerName(player: Int, name: String, onResult: (String) -> String)
    }
}