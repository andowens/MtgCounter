package com.firerocks.mtgcounter.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterFragment
import com.firerocks.mtgcounter.utils.adapters.DeviceAdapter
import kotlinx.android.synthetic.main.activity_device_list.*

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

    private lateinit var mNewDevicesAdapter: DeviceAdapter

    private val  mNewDeviceList = ArrayList<BluetoothDevice>()
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
        }

        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_device_list)

        mPairedDevicesAdapter = DeviceAdapter(mPairedDeviceList) {
            deviceClickListener(it)
        }
        mNewDevicesAdapter = DeviceAdapter(mNewDeviceList) {
            deviceClickListener(it)
        }


        paired_devices.adapter = mPairedDevicesAdapter

        paired_devices.layoutManager = LinearLayoutManager(this)

        // Register for broadcasts when discovery is finished
        val filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mReceiver, filter)

        // Register for broadcasts when a device is discovered
        val filter2 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter2)


        val pairedDevices = mBluetoothAdapter.bondedDevices

        pairedDevices.forEach { device ->
            mPairedDeviceList.add(device)
            mPairedDevicesAdapter.notifyItemInserted(mPairedDeviceList.size - 1)
        }
    }

    private val mReceiver = object :BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // When discovery finds a device
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Get the BluetoothDevice object from the intent
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    // If it's already paired skip it because it's been listed already
                    if (device.bondState != BluetoothDevice.BOND_BONDED) {

                        if (!mNewDeviceList.contains(device)) {
                            mNewDeviceList.add(device)
                            mNewDevicesAdapter.notifyItemInserted(mNewDeviceList.size - 1)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mReceiver)
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }
    }

    private fun doDiscovery() {

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }
        mNewDeviceList.clear()
        mNewDevicesAdapter.notifyDataSetChanged()

        // Request discovery from BluetoothAdapter
        mBluetoothAdapter.startDiscovery()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_BLUETOOTH_ON -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    val intent = Intent(this, CounterFragment::class.java)
                    startActivity(intent)
                } else {
                    doDiscovery()
                }
            }
        }
    }
}