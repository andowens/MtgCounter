package com.firerocks.mtgcounter.bluetooth.model

import android.bluetooth.BluetoothDevice
import android.util.Log
import com.firerocks.mtgcounter.bluetooth.BluetoothMVP
import com.firerocks.mtgcounter.counter.model.Player
import com.firerocks.mtgcounter.counter.model.isDead
import com.firerocks.mtgcounter.helpers.Operator
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class BluetoothModel @Inject constructor(private val mPlayer: Player):
        BluetoothMVP.Model, Observable() {

    private val TAG = "mtg.BluetoothModel"

    companion object {
        // Message types sent from the BluetoothChatService Handler
        const val MESSAGE_STATE_CHANGE = 1
        const val MESSAGE_READ = 2
        const val MESSAGE_WRITE = 3
        const val MESSAGE_CONNECTED = 4
        const val MESSAGE_ERROR = 5
        const val PLAYER_DEAD = 6
        const val UPDATE_OPPONENT = 8
        const val START_NEW_GAME = 9
        const val ROLL_DIE = 10

        // Error types
        const val CONNECTION_FAILED = 0
        const val CONNECTION_LOST = 1
    }

    private lateinit var mObserver: Observer
    private lateinit var mBlueToothHelper: BlueToothHelper
    private lateinit var mEnemyPlayer: Player

    private val GET_ENEMY = "get.enemy.player"
    private val NEW_GAME = "new.game"

    private val mBluetoothObserver = object : io.reactivex.Observer<Pair<Int, Any>> {

        override fun onNext(t: Pair<Int, Any>) {
            val msg = t.first

            when (msg) {
                MESSAGE_STATE_CHANGE -> {

                }
                MESSAGE_CONNECTED -> {
                    mBlueToothHelper.write(GET_ENEMY.toByteArray())
                    notifyObservers(t)
                }
                MESSAGE_ERROR -> {
                    notifyObservers(t)
                }
                MESSAGE_READ -> {
                    val second = t.second as String
                    Log.i(TAG, second)
                    val gson = Gson()
                    Log.e(TAG, "MESSAGE_READ: $second")
                    try {
                        mEnemyPlayer = gson.fromJson(second, Player::class.java)

                        val tmpPair = Pair(UPDATE_OPPONENT, mEnemyPlayer)
                        notifyObservers(tmpPair)
                    } catch (e: Exception) {
                        Log.i(TAG, "Error converting json to class")
                    }

                    if (second == GET_ENEMY) {
                        val playerJson = gson.toJson(mPlayer)
                        mBlueToothHelper.write(playerJson.toByteArray())
                    } else if (second == NEW_GAME) {
                        notifyObservers(Pair(START_NEW_GAME, ""))
                    } else if (second.split(" ")[0] == "$ROLL_DIE") {
                        Log.e(TAG, "Second: $second")
                        if (second.split(" ").size == 2) {
                            notifyObservers(Pair(ROLL_DIE, second.split(" ")[1]))
                        }
                    }
                }
            }
        }

        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onError(e: Throwable) {
        }

    }

    init {
        mBlueToothHelper = BlueToothHelper(mBluetoothObserver)
    }

    override fun addObserver(observer: Observer) {
        mObserver = observer
    }

    override fun sendNewGame() {
        mBlueToothHelper.write(NEW_GAME.toByteArray())
    }

    private fun notifyObservers(data: Pair<Int, Any>) {
        mObserver.update(this, data)
    }

    override fun getPlayersHealth(): Int {
        return mPlayer.health
    }

    override fun getPlayersName(): String {
        return mPlayer.name
    }

    override fun setStartPlayerHealth(health: Int) {
        mPlayer.health = health
    }

    override fun setStartPlayerName(name: String) {
        mPlayer.name = name
    }

    override fun changeNameNotifyOpponent(name: String) {
        mPlayer.name = name

        val gson = Gson()
        val playerJson = gson.toJson(mPlayer)
        mBlueToothHelper.write(playerJson.toByteArray())
    }

    override fun sendDieRollResult(roll: String) {
        mBlueToothHelper.write("$ROLL_DIE $roll".toByteArray())
    }

    override fun connectDevice(device: BluetoothDevice) {
        mBlueToothHelper.connect(device)
    }

    override fun getServiceState(): Int {
        return mBlueToothHelper.getState()
    }

    override fun startService() {
        mBlueToothHelper.start()
    }

    override fun stopService() {
        mBlueToothHelper.stop()
    }

    override fun updatePlayerHealth(health: Int, operatorType: Operator): Int {
        var add = 1

        if (operatorType == Operator.SUBTRACT) {
            add = -1
        }

        mPlayer.health += add

        if (mPlayer.isDead()) {
            notifyObservers(Pair(PLAYER_DEAD, ""))
        }

        val gson = Gson()
        val playerJson = gson.toJson(mPlayer)
        Log.i(TAG, playerJson.toString())
        mBlueToothHelper.write(playerJson.toByteArray())

        return mPlayer.health
    }
}