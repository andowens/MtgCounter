package com.firerocks.mtgcounter.counter

import android.content.Context
import android.util.Log
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.data.Player
import javax.inject.Inject

class CounterModel @Inject constructor(private var mContext: Context): CounterMVP.Model {

    private var playerList: MutableList<Player> = ArrayList()


    init {
        playerList.add(Player(mContext.getString(R.string.player_one_default_name),
                mContext.resources.getInteger(R.integer.default_player_health)))

        playerList.add(Player(mContext.getString(R.string.player_two_default_name),
                mContext.resources.getInteger(R.integer.default_player_health)))
    }

    override fun addPlayer(onResult: (Player) -> Unit) {
        when (playerList.size) {
            2 -> {
                playerList
                        .add(Player(mContext.getString(R.string.player_three_default_name),
                                mContext.resources.getInteger(R.integer.default_player_health)))
                onResult(playerList[3])
            }

            3 -> {
                playerList
                        .add(Player(mContext.getString(R.string.player_four_default_name),
                                mContext.resources.getInteger(R.integer.default_player_health)))
                onResult(playerList[4])
            }
        }
    }

    override fun removePlayer() {
        if (playerList.size > 2) {
            playerList.removeAt(playerList.size - 1)
        }
    }

    override fun resetPlayerHealth(onResult: (Int) -> Int) {
        for (player in playerList) {
            player.health = mContext.resources.getInteger(R.integer.default_player_health)
        }
        onResult(mContext.resources.getInteger(R.integer.default_player_health))
    }

    override fun updatePlayerHealth(player: Int, update: String, onResult: (Int) -> Int) {
        val playerObj = playerList[player]

        when (update) {
            "plus" -> playerObj.health = playerObj.health + 1
            "minus" -> playerObj.health = playerObj.health - 1
        }

        onResult(playerObj.health)
    }

    override fun updatePlayerName(player: Int, name: String, onResult: (String) -> String) {
        playerList[player].name = name
        onResult(name)
    }
}

