package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Handler
import java.util.*

class BlueToothHelper constructor(private val mContext: Context, private val mHandler: Handler) {

    private val MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")

    private val NAME =  "BluetoothCounter"

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    companion object {
        const val STATE_NONE = 0 // We're doing nothing
        const val STATE_LISTEN = 1 // now listening for incoming connections
        const val STATE_CONNECTING = 2 // now initiating an outgoing connection
        const val STATE_CONNECTED = 3 // now connected to a remote device
    }

    private var mLock = Any()

    private var mState = STATE_NONE

    private fun setState(state: Int) {
        synchronized(mLock) {
            mState = state

            mHandler.obtainMessage()
        }
    }
}