package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.util.Log
import java.util.*
import javax.inject.Inject

class BluetoothPresenter @Inject constructor(private val mModel: BluetoothMVP.Model,
                                             var mBluetoothAdapter: BluetoothAdapter?):
        BluetoothMVP.Presenter, Observer {

    override fun bluetoothDeviceSelected(address: String) {
        val device = mBluetoothAdapter?.getRemoteDevice(address)
        mModel.connectDevice(device!!)
    }

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

        // If no bluetooth on the device inform the user and go back to the single phone activity
        if (mBluetoothAdapter == null) {
            mView.showNoBluetoothDialog()
        } else {
            // If bluetooth is off request to turn it on
            if (!mBluetoothAdapter?.isEnabled!!) {
                mView.requestBluetoothOn()
            }
        }
    }
}