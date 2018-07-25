package com.firerocks.mtgcounter.bluetooth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.root.App
import javax.inject.Inject

class BluetoothActivity: AppCompatActivity(), BluetoothMVP.View {

    companion object {
        // Message types sent from the BluetoothChatService Handler
        const val MESSAGE_STATE_CHANGE = 1
        const val MESSAGE_READ = 2
        const val MESSAGE_WRITE = 3
        const val MESSAGE_DEVICE_NAME = 4
        const val MESSAGE_TOAST = 5
        const val TOAST = "toast"
        const val DEVICE_NAME = "device_name"
    }

    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        mPresenter.setView(this)
    }

    override fun getDefaultHealth(): Int {
        return resources.getInteger(R.integer.default_player_health)
    }
}