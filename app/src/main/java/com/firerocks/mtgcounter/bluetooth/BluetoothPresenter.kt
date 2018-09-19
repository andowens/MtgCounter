package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.util.Log
import com.firerocks.mtgcounter.helpers.Operator
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

    override fun onResume() {
         if (mModel.getServiceState() == BlueToothHelper.STATE_NONE) {
            mModel.startService()
         }
    }

    override fun onPause() {
        mModel.stopService()
    }

    override fun menuNewGame() {
        mModel.sendNewGame()
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
            BluetoothModel.MESSAGE_SNACKBAR -> {
                mView.errorSnackbar(data.second as String)
            }
            BluetoothModel.UPDATE_OPPONENT_HEALTH -> {
                Log.i(TAG, "update opponent health")
                mView.updateOpponentHealth(data.second as String)
            }
            BluetoothModel.UPDATE_OPPONENT_NAME -> {
                Log.i(TAG, "Update opponent name")
                mView.updateOpponentName(data.second as String)
            }
            BluetoothModel.START_NEW_GAME -> {
                mModel.setStartPlayerHealth(mView.getDefaultHealth())
                mView.setPlayerHealth(mModel.getPlayersHealth().toString())
            }
        }
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
            mView.showNoDeviceConnectedSnackBar()
        }
    }
}