package com.firerocks.mtgcounter.counter

import android.view.View
import com.firerocks.mtgcounter.R
import javax.inject.Inject

class CounterPresenter: CounterMVP.Presenter {
    private lateinit var mCounterView: CounterMVP.View
    private var mPlayerList: MutableList<Player> = ArrayList()

    override fun setView(counterView: CounterMVP.View) {
        mCounterView = counterView
    }

    override fun updatePlayerHealth(player: Int, update: String, onResult: (Int) -> Unit) {
        val playerObj = mPlayerList[player - 1]

        when (update) {
            "plus" -> playerObj.health = playerObj.health + 1
            "minus" -> {
                playerObj.health = playerObj.health - 1

                if (playerObj.isDead()) {
                    mCounterView.launchPlayerDeadSnackBar(playerObj.name)
                }
            }
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

    override fun resetPlayerHealth(onResult: (Int) -> Unit) {
        for (player in mPlayerList) {
            player.health = mCounterView.getDefaultHealth()
        }
        onResult(mCounterView.getDefaultHealth())
    }

    override fun addPlayer(onResult: (Player) -> Unit) {
        when (mPlayerList.size) {
            0 -> {
                mPlayerList.add(Player(mCounterView.getPlayerDefaultName(1),
                        mCounterView.getDefaultHealth()))
                onResult(mPlayerList[0])
            }
            1 -> {
                mPlayerList.add(Player(mCounterView.getPlayerDefaultName(2),
                        mCounterView.getDefaultHealth()))
                onResult(mPlayerList[1])
            }
            2 -> {
                mPlayerList
                        .add(Player(mCounterView.getPlayerDefaultName(3),
                                mCounterView.getDefaultHealth()))
                onResult(mPlayerList[2])
            }
            3 -> {
                mPlayerList
                        .add(Player(mCounterView.getPlayerDefaultName(4),
                                mCounterView.getDefaultHealth()))
                onResult(mPlayerList[3])
            }
        }
    }

    override fun threePlayerGame() {

    }

    override fun fourPlayerGame() {

    }

    override fun twoHeadedGiantGame() {

    }
}