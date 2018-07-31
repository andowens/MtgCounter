package com.firerocks.mtgcounter.helpers

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.firerocks.mtgcounter.R
import javax.inject.Inject

/**
 * Created by Andrew on 7/29/2018.
 */
class DiscoverDeviceDialog : DialogFragment() {

    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private lateinit var mPairedDevicesAdapter: ArrayAdapter<String>

    private lateinit var mNewDevicesAdapter: ArrayAdapter<String>

    private lateinit var mNewDeviceListListView: ListView
    private lateinit var mPairedDevicesListView: ListView
    private lateinit var mListener: DiscoverDeviceDialogListener

    // The on-click listener for all devices in the ListViews
    private val mDeviceClickListener = AdapterView.OnItemClickListener {
        parent, view, position, id ->

        // Cancel discovery because it's costly and we're about to connect
        mBluetoothAdapter.cancelDiscovery()

        // Get the device MAC address, which is the last 17 chars in the View
        val info = (view as TextView).text.toString()
        val address = info.substring(info.length - 17)
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

        mPairedDevicesAdapter = ArrayAdapter(context, R.layout.device_name)
        mNewDevicesAdapter = ArrayAdapter(context, R.layout.device_name)

        val baseView = inflater?.inflate(R.layout.activity_device_list, null)

        builder.setView(baseView)

        mNewDeviceListListView = baseView!!.findViewById(R.id.new_devices)
        mPairedDevicesListView = baseView.findViewById(R.id.paired_devices)

        mNewDeviceListListView.adapter = mNewDevicesAdapter
        mPairedDevicesListView.adapter = mPairedDevicesAdapter

        mNewDeviceListListView.onItemClickListener = mDeviceClickListener
        mPairedDevicesListView.onItemClickListener = mDeviceClickListener

        // Register for broadcasts when a device is discovered
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(mReceiver, filter)
        // Register for broadcasts when discovery is finished
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        activity?.registerReceiver(mReceiver, filter)

        val pairedDevices = mBluetoothAdapter.bondedDevices

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size > 0) {
            view?.findViewById<TextView>(R.id.title_paired_devices)?.visibility = View.VISIBLE
            for (device in pairedDevices) {
                mPairedDevicesAdapter.add(device.name + "\n" + device.address)
            }
        } else {
            val noDevices = resources.getText(R.string.none_paired).toString()
            mPairedDevicesAdapter.add(noDevices)
        }
        builder.setNeutralButton(R.string.button_scan, null)

        builder.setNegativeButton(R.string.cancel) { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()

        // Need to override the on click for the neutral button so it doesn't close the dialog
        alertDialog.setOnShowListener {
            val neutralButton = (it as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
            neutralButton.setOnClickListener {
                doDiscovery()
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
                    mNewDevicesAdapter.add(device.name + "\n" + device.address)
                    mNewDevicesAdapter.notifyDataSetChanged()
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                dialog.setTitle(R.string.select_device)
                if (mNewDevicesAdapter.count == 0) {
                    val noDevices = resources.getText(R.string.none_found).toString()
                    mNewDevicesAdapter.add(noDevices)
                }
            }
        }
    }

    private fun doDiscovery() {
        dialog.setTitle(R.string.scanning)
        // Turn on su-title for new devices
        view?.findViewById<TextView>(R.id.title_new_devices)?.visibility = View.VISIBLE

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }

        // Request discovery from BluetoothAdapter
        mBluetoothAdapter.startDiscovery()
    }
}