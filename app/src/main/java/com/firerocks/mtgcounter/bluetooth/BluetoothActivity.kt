package com.firerocks.mtgcounter.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterActivity
import com.firerocks.mtgcounter.helpers.changeNameDialog
import com.firerocks.mtgcounter.helpers.rollDiceDialog
import com.firerocks.mtgcounter.root.App
import com.firerocks.mtgcounter.views.CustomFontTextView
import kotlinx.android.synthetic.main.bluetooth_view.*
import java.util.*
import javax.inject.Inject

class BluetoothActivity: AppCompatActivity(), BluetoothMVP.View {

    private val TAG = "mtg.BlueTActivity"

    // Intent request codes
    companion object {
        private const val REQUEST_BLUETOOTH_ON = 2
        const val DEVICE_SELECTED_RESULT = 1
    }

    private lateinit var mOpponentHealthTextView: CustomFontTextView
    private lateinit var mOpponentNameTextView: CustomFontTextView
    private lateinit var mNoDeviceSnackBar: com.google.android.material.snackbar.Snackbar
    private var mBluetoothOn = false

    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_view)

        mOpponentHealthTextView = findViewById(R.id.opponent_health)
        mOpponentNameTextView = findViewById(R.id.opponent_name)
        (application as App).appComponent.inject(this)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //Have to request location permission
        val requestCoursePermission = 1
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            requestCoursePermission)

        mNoDeviceSnackBar = com.google.android.material.snackbar.Snackbar.make(findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_view),
                getString(R.string.no_device_connected),
                com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE)

        mPresenter.setView(this) { name, health, bluetooth ->
            player_health.text = health.toString()
            player_name.text = name
            mBluetoothOn = bluetooth
        }
    }

    override fun onResume() {
        super.onResume()
        if (mBluetoothOn) {
            mPresenter.onResume()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("OnSTART", "Onstart started")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Onstop", "Onstopped")
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
            R.id.menu_roll -> {
                rollDiceDialog(this)
                return true
            }
            R.id.menu_discover -> {
                val intent = Intent(this, DiscoverDeviceActivity::class.java)
                startActivityForResult(intent, DEVICE_SELECTED_RESULT)

                return true
            }
        }

        return false
    }

    fun upClicked(view: View) {
        val health = player_health.text.toString()
        mPresenter.upClicked(health) { newHealth ->
            player_health.text = newHealth
        }
    }

    fun downClicked(view: View) {
        val health = player_health.text.toString()
        mPresenter.downClicked(health) { newHealth ->
            player_health.text = newHealth
        }
    }

    fun nameClicked(view: View) {

        changeNameDialog(this) { name ->
            mPresenter.nameClicked(name) {
                player_name.text = name
            }
        }
    }

    override fun errorSnackbar(error: String) {
        com.google.android.material.snackbar.Snackbar.make(main_view,
                error,
                com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show()
    }

    override fun showNoBluetoothDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.bluetooth_dialog_title))
                .setMessage(getString(R.string.no_bluetooth_on_device))
                .setIcon(android.R.drawable.stat_sys_data_bluetooth)
                .setPositiveButton("Ok") { dialog, which ->
                    val intent = Intent(this, CounterActivity::class.java)
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


        }.show()
    }

    override fun dismissNoDeviceConnectedSnackBar() {
        mNoDeviceSnackBar.dismiss()
    }

    override fun launchPlayerDeadSnackBar(player: String) {
        com.google.android.material.snackbar.Snackbar.make(main_view,
                resources.getString(R.string.player_dead, player),
                com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.new_game)) {

                }.show()
    }

    override fun updateOpponentHealth(health: String) {
        mOpponentHealthTextView.text = health
    }

    override fun updateOpponentName(name: String) {
        mOpponentNameTextView.text = name
    }

    override fun setPlayerHealth(health: String) {
        player_health.text = health
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == DEVICE_SELECTED_RESULT) {

            data?.let {
                val address: String? = it.getStringExtra(DiscoverDeviceActivity.ADDRESS)
                if (address != null) {
                    mPresenter.bluetoothDeviceSelected(address)
                }
            }
        }
        if (requestCode == REQUEST_BLUETOOTH_ON) {
            if (resultCode != Activity.RESULT_CANCELED) {
                mPresenter.onPause()
            }
        }
    }
}