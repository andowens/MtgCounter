package com.firerocks.mtgcounter.bluetooth.view

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.bluetooth.BluetoothMVP
import com.firerocks.mtgcounter.counter.model.Player
import com.firerocks.mtgcounter.counter.model.isDead
import com.firerocks.mtgcounter.helpers.animateView
import com.firerocks.mtgcounter.helpers.changeNameDialog
import com.firerocks.mtgcounter.views.CustomFontTextView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.bluetooth_view.*
import java.util.*
import javax.inject.Inject

class BluetoothFragment: DaggerFragment(), BluetoothMVP.View {

    private val TAG = "mtg.BlueTFrag"

    // Intent request codes
    companion object {
        private const val REQUEST_COURSE_PERMISSION = 3
        private const val REQUEST_BLUETOOTH_ON = 2
        const val DEVICE_SELECTED_RESULT = 1

        fun newInstance() : BluetoothFragment = BluetoothFragment()
    }

    private lateinit var mOpponentHealthTextView: CustomFontTextView
    private lateinit var mOpponentNameTextView: CustomFontTextView
    private lateinit var mNoDeviceSnackBar: Snackbar

    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mOpponentHealthTextView = view.findViewById(R.id.opponent_health)
        mOpponentNameTextView = view.findViewById(R.id.opponent_name)

        mNoDeviceSnackBar = Snackbar.make(main_view,
                getString(R.string.no_device_connected),
                Snackbar.LENGTH_INDEFINITE)

        mPresenter.setView(this) { name, health ->
            player_health.text = health.toString()
            player_name.text = name
        }

        dice_roll.setOnClickListener {
            mPresenter.dieRollClicked()
        }

        player_up_arrow.setOnClickListener {
            upClicked(it)
        }

        player_down_arrow.setOnClickListener {
            downClicked(it)
        }

        player_name.setOnClickListener {
            nameClicked(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.bluetooth_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            R.id.menu_new_game -> {
                mPresenter.menuNewGame()
                return true
            }
            R.id.menu_connect -> {
                launchConnectActivity()
            }
        }

        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bluetooth_view, container, false)
    }

    /**
     * Helper function that helps get the application context if it can
     *
     * @param lambda Passed in function that lets you use the context.
     */
    private fun appContext (lambda: (Context) -> Unit) {
        activity?.applicationContext?.let {
            lambda(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        //Have to request location permission
        appContext { context ->
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                activity?.let {
                    ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                            REQUEST_COURSE_PERMISSION)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mPresenter.checkBluetoothEnabled { bluetooth ->
            if (!bluetooth) {
                requestBluetoothOn()
            } else {
                mPresenter.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun getDefaultHealth(): Int {
        return resources.getInteger(R.integer.default_player_health)
    }

    override fun getRandomPlayerName(): String {
        val nameList = listOf<String>(getString(R.string.player_four_default_name),
                getString(R.string.player_three_default_name),
                getString(R.string.player_two_default_name),
                getString(R.string.player_one_default_name))

        val nameListSize = nameList.size - 1

        return nameList[Random().nextInt(nameListSize)]
    }

    private fun launchConnectActivity() {
        appContext {
            val intent = Intent(it, ConnectDeviceActivity::class.java)
            startActivityForResult(intent, DEVICE_SELECTED_RESULT)
        }
    }

    override fun onStop() {
        super.onStop()
        mNoDeviceSnackBar.dismiss()
    }

    fun upClicked(view: View) {
        appContext {
            animateView(it, view, R.animator.chevron_animation)
        }
        val health = player_health.text.toString()
        mPresenter.upClicked(health) { newHealth ->
            appContext {
                animateView(it, player_health, R.animator.health_animation)
            }
            player_health.text = newHealth
        }
    }

    fun downClicked(view: View) {
        val health = player_health.text.toString()
        appContext {
            animateView(it, view, R.animator.chevron_animation)
        }
        mPresenter.downClicked(health) { newHealth ->
            player_health.text = newHealth
            appContext {
                animateView(it, player_health, R.animator.health_animation)
            }
        }
    }

    fun nameClicked(view: View) {

        activity?.let {
            changeNameDialog(it) { name ->
                mPresenter.nameClicked(name) {
                    player_name.text = name
                }
            }
        }
    }

    override fun showDieRollResult(result: String) {
        activity?.runOnUiThread {
            roll_result.text = result
        }
    }

    override fun errorSnackbar(error: String) {
        if (isVisible) {
            val snackbar = Snackbar.make(main_view, error, Snackbar.LENGTH_LONG)
            snackbar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    showNoDeviceConnectedSnackBar()
                }
            }).show()
        }
    }

    override fun showNoBluetoothDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.bluetooth_dialog_title))
                    .setMessage(getString(R.string.no_bluetooth_on_device))
                    .setIcon(android.R.drawable.stat_sys_data_bluetooth)
                    .setPositiveButton("Ok") { dialog, which ->
                        activity?.onBackPressed()
                    }.show()
        }
    }

    override fun requestBluetoothOn() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
        startActivityForResult(discoverableIntent, REQUEST_BLUETOOTH_ON)
    }

    override fun showNoDeviceConnectedSnackBar() {
        mNoDeviceSnackBar.setAction(getString(R.string.connect_device)) {
            launchConnectActivity()
        }.show()
    }

    override fun dismissNoDeviceConnectedSnackBar() {
        mNoDeviceSnackBar.dismiss()
    }

    override fun launchPlayerDeadSnackBar(player: String) {
        Snackbar.make(main_view,
                resources.getString(R.string.player_dead, player), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.new_game)) {
                    mPresenter.menuNewGame()
                }.show()
    }

    override fun updateOpponent(player: Player) {
        activity?.runOnUiThread {
            mOpponentNameTextView.text = player.name
            mOpponentHealthTextView.text = player.health.toString()
            if (player.isDead()) {
                launchPlayerDeadSnackBar(player.name)
            }
            appContext {
                animateView(it, mOpponentHealthTextView, R.animator.health_animation)
            }
        }
    }

    override fun setPlayerHealth(health: String) {
        activity?.runOnUiThread {
            player_health.text = health
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_BLUETOOTH_ON -> {
                if (resultCode != Activity.RESULT_CANCELED) {
                    mPresenter.onResume()
                }
            }
            DEVICE_SELECTED_RESULT -> {
                if (data != null) {

                    val address: String? = data.getStringExtra(ConnectDeviceActivity.ADDRESS)
                    if (address != null) {
                        mPresenter.bluetoothDeviceSelected(address)
                    }
                } else {
                    showNoDeviceConnectedSnackBar()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_COURSE_PERMISSION) {
            for (permission in permissions) {
                if (android.Manifest.permission.ACCESS_COARSE_LOCATION == permission) {
                    if (grantResults[0] == PERMISSION_DENIED) {
                        appContext {
                            Toast.makeText(it, getString(R.string.coarse_access_refused),
                                    Toast.LENGTH_LONG).show()
                        }
                        activity?.onBackPressed()
                    }
                }
            }
        }
    }
}