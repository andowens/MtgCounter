package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterActivity
import com.firerocks.mtgcounter.helpers.changeNameDialog
import com.firerocks.mtgcounter.root.App
import kotlinx.android.synthetic.main.bluetooth_view.*
import java.util.*
import javax.inject.Inject

class BluetoothActivity: AppCompatActivity(), BluetoothMVP.View {

    // Intent request codes
    private val REQUEST_CONNECT_DEVICE = 1
    private val REQUEST_BLUETOOTH_ON = 2


    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_view)

        (application as App).appComponent.inject(this)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mPresenter.setView(this) { name, health ->
            player_health.text = health.toString()
            player_name.text = name
        }
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
                mPresenter.menuNewGame {
                }
                return true
            }
            R.id.menu_bluetooth -> {
                mPresenter.discoverBluetooth()
                return true
            }
        }

        return false
    }

    fun upClicked(view: View) {
        mPresenter.upClicked()
    }

    fun downClicked(view: View) {
        mPresenter.downClicked()
    }

    fun nameClicked(view: View) {

        changeNameDialog(this) { name ->
            mPresenter.nameClicked(name) {
                player_name.text = name
            }
        }
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
}