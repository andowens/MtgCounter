package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BlueToothHelper constructor(private val observer: Observer<Pair<Int, Any>>) {

    private val TAG = " BluetoothChatService"

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    // Unique UUID for this application
    private val MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")

    // Name for the SDP record when creating server socket
    private val NAME =  "BluetoothCounter"

    private var mAcceptThread: AcceptThread? = null
    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null

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

            val gson = Gson()
            gson.toJson

            Observable.just(Pair(BluetoothActivity.MESSAGE_DEVICE_NAME, mState))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe(observer)
        }
    }

    fun getState() : Int {
        synchronized(mLock) {
            return mState
        }
    }

    private fun connected(socket: BluetoothSocket, device: BluetoothDevice) {
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
        // Cancel the accept thread because we only want to connect ot one device
        if (mAcceptThread != null) {
            mAcceptThread?.cancel()
            mAcceptThread = null
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread?.start()
        // Send the name of the connected device back to the UI Activity

        Observable.just(Pair(BluetoothActivity.MESSAGE_DEVICE_NAME, device.name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer)

        setState(STATE_CONNECTED)

    }

    fun connect(device: BluetoothDevice) {
        synchronized(mLock) {
            if (mState == STATE_CONNECTING) {
                // Cancel any thread attempting to make a connection
                if (mConnectThread != null) {
                    mConnectThread?.cancel()
                    mConnectThread = null
                }
            }
            // Cancel any thread currently running a connection
            if (mConnectedThread != null) {
                mConnectedThread?.cancel()
                mConnectedThread = null
            }
            // Start the thread to connect with the given device
            mConnectThread = ConnectThread(device)
            mConnectThread?.start()
            setState(STATE_CONNECTING)
        }
    }

    fun stop() {
        synchronized(mLock) {
            if (mConnectedThread != null) {
                mConnectedThread?.cancel()
                mConnectedThread = null
            }
            if (mConnectThread != null) {
                mConnectThread?.cancel()
                mConnectThread = null
            }
            if (mAcceptThread != null) {
                mAcceptThread?.cancel()
                mAcceptThread = null
            }
            setState(STATE_NONE)
        }
    }

    private fun start() {
        synchronized(mLock) {
            // Cancel any thread attempting to make a connection
            if (mConnectThread != null) {
                mConnectThread?.cancel()
                mConnectThread = null
            }
            // Cancel any thread currently running a connection
            if (mConnectedThread != null) {
                mConnectedThread?.cancel()
                mConnectedThread = null
            }
            // Start the thread to listen on a BluetoothServerSocket
            if (mAcceptThread == null) {
                mAcceptThread = AcceptThread()
                mAcceptThread?.start()
            }
        }
        setState(STATE_LISTEN)
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    fun write(out: ByteArray) {
        // Create temporary object
        var r: ConnectedThread
        // Synchronize a copy of the ConnectedThread
        synchronized(mLock) {
            if (mState != STATE_CONNECTED) return
            r = mConnectedThread ?: return // If mConnectedThread is null return

            r.write(out)
        }
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private fun connectionFailed() {
        setState(STATE_LISTEN)

        Observable.just(Pair(BluetoothActivity.MESSAGE_TOAST, "Unable to connect to device"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer)
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private fun connectionLost() {
        setState(STATE_LISTEN)

        Observable.just(Pair(BluetoothActivity.MESSAGE_TOAST, "Device connection was lost"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer)
    }

    inner class AcceptThread: Thread() {

        // The local server socket
        private val mServerSocket= mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID)

        private lateinit var socket: BluetoothSocket

        override fun run() {
            name = "AcceptThread"
            while (mState != STATE_CONNECTED) {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mServerSocket.accept()

                synchronized(mLock) {
                    when (mState) {
                        STATE_LISTEN, STATE_CONNECTING -> {
                            // Situation normal. Start the connected thread
                            connected(socket, socket.remoteDevice)
                        }
                        STATE_NONE, STATE_CONNECTED -> {
                            socket.close()
                        }
                        else -> Log.e(TAG, "Invalid state")
                    }
                }
            }
        }

        fun cancel() {
            mServerSocket.close()
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private inner class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket?

        init {
            var tmp: BluetoothSocket? = null
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID)
            } catch (e: IOException) {
                Log.e(TAG, "create() failed", e)
            }

            mmSocket = tmp
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectThread")
            name = "ConnectThread"
            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery()
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket!!.connect()
            } catch (e: IOException) {
                connectionFailed()
                // Close the socket
                try {
                    mmSocket!!.close()
                } catch (e2: IOException) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2)
                }

                // Start the service over to restart listening mode
                this@BlueToothHelper.start()
                return
            }

            // Reset the ConnectThread because we're done
            synchronized(mLock) {
                mConnectThread = null
            }
            // Start the connected thread
            connected(mmSocket, mmDevice)
        }

        fun cancel() {
            try {
                mmSocket!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }

        }
    }


    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        init {
            Log.d(TAG, "create ConnectedThread")
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
                Log.e(TAG, "temp sockets not created", e)
            }

            mmInStream = tmpIn
            mmOutStream = tmpOut
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectedThread")
            val buffer = ByteArray(1024)
            var bytes: Int
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream!!.read(buffer)
                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(BluetoothActivity.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget()
                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    connectionLost()
                    break
                }

            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        fun write(buffer: ByteArray) {
            try {
                mmOutStream!!.write(buffer)
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(BluetoothActivity.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget()
            } catch (e: IOException) {
                Log.e(TAG, "Exception during write", e)
            }

        }

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }

        }
    }
}