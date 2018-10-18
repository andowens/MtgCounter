package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothDevice
import com.firerocks.mtgcounter.helpers.Operator
import java.util.*

interface BluetoothMVP {

    interface Presenter{
        fun setView(view: BluetoothMVP.View, onResult: (String, Int) -> Unit)

        fun menuNewGame()

        fun upClicked(health: String, onResult: (String) -> Unit)

        fun downClicked(health: String, onResult: (String) -> Unit)

        fun nameClicked(name: String, onResult: (String) -> Unit)

        fun bluetoothDeviceSelected(address: String)

        fun onResume()

        fun onPause()

        fun checkBluetoothEnabled(onResult: (Boolean) -> Unit)
    }

    interface View {
        fun getDefaultHealth(): Int

        fun getRandomPlayerName(): String

        fun showNoBluetoothDialog()

        fun requestBluetoothOn()

        fun launchPlayerDeadSnackBar(player: String)

        fun showNoDeviceConnectedSnackBar()

        fun dismissNoDeviceConnectedSnackBar()

        fun errorSnackbar(error: String)

        fun updateOpponentHealth(health: String)

        fun updateOpponentName(name: String)

        fun setPlayerHealth(health: String)
    }

    interface Model {
        fun addObserver(observer: Observer)

        fun getPlayersName(): String

        fun getPlayersHealth(): Int

        fun setStartPlayerHealth(health: Int)

        fun setStartPlayerName(name: String)

        fun changeNameNotifyOpponent(name: String)

        fun connectDevice(device: BluetoothDevice)

        fun getServiceState(): Int

        fun startService()

        fun stopService()

        fun updatePlayerHealth(health: Int, operatorType: Operator): Int

        fun sendNewGame()
    }
}