package com.firerocks.mtgcounter.bluetooth

class BluetoothPresenter: BluetoothMVP.Presenter {

    private lateinit var mView: BluetoothMVP.View

    override fun setView(view: BluetoothMVP.View) {
        mView = view
    }
}