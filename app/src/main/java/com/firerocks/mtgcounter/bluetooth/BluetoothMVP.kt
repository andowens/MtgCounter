package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothDevice
import com.firerocks.mtgcounter.counter.model.Player
import com.firerocks.mtgcounter.helpers.Operator
import java.util.*

interface BluetoothMVP {

    interface Presenter{
        /**
         * Helper function that injects the view into the presenter with a call back
         * tha lets the view know when it is done
         *
         * @param view The view to inject
         * @param onResult The callback used to tell the view that it has been set in
         *                 the presenter.
         */
        fun setView(view: BluetoothMVP.View, onResult: (String, Int) -> Unit)

        /**
         * Tells the presenter that the user has selected the new game button
         */
        fun menuNewGame()

        /**
         * Tells the presenter that the user has selected the up click button
         *
         * @param health The current health value for the player
         * @param onResult Callback used to notify view when it is done
         */
        fun upClicked(health: String, onResult: (String) -> Unit)

        /**
         * Tells the presenter that the user has selected the down button
         *
         * @param health The current health value or the player
         * @param onResult Callback used to notify view when it is done
         */
        fun downClicked(health: String, onResult: (String) -> Unit)

        /**
         * Tells the presenter that the user has selected a name to change
         *
         * @param name The current name of the player
         * @param onResult Callback used to notify view when it is done
         */
        fun nameClicked(name: String, onResult: (String) -> Unit)

        /**
         * Tells the presenter that a bluetooth device was selected
         *
         * @param address The address of the device selected
         */
        fun bluetoothDeviceSelected(address: String)

        /**
         * Tells the presenter to do onResume things
         */
        fun onResume()

        /**
         * Tells the presenter to do onPause things
         */
        fun onPause()

        /**
         * Need to have model check if the bluetooth is on and enabled
         *
         * @param onResult Callback used to notify view if bluetooth is enabled
         */
        fun checkBluetoothEnabled(onResult: (Boolean) -> Unit)

        /**
         * Notify presenter that the roll die button was selected
         */
        fun dieRollClicked()
    }

    interface View {

        /**
         * Used to get the default health from the resource file
         *
         * @return The default health
         */
        fun getDefaultHealth(): Int

        /**
         * Used get a random player name from the resource files
         *
         * @return The random name
         */
        fun getRandomPlayerName(): String

        /**
         * Used to display the no bluetooth dialog
         */
        fun showNoBluetoothDialog()

        /**
         * Used to request the bluetooth be turned on
         */
        fun requestBluetoothOn()

        /**
         * Used to launch the player dead snack bar
         *
         * @param player The dead players name
         */
        fun launchPlayerDeadSnackBar(player: String)

        /**
         * Used to show the no device connect snackbar
         */
        fun showNoDeviceConnectedSnackBar()

        /**
         * Tell the no device connect snackbar to go away since it is
         * permanent
         */
        fun dismissNoDeviceConnectedSnackBar()

        /**
         * Launch a generic snackbar with any message
         */
        fun errorSnackbar(error: String)

        /**
         * Tell the view to update the opponent details
         *
         * @param player The opponent players details
         */
        fun updateOpponent(player: Player)

        /**
         * Tell the view to update the devices player health
         *
         * @param health The health value to use to update
         */
        fun setPlayerHealth(health: String)

        /**
         * Tell the view to display the roll result
         *
         * @param result The result of the die roll
         */
        fun showDieRollResult(result: String)
    }

    interface Model {

        /**
         * Add an observer to the model so the model can report back to it
         *
         * @param observer The observer to add
         */
        fun addObserver(observer: Observer)

        /**
         * Get the player's name from the model
         *
         * @return The name of the player
         */
        fun getPlayersName(): String

        /**
         * Get the player's health from the model
         *
         * @return The health of the player
         */
        fun getPlayersHealth(): Int

        /**
         * Set the players health in the model
         *
         * @param health The health to set it to
         */
        fun setStartPlayerHealth(health: Int)

        /**
         * Set the players name
         *
         * @param name The name to set
         */
        fun setStartPlayerName(name: String)

        /**
         * Tell the opponent device that the player changed his name
         *
         * @param name The name to send to the opponent
         */
        fun changeNameNotifyOpponent(name: String)

        /**
         * Tell the model to connect to the passed in bluetooth device
         *
         * @param device The device to connect to
         */
        fun connectDevice(device: BluetoothDevice)

        /**
         * Get the state of the bluetooth service
         *
         * @return The state of the service
         */
        fun getServiceState(): Int

        /**
         * Tell the bluetooth service to start
         */
        fun startService()

        /**
         * Tell the bluetooth service to stop
         */
        fun stopService()

        /**
         * Tell the model to update the players health
         *
         * @param health The new health of the player
         * @param operatorType The operator type plus or minus
         *
         * @return The new value of the players health
         */
        fun updatePlayerHealth(health: Int, operatorType: Operator): Int

        /**
         * Send a new game request to the opponent
         *
         * @param onResult Call back for when it is done, that includes the opponent
         *                 can be passed up to the view so we can updated the opponents
         *                 details
         */
        fun sendNewGame(onResult: (Player) -> Unit)

        /**
         * Send the die roll result to the opponent's device
         *
         * @param roll The roll result
         */
        fun sendDieRollResult(roll: String)
    }
}