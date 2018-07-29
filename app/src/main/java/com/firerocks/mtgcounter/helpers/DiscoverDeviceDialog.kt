package com.firerocks.mtgcounter.helpers

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import com.firerocks.mtgcounter.R
import javax.inject.Inject

/**
 * Created by Andrew on 7/29/2018.
 */
class DiscoverDeviceDialog(context: Context) : AlertDialog(context) {

    @Inject lateinit var mblueToothAdapter: BluetoothAdapter

    private val mPairedDevicesAdapter = ArrayAdapter<String>(context, R.layout.device_name)

    private val mNewDevicesAdapter = ArrayAdapter<String>(context, R.layout.device_name)



    override fun setView(view: View?) {
        super.setView(view)


    }
}