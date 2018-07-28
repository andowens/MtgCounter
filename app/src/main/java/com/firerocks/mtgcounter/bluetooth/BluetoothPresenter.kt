package com.firerocks.mtgcounter.bluetooth

import android.util.Log
import java.util.*

class BluetoothPresenter constructor(private val mModel: BluetoothMVP.Model): BluetoothMVP.Presenter, Observer {

    private val TAG = "mtg.BluetoothPresenter"

    init {
        mModel.addObserver(this)
    }

    override fun menuNewGame(onResult: (Int) -> Unit) {

    }

    override fun discoverBluetooth() {

    }

    override fun upClicked() {

    }

    override fun downClicked() {
    }

    override fun nameClicked(name: String, onResult: (String) -> Unit) {
        mModel.changeNameNotifyOpponent(name)
        onResult(name)
    }

    override fun update(o: Observable?, arg: Any?) {
    }

    private lateinit var mView: BluetoothMVP.View

    override fun setView(view: BluetoothMVP.View, onResult: (String, Int) -> Unit) {
        mView = view
        mModel.setStartPlayerName(mView.getRandomPlayerName())
        mModel.setStartPlayerHealth(mView.getDefaultHealth())
        onResult(mModel.getPlayersName(), mModel.getPlayersHealth())
    }
}