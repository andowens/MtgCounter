package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import com.firerocks.mtgcounter.data.Player
import com.firerocks.mtgcounter.data.isDead
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
        const val MESSAGE_SNACKBAR = 5
        const val PLAYER_DEAD = 6
        const val UPDATE_OPPONENT_HEALTH = 7
        const val UPDATE_OPPONENT_NAME = 8
        const val START_NEW_GAME = 9
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
                    //mBlueToothHelper.write(GET_ENEMY.toByteArray())
                }
                MESSAGE_SNACKBAR -> {
                    notifyObservers(t)
                }
                MESSAGE_READ -> {
                    val second = t.second as String
                    val gson = Gson()
                    Log.i(TAG, "MESSAGE_READ: $second")
                    try {
                        mEnemyPlayer = gson.fromJson(second, Player::class.java)

                        var tmp = Pair(UPDATE_OPPONENT_HEALTH, mEnemyPlayer.health.toString())
                        notifyObservers(tmp)

                        tmp = Pair(UPDATE_OPPONENT_NAME, mEnemyPlayer.name)
                        notifyObservers(tmp)
                    } catch (e: Exception) {
                        Log.i(TAG, "Error converting json to class")
                    }

                    if (second == GET_ENEMY) {
                        val playerJson = gson.toJson(mPlayer)
                        mBlueToothHelper.write(playerJson.toByteArray())
                    } else if (second == NEW_GAME) {
                        notifyObservers(Pair(START_NEW_GAME, ""))
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