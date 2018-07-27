package com.firerocks.mtgcounter.bluetooth

import io.reactivex.disposables.Disposable
import java.util.*

class BluetoothModel: BluetoothMVP.Model, Observable() {

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
}