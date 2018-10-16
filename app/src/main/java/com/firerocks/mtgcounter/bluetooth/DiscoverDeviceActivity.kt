package com.firerocks.mtgcounter.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.CounterActivity
import com.firerocks.mtgcounter.utils.adapters.DeviceAdapter
import kotlinx.android.synthetic.main.activity_device_list.*

/**
 * Created by Andrew on 7/29/2018.
 */
class DiscoverDeviceActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_BLUETOOTH_ON = 2
        const val ADDRESS = "address_extra"
    }

    private val mBluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    private lateinit var mPairedDevicesAdapter: DeviceAdapter

    private lateinit var mNewDevicesAdapter: DeviceAdapter

    private val  mNewDeviceList = ArrayList<String>()
    private val mPairedDeviceList = ArrayList<String>()

    // The on-click listener for all devices in the ListViews
    private fun deviceClickListener(device: String) {

        // Cancel discovery because it's costly and we're about to connect
        mBluetoothAdapter.cancelDiscovery()

        // Get the device MAC address, which is the last 17 chars in the string
        val address = device.substring(device.length - 17)
        //dialog.dismiss()
        // Create the result Intent and include the MAC address
        //mListener.onDeviceItemClicked(this, address)
        val intent = Intent()
        intent.putExtra(ADDRESS, address)

        setResult(BluetoothActivity.DEVICE_SELECTED_RESULT, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_device_list)

        mPairedDevicesAdapter = DeviceAdapter(mPairedDeviceList) {
            deviceClickListener(it)
        }
        mNewDevicesAdapter = DeviceAdapter(mNewDeviceList) {
            deviceClickListener(it)
        }


        new_devices.adapter = mNewDevicesAdapter
        paired_devices.adapter = mPairedDevicesAdapter

        new_devices.layoutManager = LinearLayoutManager(this)
        paired_devices.layoutManager = LinearLayoutManager(this)

        // Register for broadcasts when discovery is finished
        val filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mReceiver, filter)

        // Register for broadcasts when a device is discovered
        val filter2 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter2)


        val pairedDevices = mBluetoothAdapter.bondedDevices

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                mPairedDeviceList.add(device.name + "\n" + device.address)
            }
        } else {
            val noDevices = resources.getText(R.string.none_paired).toString()
            mPairedDeviceList.add(noDevices)
        }

        mPairedDevicesAdapter.notifyDataSetChanged()

        cancel_button.setOnClickListener {
            onBackPressed()
        }

        scan_button.setOnClickListener {
            bluetooth_searching_progress.visibility = View.VISIBLE
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
            startActivityForResult(discoverableIntent, REQUEST_BLUETOOTH_ON)
        }
    }

    private val mReceiver = object :BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // If it's already paired skip it because it's been listed already
                if (device.bondState != BluetoothDevice.BOND_BONDED) {
                    val deviceString = device.name + "\n" + device.address
                    if (!mNewDeviceList.contains(deviceString)) {
                        mNewDeviceList.add(deviceString)
                        mNewDevicesAdapter.notifyDataSetChanged()
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                bluetooth_searching_progress.visibility = View.INVISIBLE
                if (mNewDeviceList.size == 0) {
                    val noDevices = resources.getText(R.string.none_found).toString()
                    mNewDeviceList.add(noDevices)
                    mNewDevicesAdapter.notifyDataSetChanged()
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
                    val intent = Intent(this, CounterActivity::class.java)
                    startActivity(intent)
                } else {
                    doDiscovery()
                }
            }
        }
    }
}