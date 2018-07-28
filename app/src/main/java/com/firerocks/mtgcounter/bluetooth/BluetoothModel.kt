package com.firerocks.mtgcounter.bluetooth

import com.firerocks.mtgcounter.data.Player
import io.reactivex.disposables.Disposable
import java.util.*

class BluetoothModel(private val mPlayer: Player): BluetoothMVP.Model, Observable() {

    companion object {
        // Message types sent from the BluetoothChatService Handler
        const val MESSAGE_STATE_CHANGE = 1
        const val MESSAGE_READ = 2
        const val MESSAGE_WRITE = 3
        const val MESSAGE_DEVICE_NAME = 4
        const val MESSAGE_TOAST = 5
        const val TOAST = 6
    }

    private lateinit var mObserver: Observer

    private val mBluetoothObserver = object : io.reactivex.Observer<Pair<Int, Any>> {

        override fun onNext(t: Pair<Int, Any>) {
        }

        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onError(e: Throwable) {
        }

    }

    override fun addObserver(observer: Observer) {
        mObserver = observer
    }


    private fun notifyObservers(type: String) {
        mObserver.update(this, type)
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

    }
}