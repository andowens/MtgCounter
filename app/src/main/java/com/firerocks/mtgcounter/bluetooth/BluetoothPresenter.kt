package com.firerocks.mtgcounter.bluetooth

import java.util.*

class BluetoothPresenter constructor(private val mModel: BluetoothMVP.Model): BluetoothMVP.Presenter, Observer {

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