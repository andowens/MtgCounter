package com.firerocks.mtgcounter.bluetooth

import java.util.*

class BluetoothModel: BluetoothMVP.Model, Observable() {

    private lateinit var mObserver: Observer

    private val mBluetoothObserver = io.reactivex.Observer<>()

    override fun addObserver(observer: Observer) {
        mObserver = observer
    }


    private fun notifyObservers(type: String) {
        mObserver.update(this, type)
    }
}