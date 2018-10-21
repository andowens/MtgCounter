package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
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
    private val MY_UUID = UUID.fromString("57417e98-15dc-47a2-8c9d-fff04b2f987c")

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

            Observable.just(Pair(BluetoothModel.MESSAGE_STATE_CHANGE, mState))
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

        Observable.just(Pair(BluetoothModel.MESSAGE_CONNECTED, device.name))
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
        }
        setState(STATE_CONNECTING)
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
        }
        setState(STATE_NONE)
    }

    fun start() {
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

        Observable.just(Pair(BluetoothModel.MESSAGE_SNACKBAR, "Unable to connect to device"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer)
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private fun connectionLost() {
        setState(STATE_LISTEN)

        Observable.just(Pair(BluetoothModel.MESSAGE_SNACKBAR, "Device connection was lost"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer)
    }

    inner class AcceptThread: Thread() {

        // The local server socket
        private val mServerSocket : BluetoothServerSocket? by lazy {
            mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID)
        }

        override fun run() {
            name = "AcceptThread"
            while (mState != STATE_CONNECTED) {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                val socket : BluetoothSocket? = try {
                    mServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "accept()failed", e)
                    break
                }
                synchronized(mLock) {
                    when (mState) {
                        STATE_LISTEN, STATE_CONNECTING -> {
                            // Situation normal. Start the connected thread
                            connected(socket!!, socket.remoteDevice)
                        }
                        STATE_NONE, STATE_CONNECTED -> {
                            socket?.close()
                        }
                        else -> Log.e(TAG, "Invalid state")
                    }
                }
            }
        }

        fun cancel() {
            mServerSocket?.close()
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private inner class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            mmDevice.createRfcommSocketToServiceRecord(MY_UUID)
        }


        override fun run() {
            Log.i(TAG, "BEGIN mConnectThread")
            name = "ConnectThread"
            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery()
            // Make a connection to the BluetoothSocket
            try {

                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket?.connect()
            } catch (e: IOException) {
                connectionFailed()
                // Close the socket
                cancel()

                // Start the service over to restart listening mode
                this@BlueToothHelper.start()
                return
            }

            // Reset the ConnectThread because we're done
            synchronized(mLock) {
                mConnectThread = null
            }
            // Start the connected thread
            mmSocket?.let { socket ->
                connected(socket, mmDevice)
            }
        }

        fun cancel() {
            try {
                mmSocket?.use { socket ->
                    socket.close()
                }
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
        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        val buffer = ByteArray(1024)


        override fun run() {
            var bytes: Int
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer)

                    Observable.just(Pair(BluetoothModel.MESSAGE_READ, String(buffer, 0, bytes)))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(Schedulers.newThread())
                            .subscribe(observer)

                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    connectionLost()
                    this@BlueToothHelper.start()
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
                mmOutStream.write(buffer)

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