package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.util.Log
import com.firerocks.mtgcounter.data.Player
import com.firerocks.mtgcounter.helpers.Operator
import java.util.*
import javax.inject.Inject

class BluetoothPresenter @Inject constructor(private val mModel: BluetoothMVP.Model,
                                             var mBluetoothAdapter: BluetoothAdapter?):
        BluetoothMVP.Presenter, Observer {

    override fun bluetoothDeviceSelected(address: String) {
        mBluetoothAdapter?.getRemoteDevice(address)?.let { device ->
            mModel.connectDevice(device)
        }
    }

    override fun checkBluetoothEnabled(onResult: (Boolean) -> Unit) {
        var bluetooth = false
        mBluetoothAdapter?.let { adapter ->
            if (adapter.isEnabled) {
                bluetooth = true
            }
        }
        onResult(bluetooth)
    }

    private val TAG = "mtg.BluetoothPresenter"

    init {
        mModel.addObserver(this)
    }

    override fun onResume() {
         if (mModel.getServiceState() == BlueToothHelper.STATE_NONE) {
             mModel.startService()
         }
    }

    override fun onPause() {
        Log.i(TAG, "Stop service")
        mModel.stopService()
    }

    override fun menuNewGame() {
        mModel.sendNewGame()
        mModel.setStartPlayerHealth(mView.getDefaultHealth())
        mView.setPlayerHealth(mView.getDefaultHealth().toString())
    }

    override fun upClicked(health: String, onResult: (String) -> Unit) {
        val newHealth = mModel.updatePlayerHealth(health.toInt(), Operator.ADD)
        onResult(newHealth.toString())
    }

    override fun downClicked(health: String, onResult: (String) -> Unit) {
        val newHealth = mModel.updatePlayerHealth(health.toInt(), Operator.SUBTRACT)
        onResult(newHealth.toString())
    }

    override fun nameClicked(name: String, onResult: (String) -> Unit) {
        mModel.changeNameNotifyOpponent(name)
        onResult(name)
    }

    override fun update(o: Observable?, arg: Any?) {
        val data = arg as Pair<*, *>
        when (data.first) {
            BluetoothModel.PLAYER_DEAD -> {
                mView.launchPlayerDeadSnackBar(mModel.getPlayersName())
            }
            BluetoothModel.MESSAGE_ERROR -> {

                when (data.second) {
                    BluetoothModel.CONNECTION_FAILED -> {
                        mView.errorSnackbar("Failed connecting to device")
                    }
                    BluetoothModel.CONNECTION_LOST -> {
                        mView.errorSnackbar("Device disconnected")
                    }

                }
            }
            BluetoothModel.UPDATE_OPPONENT -> {
                val player = data.second as? Player
                player?.let {
                    mView.updateOpponent(it)
                }
            }
            BluetoothModel.START_NEW_GAME -> {
                mModel.setStartPlayerHealth(mView.getDefaultHealth())
                mView.setPlayerHealth(mModel.getPlayersHealth().toString())
            }
            BluetoothModel.MESSAGE_CONNECTED -> {
                mView.dismissNoDeviceConnectedSnackBar()
            }
            BluetoothModel.ROLL_DIE -> {
                mView.showDieRollResult(data.second as String)
            }
        }
    }

    private lateinit var mView: BluetoothMVP.View

    override fun setView(view: BluetoothMVP.View, onResult: (String, Int) -> Unit) {
        mView = view
        mModel.setStartPlayerName(mView.getRandomPlayerName())
        mModel.setStartPlayerHealth(mView.getDefaultHealth())
        // If no bluetooth on the device inform the user and go back to the single phone activity
        if (mBluetoothAdapter == null) {
            mView.showNoBluetoothDialog()
        } else {

            mView.showNoDeviceConnectedSnackBar()
        }

        onResult(mModel.getPlayersName(), mModel.getPlayersHealth())
    }

    override fun dieRollClicked() {
        val roll = (Random().nextInt(20 - 1) + 1).toString()
        mView.showDieRollResult(roll)
        mModel.sendDieRollResult(roll)
    }
}