package com.firerocks.mtgcounter.counter

import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.data.Player
import javax.inject.Inject

class CounterPresenter: CounterMVP.Presenter {
    private lateinit var mCounterView: CounterMVP.View
    private var mPlayerList: MutableList<Player> = ArrayList

    override fun setView(counterView: CounterMVP.View) {
        mCounterView = counterView
    }

    override fun updatePlayerHealth(player: Int, update: String, onResult: (Int) -> Int) {
        val playerObj = mPlayerList[player]

        when (update) {
            "plus" -> playerObj.health = playerObj.health + 1
            "minus" -> playerObj.health = playerObj.health - 1
        }

        onResult(playerObj.health)
    }

    override fun updatePlayerName(player: Int, name: String, onResult: (String) -> Unit) {
        mPlayerList[player].name = name
        onResult(name)
    }

    override fun removePlayer() {
        if (mPlayerList.size > 2) {
            mPlayerList.removeAt(mPlayerList.size - 1)
        }
    }

    override fun resetPlayerHealth(onResult: (Int) -> Int) {
        for (player in mPlayerList) {
            player.health = mContext.resources.getInteger(R.integer.default_player_health)
        }
        onResult(mContext.resources.getInteger(R.integer.default_player_health))
    }

    override fun addPlayer(onResult: (Player) -> Unit) {
        when (mPlayerList.size) {
            2 -> {
                mPlayerList
                        .add(Player(mContext.getString(R.string.player_three_default_name),
                                mContext.resources.getInteger(R.integer.default_player_health)))
                onResult(mPlayerList[3])
            }

            3 -> {
                mPlayerList
                        .add(Player(mContext.getString(R.string.player_four_default_name),
                                mContext.resources.getInteger(R.integer.default_player_health)))
                onResult(mPlayerList[4])
            }
        }
    }
}