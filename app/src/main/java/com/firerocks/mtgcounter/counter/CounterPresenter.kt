package com.firerocks.mtgcounter.counter

import android.util.Log
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.helpers.GameType
import com.firerocks.mtgcounter.helpers.Operator
import com.firerocks.mtgcounter.helpers.PlayerID
import javax.inject.Inject

class CounterPresenter: CounterMVP.Presenter {
    private val TAG = "mtg.CounterPresenter"
    private lateinit var mCounterView: CounterMVP.View
    private var mPlayerList: MutableList<Player> = ArrayList()

    private var mGameType = GameType.NORMAL

    override fun setView(counterView: CounterMVP.View) {
        mCounterView = counterView
    }

    override fun updatePlayerHealth(playerID: PlayerID, operator: Operator, onResult: (PlayerID ,Int) -> Unit) {
        var add = 1

        if (operator == Operator.SUBTRACT) {
            add = -1
        }


        val player : Player = when (playerID) {
            PlayerID.ONE -> {
                mPlayerList[0].health += add
                mPlayerList[0]
            }
            PlayerID.TWO -> {
                mPlayerList[1].health += add
                mPlayerList[1]
            }
            PlayerID.THREE -> {
                mPlayerList[2].health += add
                mPlayerList[2]
            }
            PlayerID.FOUR -> {
                mPlayerList[3].health += add
                mPlayerList[3]
            }
        }

        if (player.isDead()) {
            mCounterView.launchPlayerDeadSnackBar(player.name)
        }

        onResult(playerID, player.health)
    }

    override fun updatePlayerName(playerID: PlayerID, name: String, onResult: (String) -> Unit) {
        when (playerID) {
            PlayerID.ONE -> mPlayerList[0].name = name
            PlayerID.TWO -> mPlayerList[1].name = name
            PlayerID.THREE -> mPlayerList[2].name = name
            PlayerID.FOUR -> mPlayerList[3].name = name
        }
        onResult(name)
    }

    override fun removePlayer() {
        if (mPlayerList.size > 2) {
            mPlayerList.removeAt(mPlayerList.size - 1)
        }
    }

    override fun resetAllPlayersHealth(onResult: (Int, Int) -> Unit) {
        for (player in mPlayerList) {
            player.health = mCounterView.getDefaultHealth(mGameType)
        }
        onResult(mCounterView.getDefaultHealth(mGameType), mPlayerList.size)
    }

    override fun resetPlayerHealth(playerID: PlayerID, onResult: (PlayerID, Int) -> Unit) {

        val health = mCounterView.getDefaultHealth(mGameType)

        when (playerID) {
            PlayerID.ONE -> {
                mPlayerList[0].health = health
            }
            PlayerID.TWO -> {
                mPlayerList[1].health = health
            }
            PlayerID.THREE -> {
                mPlayerList[2].health = health
            }
            PlayerID.FOUR -> {
                mPlayerList[3].health = health
            }
        }

        onResult(playerID, health)
    }

    override fun addPlayer() {
        when (mPlayerList.size) {
            0 -> {
                mPlayerList.add(Player(mCounterView.getPlayerDefaultName(1),
                        mCounterView.getDefaultHealth(mGameType)))
            }
            1 -> {
                mPlayerList.add(Player(mCounterView.getPlayerDefaultName(2),
                        mCounterView.getDefaultHealth(mGameType)))
            }
            2 -> {
                mPlayerList
                        .add(Player(mCounterView.getPlayerDefaultName(3),
                                mCounterView.getDefaultHealth(mGameType)))
            }
            3 -> {
                mPlayerList
                        .add(Player(mCounterView.getPlayerDefaultName(4),
                                mCounterView.getDefaultHealth(mGameType)))
            }
        }
    }

    override fun threePlayerGame() {
        mGameType = GameType.NORMAL

        mPlayerList.clear()
        addPlayer()
        addPlayer()
        addPlayer()
        mCounterView.threePlayerGame()
    }

    override fun fourPlayerGame() {
        mGameType = GameType.NORMAL
        mPlayerList.clear()
        addPlayer()
        addPlayer()
        addPlayer()
        addPlayer()
        mCounterView.fourPlayerGame()
    }

    override fun twoPlayerGame() {
        mGameType = GameType.NORMAL
        mPlayerList.clear()
        addPlayer()
        addPlayer()
        mCounterView.twoPlayerGame()
    }

    override fun twoHeadedGiantGame() {
        mGameType = GameType.TWO_HEADED_GIANT
        mPlayerList.clear()
        addPlayer()
        addPlayer()
        mCounterView.twoHeadedGiantGame(mPlayerList[0].health)
    }
}