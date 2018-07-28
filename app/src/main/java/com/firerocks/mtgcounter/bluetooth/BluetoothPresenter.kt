package com.firerocks.mtgcounter.bluetooth

import java.util.*

class BluetoothPresenter constructor(private val mModel: BluetoothMVP.Model): BluetoothMVP.Presenter, Observer {
    override fun menuNewGame(onResult: (Int) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun discoverBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun upClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nameClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        mModel.addObserver(this)
    }

    override fun update(o: Observable?, arg: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mView: BluetoothMVP.View

    override fun setView(view: BluetoothMVP.View) {
        mView = view
    }
}