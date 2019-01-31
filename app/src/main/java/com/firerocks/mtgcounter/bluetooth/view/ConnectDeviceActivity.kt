package com.firerocks.mtgcounter.bluetooth.view

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.view.CounterFragment
import com.firerocks.mtgcounter.utils.adapters.DeviceAdapter
import kotlinx.android.synthetic.main.fragment_device_list.*

/**
 * Created by Andrew on 7/29/2018.
 */
class ConnectDeviceActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_BLUETOOTH_ON = 2
        private const val PAIR_REQUEST_CODE = 3
        const val ADDRESS = "address_extra"
    }

    private val mBluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    private lateinit var mPairedDevicesAdapter: DeviceAdapter

    private val mPairedDeviceList = ArrayList<BluetoothDevice>()

    // The on-click listener for all devices in the ListViews
    private fun deviceClickListener(device: BluetoothDevice) {

        // Cancel discovery because it's costly and we're about to connect
        mBluetoothAdapter.cancelDiscovery()

        val address = device.address

        // Create the result Intent and include the MAC address
        val intent = Intent()
        intent.putExtra(ADDRESS, address)

        setResult(BluetoothFragment.DEVICE_SELECTED_RESULT, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            R.id.pair_device -> {
                val intent = Intent()
                intent.action = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
                startActivity(intent)
            }
        }

        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.connect_device_menu, menu)

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.fragment_device_list)

        mPairedDevicesAdapter = DeviceAdapter(mPairedDeviceList) {
            deviceClickListener(it)
        }

        paired_devices.adapter = mPairedDevicesAdapter

        paired_devices.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        val pairedDevices = mBluetoothAdapter.bondedDevices

        if (pairedDevices.isEmpty()) {
            no_paired.visibility = View.VISIBLE
            paired_devices.visibility = View.GONE
        } else {

            pairedDevices.forEach { device ->
                no_paired.visibility = View.GONE
                paired_devices.visibility = View.VISIBLE
                mPairedDeviceList.add(device)
                mPairedDevicesAdapter.notifyItemInserted(mPairedDeviceList.size - 1)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mPairedDeviceList.clear()
        mPairedDevicesAdapter.notifyDataSetChanged()
    }
}