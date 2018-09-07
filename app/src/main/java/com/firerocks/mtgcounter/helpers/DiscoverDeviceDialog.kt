package com.firerocks.mtgcounter.helpers

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.utils.adapters.DeviceAdapter

/**
 * Created by Andrew on 7/29/2018.
 */
class DiscoverDeviceDialog : DialogFragment() {

    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private lateinit var mPairedDevicesAdapter: DeviceAdapter

    private lateinit var mNewDevicesAdapter: DeviceAdapter

    private lateinit var mNewDeviceRecyclerView: RecyclerView
    private lateinit var mPairedDevicesRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mListener: DiscoverDeviceDialogListener
    private val  mNewDeviceList = ArrayList<String>()
    private val mPairedDeviceList = ArrayList<String>()

    // The on-click listener for all devices in the ListViews
    private fun mDeviceClickListener(device: String) {

        // Cancel discovery because it's costly and we're about to connect
        mBluetoothAdapter.cancelDiscovery()

        // Get the device MAC address, which is the last 17 chars in the string
        val address = device.substring(device.length - 17)
        dialog.dismiss()
        // Create the result Intent and include the MAC address
        mListener.onDeviceItemClicked(this, address)
    }

    interface DiscoverDeviceDialogListener {
        fun onDeviceItemClicked(dialogFragment: DialogFragment, address: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            mListener = (context as DiscoverDeviceDialogListener)
        } catch (e: Exception) {
            throw Exception(context.toString())
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val inflater = activity?.layoutInflater

        mPairedDevicesAdapter = DeviceAdapter(mPairedDeviceList) {
            mDeviceClickListener(it)
        }
        mNewDevicesAdapter = DeviceAdapter(mNewDeviceList) {
            mDeviceClickListener(it)
        }

        val baseView = inflater?.inflate(R.layout.activity_device_list, null)

        builder.setView(baseView)

        mNewDeviceRecyclerView = baseView!!.findViewById(R.id.new_devices)
        mPairedDevicesRecyclerView = baseView.findViewById(R.id.paired_devices)
        mProgressBar = baseView.findViewById(R.id.bluetooth_searching_progress)

        mNewDeviceRecyclerView.adapter = mNewDevicesAdapter
        mPairedDevicesRecyclerView.adapter = mPairedDevicesAdapter

        mNewDeviceRecyclerView.layoutManager = LinearLayoutManager(context)
        mPairedDevicesRecyclerView.layoutManager = LinearLayoutManager(context)

        // Register for broadcasts when discovery is finished
        val filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        activity?.registerReceiver(mReceiver, filter)

        // Register for broadcasts when a device is discovered
        val filter2 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(mReceiver, filter2)


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
        Log.i("TAG", "Size: ${pairedDevices.size}")
        mPairedDevicesAdapter.notifyDataSetChanged()
        builder.setNeutralButton(R.string.button_scan, null)

        builder.setNegativeButton(R.string.cancel) { dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()

        // Need to override the on click for the neutral button so it doesn't close the dialog
        alertDialog.setOnShowListener {
            val neutralButton = (it as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
            neutralButton.setOnClickListener {
                doDiscovery()
                mProgressBar.visibility = View.VISIBLE
            }
        }


        return alertDialog
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
                dialog?.setTitle(R.string.select_device)
                mProgressBar.visibility = View.INVISIBLE
                if (mNewDeviceList.size == 0) {
                    val noDevices = resources.getText(R.string.none_found).toString()
                    mNewDeviceList.add(noDevices)
                    mNewDevicesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        // Need to unregister the receiver once the dialog is dismissed
        activity?.unregisterReceiver(mReceiver)
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }
    }

    private fun doDiscovery() {
        dialog.setTitle(R.string.scanning)

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }
        mNewDeviceList.clear()
        mNewDevicesAdapter.notifyDataSetChanged()

        // Request discovery from BluetoothAdapter
        mBluetoothAdapter.startDiscovery()
    }
}