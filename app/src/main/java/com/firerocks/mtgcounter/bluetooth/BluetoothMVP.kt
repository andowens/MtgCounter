package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothDevice
import java.util.*

interface BluetoothMVP {

    interface Presenter{
        fun setView(view: BluetoothMVP.View, onResult: (String, Int) -> Unit)

        fun menuNewGame(onResult: (Int) -> Unit)

        fun discoverBluetooth()

        fun upClicked()

        fun downClicked()

        fun nameClicked(name: String, onResult: (String) -> Unit)

        fun bluetoothDeviceSelected(address: String)
    }

    interface View {
        fun getDefaultHealth(): Int

        fun getRandomPlayerName(): String

        fun showNoBluetoothDialog()

        fun requestBluetoothOn()
    }

    interface Model {
        fun addObserver(observer: Observer)

        fun getPlayersName(): String

        fun getPlayersHealth(): Int

        fun setStartPlayerHealth(health: Int)

        fun setStartPlayerName(name: String)

        fun changeNameNotifyOpponent(name: String)

        fun connectDevice(device: BluetoothDevice)
    }
}