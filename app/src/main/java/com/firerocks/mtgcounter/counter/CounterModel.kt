package com.firerocks.mtgcounter.counter

import android.content.Context
import android.util.Log
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.data.Player
import javax.inject.Inject

class CounterModel: CounterMVP.Model {

    private lateinit var playerList: MutableList<Player>
    @Inject lateinit var mContext: Context

    init {
        playerList.add(Player(mContext.getString(R.string.player_one_default_name),
                mContext.resources.getInteger(R.integer.default_player_health)))

        playerList.add(Player(mContext.getString(R.string.player_two_default_name),
                mContext.resources.getInteger(R.integer.default_player_health)))
    }

    override fun addPlayer() {
        when (playerList.size) {
            2 -> playerList
                    .add(Player(mContext.getString(R.string.player_three_default_name),
                            mContext.resources.getInteger(R.integer.default_player_health)))

            3 -> playerList
                    .add(Player(mContext.getString(R.string.player_four_default_name),
                            mContext.resources.getInteger(R.integer.default_player_health)))

        }
    }

    override fun removePlayer() {
        if (playerList.size > 2) {
            playerList.removeAt(playerList.size - 1)
        }
    }

    override fun resetPlayerHealth() {
        for (player in playerList) {
            player.health = mContext.resources.getInteger(R.integer.default_player_health)
        }
    }

    override fun updatePlayerHealth(player: Int, update: String) {
        val playerObj = playerList[player]

        when (update) {
            "plus" -> playerObj.health = playerObj.health + 1
            "minus" -> playerObj.health = playerObj.health - 1
        }
    }

    override fun getPlayerHealth(player: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

