package com.firerocks.mtgcounter.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterFragment
import com.firerocks.mtgcounter.data.Player
import com.firerocks.mtgcounter.helpers.animateView
import com.firerocks.mtgcounter.helpers.changeNameDialog
import com.firerocks.mtgcounter.root.App
import com.firerocks.mtgcounter.views.CustomFontTextView
import kotlinx.android.synthetic.main.bluetooth_view.*
import java.util.*
import javax.inject.Inject

class BluetoothActivity: AppCompatActivity(), BluetoothMVP.View {

    private val TAG = "mtg.BlueTActivity"

    // Intent request codes
    companion object {
        private const val REQUEST_COURSE_PERMISSION = 3
        private const val REQUEST_BLUETOOTH_ON = 2
        const val DEVICE_SELECTED_RESULT = 1
    }

    private lateinit var mOpponentHealthTextView: CustomFontTextView
    private lateinit var mOpponentNameTextView: CustomFontTextView
    private lateinit var mNoDeviceSnackBar: Snackbar

    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_view)

        mOpponentHealthTextView = findViewById(R.id.opponent_health)
        mOpponentNameTextView = findViewById(R.id.opponent_name)
        (application as App).appComponent.inject(this)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //Have to request location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_COURSE_PERMISSION)
        }

        mNoDeviceSnackBar = Snackbar.make(findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_view),
                getString(R.string.no_device_connected),
                Snackbar.LENGTH_INDEFINITE)

        mPresenter.setView(this) { name, health ->
            player_health.text = health.toString()
            player_name.text = name
        }

        dice_roll.setOnClickListener {
            mPresenter.dieRollClicked()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.bluetooth_menu, menu)
        return true
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
                return true
            }
        }

        return false
    }

    private fun launchConnectActivity() {
        val intent = Intent(this, ConnectDeviceActivity::class.java)
        startActivityForResult(intent, DEVICE_SELECTED_RESULT)
    }

    fun upClicked(view: View) {
        animateView(applicationContext, view, R.animator.chevron_animation)
        val health = player_health.text.toString()
        mPresenter.upClicked(health) { newHealth ->
            animateView(applicationContext, player_health, R.animator.health_animation)
            player_health.text = newHealth
        }
    }

    fun downClicked(view: View) {
        val health = player_health.text.toString()
        animateView(this, view, R.animator.chevron_animation)
        mPresenter.downClicked(health) { newHealth ->
            player_health.text = newHealth
            animateView(applicationContext, player_health, R.animator.health_animation)
        }
    }

    fun nameClicked(view: View) {

        changeNameDialog(this) { name ->
            mPresenter.nameClicked(name) {
                player_name.text = name
            }
        }
    }

    override fun showDieRollResult(result: String) {
        roll_result.text = result
    }

    override fun errorSnackbar(error: String) {
        val snackbar = Snackbar.make(main_view, error, Snackbar.LENGTH_LONG)
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                showNoDeviceConnectedSnackBar()
            }
        }).show()
    }

    override fun showNoBluetoothDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.bluetooth_dialog_title))
                .setMessage(getString(R.string.no_bluetooth_on_device))
                .setIcon(android.R.drawable.stat_sys_data_bluetooth)
                .setPositiveButton("Ok") { dialog, which ->
                    val intent = Intent(this, CounterFragment::class.java)
                    startActivity(intent)
                }.show()
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

                }.show()
    }

    override fun updateOpponent(player: Player) {
        runOnUiThread {
            mOpponentNameTextView.text = player.name
            mOpponentHealthTextView.text = player.health.toString()
            animateView(applicationContext, mOpponentHealthTextView, R.animator.health_animation)
        }
    }

    override fun setPlayerHealth(health: String) {
        runOnUiThread {
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

                data?.let {
                    val address: String? = it.getStringExtra(ConnectDeviceActivity.ADDRESS)
                    if (address != null) {
                        mPresenter.bluetoothDeviceSelected(address)
                    }
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
                        Toast.makeText(applicationContext, getString(R.string.coarse_access_refused),
                                Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
    }
}